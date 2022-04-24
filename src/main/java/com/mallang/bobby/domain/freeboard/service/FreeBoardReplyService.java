package com.mallang.bobby.domain.freeboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardReplyDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardReply;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardReplyRepository;
import com.mallang.bobby.dto.PagingCursorDto;
import com.mallang.bobby.exception.NotLoginException;
import com.mallang.bobby.exception.PermissionDeniedException;
import com.mallang.bobby.exception.UnExpectedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardReplyService {
	private final ModelMapper modelMapper;
	private final FreeBoardReplyRepository freeBoardReplyRepository;

	public PagingCursorDto<FreeBoardReplyDto> get(long freeBoardCommentId, long cursor, int size) {
		final List<FreeBoardReply> freeBoardReplyList = freeBoardReplyRepository.findAllByFreeBoardCommentIdOrderByIdDesc(freeBoardCommentId, cursor, size);
		if (CollectionUtils.isEmpty(freeBoardReplyList)) {
			return PagingCursorDto.<FreeBoardReplyDto>builder()
				.cursor(null)
				.isLast(true)
				.items(new ArrayList<>())
				.build();
		}

		final long nextCursor = freeBoardReplyList.get(freeBoardReplyList.size()-1).getId();
		final boolean existNextPage = freeBoardReplyRepository.existsByFreeBoardCommentIdAndIdLessThan(freeBoardCommentId, nextCursor);

		return PagingCursorDto.<FreeBoardReplyDto>builder()
			.cursor(nextCursor)
			.isLast(!existNextPage)
			.items(freeBoardReplyList.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList()))
			.build();
	}

	private FreeBoardReplyDto mapToDto(FreeBoardReply freeBoardReply) {
		final FreeBoardReplyDto freeBoardReplyDto = modelMapper.map(freeBoardReply, FreeBoardReplyDto.class);
		if (freeBoardReplyDto.getIsDeleted()) {
			return FreeBoardReplyDto.builder()
				.id(freeBoardReplyDto.getId())
				.freeBoardCommentId(freeBoardReplyDto.getFreeBoardCommentId())
				.contents("삭제된 답글입니다.")
				.writerId(0L)
				.writerNickname("")
				.isDeleted(true)
				.createdAt(null)
				.modifiedAt(null)
				.build();
		}
		return freeBoardReplyDto;
	}

	public Long save(FreeBoardReplyDto freeBoardReplyDto, UserDto userDto) {
		try {
			if (userDto == null || userDto.getId() == null) {
				throw new NotLoginException();
			}

			if (freeBoardReplyDto.getId() == null) {
				return insert(freeBoardReplyDto, userDto);
			}

			return update(freeBoardReplyDto, userDto);
		} catch (Exception e) {
			log.error("FreeBoardCommentReplyService.save(): {}", e.getMessage());
			throw e;
		}
	}

	private Long insert(FreeBoardReplyDto freeBoardReplyDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardReply freeBoardReply = new FreeBoardReply();
		freeBoardReply.setFreeBoardCommentId(freeBoardReplyDto.getFreeBoardCommentId());
		freeBoardReply.setContents(freeBoardReplyDto.getContents());
		freeBoardReply.setWriterId(userDto.getId());
		freeBoardReply.setWriterNickname(userDto.getNickname());
		freeBoardReply.setLikeCount(0);
		freeBoardReply.setIsDeleted(false);

		return freeBoardReplyRepository.save(freeBoardReply).getId();
	}

	private Long update(FreeBoardReplyDto freeBoardReplyDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardReply freeBoardReply = freeBoardReplyRepository.findById(freeBoardReplyDto.getId()).orElse(null);

		if (freeBoardReply == null) {
			throw new UnExpectedException("수정하려는 답글이 없습니다.(id=" + freeBoardReplyDto.getId() + ")");
		} else if (!Objects.equals(freeBoardReply.getWriterId(), userDto.getId())) {
			throw new PermissionDeniedException();
		}

		freeBoardReply.setContents(freeBoardReplyDto.getContents());

		return freeBoardReplyRepository.save(freeBoardReply).getId();
	}

	public void remove(long id, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardReply freeBoardReply = freeBoardReplyRepository.findById(id).orElse(null);

		if (freeBoardReply == null) {
			throw new UnExpectedException("삭제하려는 답글이 없습니다.(id=" + id + ")");
		} else if (!Objects.equals(freeBoardReply.getWriterId(), userDto.getId())) {
			throw new PermissionDeniedException();
		}

		freeBoardReply.setIsDeleted(true);

		freeBoardReplyRepository.save(freeBoardReply);
	}
}

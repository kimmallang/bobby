package com.mallang.bobby.domain.freeboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentReplyDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardCommentReply;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardCommentReplyRepository;
import com.mallang.bobby.dto.PagingCursorDto;
import com.mallang.bobby.dto.PagingDto;
import com.mallang.bobby.exception.NotLoginException;
import com.mallang.bobby.exception.PermissionDeniedException;
import com.mallang.bobby.exception.UnExpectedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardCommentReplyService {
	private final ModelMapper modelMapper;
	private final FreeBoardCommentReplyRepository freeBoardCommentReplyRepository;

	public PagingCursorDto<FreeBoardCommentReplyDto> get(long freeBoardCommentId, long cursor, int size) {
		final List<FreeBoardCommentReply> freeBoardCommentReplyList = freeBoardCommentReplyRepository.findAllByFreeBoardCommentIdOrderByIdDesc(freeBoardCommentId, cursor, size);
		if (CollectionUtils.isEmpty(freeBoardCommentReplyList)) {
			return PagingCursorDto.<FreeBoardCommentReplyDto>builder()
				.cursor(null)
				.isLast(true)
				.items(new ArrayList<>())
				.build();
		}

		final long nextCursor = freeBoardCommentReplyList.get(freeBoardCommentReplyList.size()-1).getId();
		final boolean existNextPage = freeBoardCommentReplyRepository.existsByFreeBoardCommentIdAndIdLessThan(freeBoardCommentId, nextCursor);

		return PagingCursorDto.<FreeBoardCommentReplyDto>builder()
			.cursor(nextCursor)
			.isLast(!existNextPage)
			.items(freeBoardCommentReplyList.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList()))
			.build();
	}

	private FreeBoardCommentReplyDto mapToDto(FreeBoardCommentReply freeBoardCommentReply) {
		final FreeBoardCommentReplyDto freeBoardCommentReplyDto = modelMapper.map(freeBoardCommentReply, FreeBoardCommentReplyDto.class);
		if (freeBoardCommentReplyDto.getIsDeleted()) {
			return FreeBoardCommentReplyDto.builder()
				.id(freeBoardCommentReplyDto.getId())
				.freeBoardCommentId(freeBoardCommentReplyDto.getFreeBoardCommentId())
				.contents("삭제된 답글입니다.")
				.writerId(0L)
				.writerNickname("")
				.isDeleted(true)
				.createdAt(null)
				.modifiedAt(null)
				.build();
		}
		return freeBoardCommentReplyDto;
	}

	public Long save(FreeBoardCommentReplyDto freeBoardCommentReplyDto, UserDto userDto) {
		try {
			if (userDto == null || userDto.getId() == null) {
				throw new NotLoginException();
			}

			if (freeBoardCommentReplyDto.getId() == null) {
				return insert(freeBoardCommentReplyDto, userDto);
			}

			return update(freeBoardCommentReplyDto, userDto);
		} catch (Exception e) {
			log.error("FreeBoardCommentReplyService.save(): {}", e.getMessage());
			throw e;
		}
	}

	private Long insert(FreeBoardCommentReplyDto freeBoardCommentReplyDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardCommentReply freeBoardCommentReply = new FreeBoardCommentReply();
		freeBoardCommentReply.setFreeBoardCommentId(freeBoardCommentReplyDto.getFreeBoardCommentId());
		freeBoardCommentReply.setContents(freeBoardCommentReplyDto.getContents());
		freeBoardCommentReply.setWriterId(userDto.getId());
		freeBoardCommentReply.setWriterNickname(userDto.getNickname());
		freeBoardCommentReply.setLikeCount(0);
		freeBoardCommentReply.setIsDeleted(false);

		return freeBoardCommentReplyRepository.save(freeBoardCommentReply).getId();
	}

	private Long update(FreeBoardCommentReplyDto freeBoardCommentReplyDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardCommentReply freeBoardCommentReply = freeBoardCommentReplyRepository.findById(freeBoardCommentReplyDto.getId()).orElse(null);

		if (freeBoardCommentReply == null) {
			throw new UnExpectedException("수정하려는 답글이 없습니다.(id=" + freeBoardCommentReplyDto.getId() + ")");
		} else if (!Objects.equals(freeBoardCommentReply.getWriterId(), userDto.getId())) {
			throw new PermissionDeniedException();
		}

		freeBoardCommentReply.setContents(freeBoardCommentReplyDto.getContents());

		return freeBoardCommentReplyRepository.save(freeBoardCommentReply).getId();
	}

	public void remove(long id, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardCommentReply freeBoardCommentReply = freeBoardCommentReplyRepository.findById(id).orElse(null);

		if (freeBoardCommentReply == null) {
			throw new UnExpectedException("삭제하려는 답글이 없습니다.(id=" + id + ")");
		} else if (!Objects.equals(freeBoardCommentReply.getWriterId(), userDto.getId())) {
			throw new PermissionDeniedException();
		}

		freeBoardCommentReply.setIsDeleted(true);

		freeBoardCommentReplyRepository.save(freeBoardCommentReply);
	}
}

package com.mallang.bobby.domain.freeboard.service;

import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentReplyDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardCommentReply;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardCommentReplyRepository;
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

	private static final Sort sortByIdDesc = Sort.by(Sort.Direction.DESC, "id");

	public PagingDto<FreeBoardCommentReplyDto> get(long freeBoardCommentId, int page, int size) {
		final Pageable pageable = PageRequest.of((page - 1), size, sortByIdDesc);
		final Page<FreeBoardCommentReply> freeBoardCommentReplyPage = freeBoardCommentReplyRepository.findAllByFreeBoardCommentId(freeBoardCommentId, pageable);

		return PagingDto.<FreeBoardCommentReplyDto>builder()
			.page(page)
			.isLast(page >= freeBoardCommentReplyPage.getTotalPages())
			.items(freeBoardCommentReplyPage.getContent().stream()
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

	public void save(FreeBoardCommentReplyDto freeBoardCommentReplyDto, UserDto userDto) {
		try {
			if (userDto == null || userDto.getId() == null) {
				throw new NotLoginException();
			}

			if (freeBoardCommentReplyDto.getId() == null) {
				insert(freeBoardCommentReplyDto, userDto);
				return;
			}

			update(freeBoardCommentReplyDto, userDto);
		} catch (Exception e) {
			log.error("FreeBoardCommentReplyService.save(): {}", e.getMessage());
			throw e;
		}
	}

	private void insert(FreeBoardCommentReplyDto freeBoardCommentReplyDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardCommentReply freeBoardCommentReply = new FreeBoardCommentReply();
		freeBoardCommentReply.setFreeBoardCommentId(freeBoardCommentReplyDto.getFreeBoardCommentId());
		freeBoardCommentReply.setContents(freeBoardCommentReplyDto.getContents());
		freeBoardCommentReply.setWriterId(userDto.getId());
		freeBoardCommentReply.setWriterNickname(userDto.getNickname());
		freeBoardCommentReply.setIsDeleted(false);

		freeBoardCommentReplyRepository.save(freeBoardCommentReply);
	}

	private void update(FreeBoardCommentReplyDto freeBoardCommentReplyDto, UserDto userDto) {
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

		freeBoardCommentReplyRepository.save(freeBoardCommentReply);
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

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
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardCommentRepository;
import com.mallang.bobby.dto.PagingDto;
import com.mallang.bobby.exception.NotLoginException;
import com.mallang.bobby.exception.PermissionDeniedException;
import com.mallang.bobby.exception.UnExpectedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardCommentService {
	private final ModelMapper modelMapper;
	private final FreeBoardCommentRepository freeBoardCommentRepository;

	private static final Sort sortByIdDesc = Sort.by(Sort.Direction.DESC, "id");

	public PagingDto get(long freeBoardId, int page, int size) {
		final Pageable pageable = PageRequest.of((page - 1), size, sortByIdDesc);
		final Page<FreeBoardComment> freeBoardCommentPage = freeBoardCommentRepository.findAllByFreeBoardIdWithReply(freeBoardId, pageable);

		return PagingDto.builder()
			.page(page)
			.isLast(page >= freeBoardCommentPage.getTotalPages())
			.items(freeBoardCommentPage.getContent().stream()
				.map(this::mapToDto)
				.collect(Collectors.toList()))
			.build();
	}

	private FreeBoardCommentDto mapToDto(FreeBoardComment freeBoardComment) {
		final FreeBoardCommentDto freeBoardCommentDto = modelMapper.map(freeBoardComment, FreeBoardCommentDto.class);
		if (freeBoardCommentDto.getIsDeleted()) {
			return FreeBoardCommentDto.builder()
				.id(freeBoardCommentDto.getId())
				.freeBoardId(freeBoardCommentDto.getFreeBoardId())
				.contents("삭제된 댓글입니다.")
				.writerId(0L)
				.writerNickname("")
				.isDeleted(true)
				.createdAt(null)
				.modifiedAt(null)
				.build();
		}
		return freeBoardCommentDto;
	}

	public void save(FreeBoardCommentDto freeBoardCommentDto, UserDto userDto) {
		try {
			if (userDto == null || userDto.getId() == null) {
				throw new NotLoginException();
			}

			if (freeBoardCommentDto.getId() == null) {
				insert(freeBoardCommentDto, userDto);
				return;
			}

			update(freeBoardCommentDto, userDto);
		} catch (Exception e) {
			log.error("FreeBoardCommentService.save(): {}", e.getMessage());
			throw e;
		}
	}

	private void insert(FreeBoardCommentDto freeBoardCommentDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardComment freeBoardComment = new FreeBoardComment();
		freeBoardComment.setFreeBoardId(freeBoardCommentDto.getFreeBoardId());
		freeBoardComment.setContents(freeBoardCommentDto.getContents());
		freeBoardComment.setWriterId(userDto.getId());
		freeBoardComment.setWriterNickname(userDto.getNickname());
		freeBoardComment.setIsDeleted(false);

		freeBoardCommentRepository.save(freeBoardComment);
	}

	private void update(FreeBoardCommentDto freeBoardCommentDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(freeBoardCommentDto.getId()).orElse(null);

		if (freeBoardComment == null) {
			throw new UnExpectedException("수정하려는 댓글이 없습니다.(id=" + freeBoardCommentDto.getId() + ")");
		} else if (!Objects.equals(freeBoardComment.getWriterId(), userDto.getId())) {
			throw new PermissionDeniedException();
		}

		freeBoardComment.setContents(freeBoardCommentDto.getContents());

		freeBoardCommentRepository.save(freeBoardComment);
	}

	public void remove(long id, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(id).orElse(null);

		if (freeBoardComment == null) {
			throw new UnExpectedException("삭제하려는 댓글이 없습니다.(id=" + id + ")");
		} else if (!Objects.equals(freeBoardComment.getWriterId(), userDto.getId())) {
			throw new PermissionDeniedException();
		}

		freeBoardComment.setIsDeleted(true);

		freeBoardCommentRepository.save(freeBoardComment);
	}
}

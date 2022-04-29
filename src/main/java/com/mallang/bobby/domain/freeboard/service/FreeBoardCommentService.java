package com.mallang.bobby.domain.freeboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoard;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardCommentRepository;
import com.mallang.bobby.dto.PagingCursorDto;
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
	private final FreeBoardReplyService freeBoardReplyService;
	private final FreeBoardCommentLikeService freeBoardCommentLikeService;

	private static final int replyCursor = 0;
	private static final int replySize = 20;

	public PagingCursorDto<FreeBoardCommentDto> get(long freeBoardId, long cursor, int size) {
		final List<FreeBoardComment> freeBoardCommentList = freeBoardCommentRepository.findAllByFreeBoardIdOrderByIdDesc(freeBoardId, cursor, size);
		if (CollectionUtils.isEmpty(freeBoardCommentList)) {
			return PagingCursorDto.<FreeBoardCommentDto>builder()
				.cursor(null)
				.isLast(true)
				.items(new ArrayList<>())
				.build();
		}

		final long nextCursor = freeBoardCommentList.get(freeBoardCommentList.size()-1).getId();
		final boolean existNextPage = freeBoardCommentRepository.existsByFreeBoardIdAndIdLessThan(freeBoardId, nextCursor);

		return PagingCursorDto.<FreeBoardCommentDto>builder()
			.cursor(nextCursor)
			.isLast(!existNextPage)
			.items(freeBoardCommentList.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList()))
			.build();
	}

	public FreeBoardCommentDto get(long freeBoardId, long id) {
		final FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(id).orElse(null);
		if (freeBoardComment == null) {
			return null;
		}

		final boolean isBadRequest = !freeBoardComment.getFreeBoardId().equals(freeBoardId);
		final boolean isDeleted = freeBoardComment.getIsDeleted();
		if (isBadRequest || isDeleted) {
			return null;
		}

		final FreeBoardCommentDto freeBoardCommentDto = modelMapper.map(freeBoardComment, FreeBoardCommentDto.class);
		freeBoardCommentDto.setCommentReplyPage(
			freeBoardReplyService.get(freeBoardCommentDto.getId(), replyCursor, replySize));

		return freeBoardCommentDto;
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

	public Long save(FreeBoardCommentDto freeBoardCommentDto, UserDto userDto) {
		try {
			if (userDto == null || userDto.getId() == null) {
				throw new NotLoginException();
			}

			if (freeBoardCommentDto.getId() == null) {
				return insert(freeBoardCommentDto, userDto);
			}

			return update(freeBoardCommentDto, userDto);
		} catch (Exception e) {
			log.error("FreeBoardCommentService.save(): {}", e.getMessage());
			throw e;
		}
	}

	private Long insert(FreeBoardCommentDto freeBoardCommentDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoardComment freeBoardComment = new FreeBoardComment();
		freeBoardComment.setFreeBoardId(freeBoardCommentDto.getFreeBoardId());
		freeBoardComment.setContents(freeBoardCommentDto.getContents());
		freeBoardComment.setWriterId(userDto.getId());
		freeBoardComment.setWriterNickname(userDto.getNickname());
		freeBoardComment.setLikeCount(0);
		freeBoardComment.setCommentReplyCount(0);
		freeBoardComment.setIsDeleted(false);

		return freeBoardCommentRepository.save(freeBoardComment).getId();
	}

	private Long update(FreeBoardCommentDto freeBoardCommentDto, UserDto userDto) {
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

		return freeBoardCommentRepository.save(freeBoardComment).getId();
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

	public Integer countCommentCountByFreeBoardId(Long freeBoardId) {
		return freeBoardCommentRepository.countAllByFreeBoardIdAndIsDeletedFalse(freeBoardId);
	}

	private boolean isLike(long freeBoardId, UserDto userDto) {
		if (userDto == null) {
			return false;
		}

		return freeBoardCommentLikeService.isLike(freeBoardId, userDto.getId());
	}

	public void like(long freeBoardId, UserDto userDto) {
		final FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(freeBoardId).orElse(null);
		if (freeBoardComment == null || userDto == null) {
			return;
		}

		freeBoardComment.setLikeCount(freeBoardComment.getLikeCount() + 1);

		freeBoardCommentRepository.save(freeBoardComment);
		freeBoardCommentLikeService.like(freeBoardId, userDto.getId());
	}

	public void unLike(long freeBoardId, UserDto userDto) {
		final FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(freeBoardId).orElse(null);
		if (freeBoardComment == null || userDto == null) {
			return;
		}

		if (freeBoardComment.getLikeCount() > 0) {
			freeBoardComment.setLikeCount(freeBoardComment.getLikeCount() - 1);
		}

		freeBoardCommentRepository.save(freeBoardComment);
		freeBoardCommentLikeService.unLike(freeBoardId, userDto.getId());
	}

	public void updateReplyCount(Long id, Integer replyCount) {
		final FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(id).orElse(null);
		if (freeBoardComment == null) {
			log.error("답글 카운트를 업데이트할 게시글을 찾지 못했습니다.");
			return;
		}
		freeBoardComment.setCommentReplyCount(Optional.ofNullable(replyCount).orElse(0));
		freeBoardCommentRepository.save(freeBoardComment);
	}
}

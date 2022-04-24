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
import com.mallang.bobby.domain.freeboard.dto.FreeBoardDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoard;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardRepository;
import com.mallang.bobby.dto.PagingCursorDto;
import com.mallang.bobby.exception.NotLoginException;
import com.mallang.bobby.exception.PermissionDeniedException;
import com.mallang.bobby.exception.UnExpectedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardService {
	private final ModelMapper modelMapper;
	private final FreeBoardRepository freeBoardRepository;
	private final FreeBoardLikeService freeBoardLikeService;

	public PagingCursorDto<FreeBoardDto> get(long cursor, int size) {
		final List<FreeBoard> freeBoardList = freeBoardRepository.findAllOrderByIdDesc(cursor, size);
		if (CollectionUtils.isEmpty(freeBoardList)) {
			return PagingCursorDto.<FreeBoardDto>builder()
				.cursor(null)
				.isLast(true)
				.items(new ArrayList<>())
				.build();
		}

		final long nextCursor = freeBoardList.get(freeBoardList.size()-1).getId();
		final boolean existNextPage = freeBoardRepository.existsByIsDeletedFalseAndIdLessThan(nextCursor);

		return PagingCursorDto.<FreeBoardDto>builder()
			.cursor(nextCursor)
			.isLast(!existNextPage)
			.items(freeBoardList.stream()
				.map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class))
				.collect(Collectors.toList()))
			.build();
	}

	public FreeBoardDto get(long id, UserDto userDto) {
		final FreeBoard freeBoard = freeBoardRepository.findByIdAndIsDeleted(id, false).orElse(null);

		if (freeBoard == null) {
			return null;
		}

		final FreeBoardDto freeBoardDto = modelMapper.map(freeBoard, FreeBoardDto.class);

		final boolean isMine = (userDto != null) && (freeBoardDto.getWriterId().equals(userDto.getId()));
		freeBoardDto.setIsMine(isMine);

		final boolean isLike = isLike(freeBoardDto.getId(), userDto);
		freeBoardDto.setIsLike(isLike);

		return freeBoardDto;
	}

	public void save(FreeBoardDto freeBoardDto, UserDto userDto) {
		try {
			if (userDto == null || userDto.getId() == null) {
				throw new NotLoginException();
			}

			if (freeBoardDto.getId() == null) {
				insert(freeBoardDto, userDto);
				return;
			}

			update(freeBoardDto, userDto);
		} catch (Exception e) {
			log.error("FreeBoardService.save(): {}", e.getMessage());
			throw e;
		}
	}

	public void remove(long id, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoard freeBoard = freeBoardRepository.findById(id).orElse(null);

		if (freeBoard == null) {
			throw new UnExpectedException("삭제하려는 게시글이 없습니다.(id=" + id + ")");
		} else if (!Objects.equals(freeBoard.getWriterId(), userDto.getId())) {
			throw new PermissionDeniedException();
		}

		freeBoard.setIsDeleted(true);

		freeBoardRepository.save(freeBoard);
	}

	private void insert(FreeBoardDto freeBoardDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoard freeBoard = new FreeBoard();
		freeBoard.setTitle(freeBoardDto.getTitle());
		freeBoard.setContents(freeBoardDto.getContents());
		freeBoard.setCommentCount(0);
		freeBoard.setLikeCount(0);
		freeBoard.setWriterId(userDto.getId());
		freeBoard.setWriterNickname(userDto.getNickname());
		freeBoard.setIsDeleted(false);

		freeBoardRepository.save(freeBoard);
	}

	private void update(FreeBoardDto freeBoardDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoard freeBoard = freeBoardRepository.findById(freeBoardDto.getId()).orElse(null);

		if (freeBoard == null) {
			throw new UnExpectedException("수정하려는 게시글이 없습니다.(id=" + freeBoardDto.getId() + ")");
		} else if (!Objects.equals(freeBoard.getWriterId(), userDto.getId())) {
			throw new PermissionDeniedException();
		}

		freeBoard.setTitle(freeBoardDto.getTitle());
		freeBoard.setContents(freeBoardDto.getContents());

		freeBoardRepository.save(freeBoard);
	}

	private boolean isLike(long freeBoardId, UserDto userDto) {
		if (userDto == null) {
			return false;
		}

		return freeBoardLikeService.isLike(freeBoardId, userDto.getId());
	}

	public void like(long freeBoardId, UserDto userDto) {
		final FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId).orElse(null);
		if (freeBoard == null || userDto == null) {
			return;
		}

		freeBoard.setLikeCount(freeBoard.getLikeCount() + 1);

		freeBoardRepository.save(freeBoard);
		freeBoardLikeService.like(freeBoardId, userDto.getId());
	}

	public void unLike(long freeBoardId, UserDto userDto) {
		final FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId).orElse(null);
		if (freeBoard == null || userDto == null) {
			return;
		}

		if (freeBoard.getLikeCount() > 0) {
			freeBoard.setLikeCount(freeBoard.getLikeCount() - 1);
		}

		freeBoardRepository.save(freeBoard);
		freeBoardLikeService.unLike(freeBoardId, userDto.getId());
	}

	public void updateCommentCount(Long id, Integer commentCount) {
		final FreeBoard freeBoard = freeBoardRepository.findById(id).orElse(null);
		if (freeBoard == null) {
			log.error("댓글 카운트를 업데이트할 게시글을 찾지 못했습니다.");
			return;
		}
		freeBoard.setCommentCount(Optional.ofNullable(commentCount).orElse(0));
		freeBoardRepository.save(freeBoard);
	}
}

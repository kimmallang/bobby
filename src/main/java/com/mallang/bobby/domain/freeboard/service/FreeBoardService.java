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
import com.mallang.bobby.domain.freeboard.dto.FreeBoardDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardResponse;
import com.mallang.bobby.domain.freeboard.entity.FreeBoard;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardRepository;
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

	private static final Sort sortByIdDesc = Sort.by(Sort.Direction.DESC, "id");

	public FreeBoardResponse get(int page, int size) {
		final Pageable pageable = PageRequest.of((page - 1), size, sortByIdDesc);
		final Page<FreeBoard> freeBoardPage = freeBoardRepository.findAllByDeleteYn(false, pageable);

		return FreeBoardResponse.builder()
			.page(page)
			.isLast(page >= freeBoardPage.getTotalPages())
			.items(freeBoardPage.getContent().stream()
				.map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class))
				.collect(Collectors.toList()))
			.build();
	}

	public FreeBoardDto get(long id, UserDto userDto) {
		final FreeBoard freeBoard = freeBoardRepository.findByIdAndDeleteYn(id, false).orElse(new FreeBoard());

		if (freeBoard == null) {
			return null;
		}

		final FreeBoardDto freeBoardDto = modelMapper.map(freeBoard, FreeBoardDto.class);

		final boolean isMine = (userDto != null) && (freeBoardDto.getWriterId().equals(userDto.getId()));
		freeBoardDto.setIsMine(isMine);

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
			log.error("`FreeBoardService.save(): {}", e.getMessage());
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

		freeBoard.setDeleteYn(true);

		freeBoardRepository.save(freeBoard);
	}

	private void insert(FreeBoardDto freeBoardDto, UserDto userDto) {
		if (userDto == null) {
			throw new NotLoginException();
		}

		final FreeBoard freeBoard = new FreeBoard();
		freeBoard.setTitle(freeBoardDto.getTitle());
		freeBoard.setContents(freeBoardDto.getContents());
		freeBoard.setCommentsCount(0);
		freeBoard.setLikeCount(0);
		freeBoard.setWriterId(userDto.getId());
		freeBoard.setWriterNickname(userDto.getNickname());
		freeBoard.setDeleteYn(false);

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
}

package com.mallang.bobby.domain.freeboard.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardService {
	private final ModelMapper modelMapper;
	private final FreeBoardRepository freeBoardRepository;

	private static final int pagingSize = 20;
	private static final Sort sortByIdDesc = Sort.by(Sort.Direction.DESC, "id");

	public FreeBoardResponse get(int page) {
		final Pageable pageable = PageRequest.of((page - 1), pagingSize, sortByIdDesc);
		final Page<FreeBoard> freeBoardPage = freeBoardRepository.findAll(pageable);

		return FreeBoardResponse.builder()
			.page(page)
			.isLast(page >= freeBoardPage.getTotalPages())
			.items(freeBoardPage.getContent().stream()
				.map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class))
				.collect(Collectors.toList()))
			.build();
	}

	public FreeBoardDto get(long id) {
		return modelMapper.map(freeBoardRepository.findById(id).orElse(new FreeBoard()), FreeBoardDto.class);
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

	private void insert(FreeBoardDto freeBoardDto, UserDto userDto) {
		final FreeBoard freeBoard = new FreeBoard();
		freeBoard.setTitle(freeBoardDto.getTitle());
		freeBoard.setContents(freeBoardDto.getContents());
		freeBoard.setCommentsCount(0);
		freeBoard.setLikeCount(0);
		freeBoard.setWriterId(userDto.getId());
		freeBoard.setWriterNickname(userDto.getNickname());

		freeBoardRepository.save(freeBoard);
	}

	private void update(FreeBoardDto freeBoardDto, UserDto userDto) {
		final FreeBoard freeBoard = freeBoardRepository.findById(freeBoardDto.getId()).orElse(new FreeBoard());
		freeBoard.setTitle(freeBoardDto.getTitle());
		freeBoard.setContents(freeBoardDto.getContents());

		if (freeBoard.getWriterId() != userDto.getId()) {
			throw new NotLoginException();
		}

		freeBoardRepository.save(freeBoard);
	}
}

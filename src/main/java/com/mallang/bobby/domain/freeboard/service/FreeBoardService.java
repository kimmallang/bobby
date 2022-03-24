package com.mallang.bobby.domain.freeboard.service;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.freeboard.dto.FreeBoardDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardResponse;
import com.mallang.bobby.domain.freeboard.entity.FreeBoard;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardRepository;
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

	public void save(FreeBoardDto freeBoardDto) {
		try {
			freeBoardRepository.save(modelMapper.map(freeBoardDto, FreeBoard.class));
		} catch (Exception e) {
			log.error("`FreeBoardService.save(): {}", e.getMessage());
			throw e;
		}
	}
}

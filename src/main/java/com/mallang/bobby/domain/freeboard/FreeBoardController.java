package com.mallang.bobby.domain.freeboard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.domain.freeboard.dto.FreeBoardDto;
import com.mallang.bobby.domain.freeboard.service.FreeBoardService;
import com.mallang.bobby.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mallang/")
@RequiredArgsConstructor
public class FreeBoardController {
	private final FreeBoardService freeBoardService;

	@GetMapping("/free-board")
	public ResponseDto get(int page) {
		return ResponseDto.builder()
			.data(freeBoardService.get(page))
			.build();
	}

	@PostMapping("/free-board")
	public ResponseDto save(FreeBoardDto freeBoardDto) {
		freeBoardService.save(freeBoardDto);

		return ResponseDto.builder().build();
	}
}

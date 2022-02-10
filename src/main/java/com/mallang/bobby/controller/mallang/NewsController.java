package com.mallang.bobby.controller.mallang;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.domain.dto.ResponseDto;
import com.mallang.bobby.external.naver.NaverNewsApi;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mallang/")
@RequiredArgsConstructor
public class NewsController {
	private final NaverNewsApi naverNewsApi;

	@GetMapping("/news")
	public ResponseDto getHello() {
		return ResponseDto.builder()
			.data(naverNewsApi.getNews())
			.build();
	}
}

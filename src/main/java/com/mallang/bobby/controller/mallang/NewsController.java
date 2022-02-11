package com.mallang.bobby.controller.mallang;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.domain.dto.ResponseDto;
import com.mallang.bobby.external.naver.NaverApi;
import com.mallang.bobby.external.naver.request.news.NewsRequest;
import com.mallang.bobby.external.naver.request.news.NewsRequestSort;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mallang/")
@RequiredArgsConstructor
public class NewsController {
	private final NaverApi naverApi;

	@GetMapping("/news")
	public ResponseDto getNews() {
		final NewsRequest newsRequest = new NewsRequest("IT",100,1, NewsRequestSort.sim);

		return ResponseDto.builder()
			.data(naverApi.getNews(newsRequest))
			.build();
	}
}

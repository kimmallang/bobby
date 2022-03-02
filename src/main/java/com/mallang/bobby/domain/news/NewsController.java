package com.mallang.bobby.domain.news;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.domain.news.service.NewsService;
import com.mallang.bobby.domain.news.dto.NewsRequestQuery;
import com.mallang.bobby.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mallang/")
@RequiredArgsConstructor
public class NewsController {
	private final NewsService newsService;

	@GetMapping("/news")
	public ResponseDto get(NewsRequestQuery query, int page) {
		return ResponseDto.builder()
			.data(newsService.get(query, page))
			.build();
	}
}

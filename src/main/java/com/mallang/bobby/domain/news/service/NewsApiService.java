package com.mallang.bobby.domain.news.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.news.vo.NewsRequestQuery;
import com.mallang.bobby.domain.news.vo.NewsVo;
import com.mallang.bobby.external.naver.NaverApi;
import com.mallang.bobby.external.naver.request.news.NewsRequest;
import com.mallang.bobby.external.naver.request.news.NewsRequestSort;
import com.mallang.bobby.external.naver.response.news.NewsResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsApiService {
	private final NaverApi naverApi;

	public List<NewsVo> get(NewsRequestQuery newsRequestQuery) {
		NewsResponse newsResponse = naverApi.getNews(NewsRequest.builder()
			.query(newsRequestQuery.getQuery())
			.sort(NewsRequestSort.sim)
			.display(100)
			.start(1)
			.build());

		return newsResponse.getItems()
			.stream()
			.map(item -> NewsVo.builder()
				.query(newsRequestQuery)
				.title(item.getTitle())
				.originalLink(item.getOriginallink())
				.naverLink(item.getLink())
				.description(item.getDescription())
				.build())
			.collect(Collectors.toList());
	}
}

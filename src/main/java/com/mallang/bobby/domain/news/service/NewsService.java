package com.mallang.bobby.domain.news.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mallang.bobby.domain.news.dto.NewsResponse;
import com.mallang.bobby.domain.news.repository.NewsRepository;
import com.mallang.bobby.domain.news.vo.NewsRequestQuery;
import com.mallang.bobby.domain.news.vo.NewsVo;
import com.mallang.bobby.domain.news.entity.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {
	private final ModelMapper modelMapper;
	private final NewsApiService newsApiService;
	private final NewsRepository newsRepository;

	private static final int pagingSize = 20;
	private static final Sort sortByIdDesc = Sort.by(Sort.Direction.DESC, "id");

	@Transactional
	public List<NewsVo> refreshFromApi(NewsRequestQuery newsRequestQuery) {
		final List<NewsVo> newsVoList = newsApiService.get(newsRequestQuery);
		final List<News> newsList = newsVoList.stream()
			.map(newsVo -> modelMapper.map(newsVo, News.class))
			.collect(Collectors.toList());

		newsRepository.deleteAllByQuery(newsRequestQuery.getQuery());

		return newsRepository.saveAll(newsList).stream()
			.map(news -> modelMapper.map(news, NewsVo.class))
			.collect(Collectors.toList());
	}

	public NewsResponse get(NewsRequestQuery newsRequestQuery, int page) {
		final Pageable pageable = PageRequest.of((page - 1), pagingSize, sortByIdDesc);
		final Page<News> newsPage = newsRepository.findAllByQuery(newsRequestQuery.getQuery(), pageable);

		return NewsResponse.builder()
			.page(page)
			.isLast(page >= newsPage.getTotalPages())
			.items(newsPage.getContent().stream()
				.map(news -> modelMapper.map(news, NewsVo.class))
				.collect(Collectors.toList()))
			.build();
	}
}

package com.mallang.bobby.domain.news.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mallang.bobby.domain.news.dto.NewsResponse;
import com.mallang.bobby.domain.news.repository.NewsRepository;
import com.mallang.bobby.domain.news.dto.NewsRequestQuery;
import com.mallang.bobby.domain.news.dto.NewsDto;
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

	@CacheEvict(value = "news", allEntries = true)
	@Transactional
	public List<NewsDto> refreshFromApi(NewsRequestQuery newsRequestQuery) {
		final List<NewsDto> newsDtoList = newsApiService.get(newsRequestQuery);
		final List<News> newsList = newsDtoList.stream()
			.map(newsDto -> modelMapper.map(newsDto, News.class))
			.collect(Collectors.toList());

		newsRepository.deleteAllByQuery(newsRequestQuery.getQuery());

		return newsRepository.saveAll(newsList).stream()
			.map(news -> modelMapper.map(news, NewsDto.class))
			.collect(Collectors.toList());
	}

	@Cacheable(value = "news")
	public NewsResponse get(NewsRequestQuery newsRequestQuery, int page) {
		final Pageable pageable = PageRequest.of((page - 1), pagingSize, sortByIdDesc);
		final Page<News> newsPage = newsRepository.findAllByQuery(newsRequestQuery.getQuery(), pageable);

		return NewsResponse.builder()
			.page(page)
			.isLast(page >= newsPage.getTotalPages())
			.items(newsPage.getContent().stream()
				.map(news -> modelMapper.map(news, NewsDto.class))
				.collect(Collectors.toList()))
			.build();
	}
}

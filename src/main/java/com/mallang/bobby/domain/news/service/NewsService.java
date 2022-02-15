package com.mallang.bobby.domain.news.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public List<NewsVo> saveFromApi(NewsRequestQuery newsRequestQuery) {
		final List<NewsVo> newsVoList = newsApiService.get(newsRequestQuery);
		final List<News> newsList = newsVoList.stream()
			.map(newsVo -> modelMapper.map(newsVo, News.class))
			.collect(Collectors.toList());

		newsRepository.deleteAllByQuery(newsRequestQuery.getQuery());

		return newsRepository.saveAll(newsList).stream()
			.map(news -> modelMapper.map(news, NewsVo.class))
			.collect(Collectors.toList());
	}
}

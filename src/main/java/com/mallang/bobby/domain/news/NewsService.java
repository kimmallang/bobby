package com.mallang.bobby.domain.news;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {
	private final ModelMapper modelMapper;
	private final NewsRepository newsRepository;

	public NewsVo save(NewsVo newsVo) {
		final News news = modelMapper.map(newsVo, News.class);
		final News savedNews = newsRepository.save(news);

		return modelMapper.map(savedNews, NewsVo.class);
	}

	public List<NewsVo> save(List<NewsVo> newsVoList) {
		final List<News> newsList = newsVoList.stream()
			.map(newsVo -> modelMapper.map(newsVo, News.class))
			.collect(Collectors.toList());

		return newsRepository.saveAll(newsList).stream()
			.map(news -> modelMapper.map(news, NewsVo.class))
			.collect(Collectors.toList());
	}

	public NewsVo find(Long id) {
		final News news = newsRepository.findById(id).orElse(new News());

		return modelMapper.map(news, NewsVo.class);
	}
}

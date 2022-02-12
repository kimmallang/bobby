package com.mallang.bobby.domain.news;

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
		try {
			final News news = modelMapper.map(newsVo, News.class);
			final News savedNews = newsRepository.save(news);
			return modelMapper.map(savedNews, NewsVo.class);
		} catch (Exception e) {
			log.error(String.valueOf(e));
			throw e;
		}
	}

	public NewsVo find(Long id) {
		final News news = newsRepository.findById(id).orElse(new News());
		return modelMapper.map(news, NewsVo.class);
	}
}

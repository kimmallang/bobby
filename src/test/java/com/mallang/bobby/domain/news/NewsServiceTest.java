package com.mallang.bobby.domain.news;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class NewsServiceTest {
	private NewsService newsService;

	@Autowired
	private NewsRepository newsRepository;

	@BeforeEach
	void init() {
		ModelMapper modelMapper = new ModelMapper();
		newsService = new NewsService(modelMapper, newsRepository);
	}

	@Test
	void save() {
		final NewsVo news = NewsVo.builder()
			.title("title")
			.description("description")
			.originalLink("originalLink")
			.naverLink("naverLink")
			.query("query")
			.build();

		Long savedId = newsService.save(news).getId();
		NewsVo savedNews = newsService.find(savedId);
		news.setId(savedNews.getId());

		assertEquals(news, savedNews, "뉴스 단일 저장 실패");
	}

	@Test
	void saveAll() {
		final List<NewsVo> newsList = Arrays.asList("test1", "test2", "test3", "test4", "test5")
			.stream()
			.map(title -> NewsVo.builder()
				.title("title")
				.description("description")
				.originalLink("originalLink")
				.naverLink("naverLink")
				.query("query")
				.build())
			.collect(Collectors.toList());

		List<NewsVo> savedNewsList = newsService.save(newsList);

		assertEquals(newsList.size(), savedNewsList.size(), "뉴스 다중 저장 실패");
	}

	@Test
	void find() {
		NewsVo news = newsService.find(8L);
		assertEquals("test", news.getTitle(), "뉴스 단일 조회 실패");
	}
}

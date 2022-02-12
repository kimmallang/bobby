package com.mallang.bobby.domain.news;

import static org.junit.jupiter.api.Assertions.*;

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

		assertEquals(news, savedNews, "news is not equals savedNews");
	}

	@Test
	void find() {
		NewsVo news = newsService.find(8L);
		assertEquals("test", news.getTitle(), "조회 실패");
	}
}

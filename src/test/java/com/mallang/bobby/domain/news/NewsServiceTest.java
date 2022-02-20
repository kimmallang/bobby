package com.mallang.bobby.domain.news;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.mallang.bobby.domain.news.repository.NewsRepository;
import com.mallang.bobby.domain.news.service.NewsApiService;
import com.mallang.bobby.domain.news.service.NewsService;
import com.mallang.bobby.domain.news.dto.NewsRequestQuery;
import com.mallang.bobby.domain.news.dto.NewsDto;
import com.mallang.bobby.external.naver.NaverApi;

@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class NewsServiceTest {
	private ModelMapper modelMapper;
	private RestTemplate restTemplate;
	private NaverApi naverApi;
	private NewsApiService newsApiService;
	private NewsService newsService;

	@Autowired
	private NewsRepository newsRepository;

	@Value("${api.naver.client-id}")
	private String clientId;

	@Value("${api.naver.client-secret}")
	private String clientSecret;

	@Value("${api.naver.url.news}")
	private String naverNewsApiUrl;

	@BeforeEach
	void init() {
		initModelMapper();
		initRestTemplate();
		initNaverApi();
		initNewsApiService();
		initNewsService();
	}

	void initModelMapper() {
		modelMapper = new ModelMapper();
	}

	void initRestTemplate() {
		restTemplate = new RestTemplate();
	}

	void initNaverApi() {
		naverApi = new NaverApi(restTemplate);
		naverApi.setClientId(clientId);
		naverApi.setClientSecret(clientSecret);
		naverApi.setNaverNewsApiUrl(naverNewsApiUrl);
	}

	void initNewsApiService() {
		newsApiService = new NewsApiService(naverApi);
	}

	void initNewsService() {
		newsService = new NewsService(modelMapper, newsApiService, newsRepository);
	}

	@Test
	void saveFromApi() {
		List<NewsDto> savedNewsList = newsService.refreshFromApi(NewsRequestQuery.IT);

		assertTrue(savedNewsList.size() > 0, "뉴스 데이터 최신화 실패");
	}
}

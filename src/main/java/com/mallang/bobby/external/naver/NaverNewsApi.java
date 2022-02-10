package com.mallang.bobby.external.naver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mallang.bobby.external.naver.response.NewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverNewsApi {
	private final RestTemplate restTemplate;

	public NewsResponse getNews() {
		try {
			final String url = UriComponentsBuilder.fromHttpUrl("https://openapi.naver.com/v1/search/news.json")
				.queryParam("query", "개발자")
				.queryParam("display", "20")
				.queryParam("start", "1")
				.queryParam("sort", "sim")
				.encode()
				.toUriString();

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("X-Naver-Client-Id","4phPhh0_MG0qX51vklfy");
			httpHeaders.add("X-Naver-Client-Secret","Q4A3CPGjq1");

			return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), NewsResponse.class).getBody();
		} catch (Exception e) {
			log.error("NaverNewsApi.getNews() {}", e.getMessage());
			throw e;
		}
	}
}

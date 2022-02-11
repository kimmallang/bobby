package com.mallang.bobby.external.naver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mallang.bobby.external.naver.request.news.NewsRequest;
import com.mallang.bobby.external.naver.response.news.NewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverApi {
	private final RestTemplate restTemplate;

	@Value("${api.naver.client-id}")
	private String clientId;

	@Value("${api.naver.client-secret}")
	private String clientSecret;

	@Value("${api.naver.url.news}")
	private String naverNewsApiUrl;

	private HttpEntity getRequestEntity() {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-Naver-Client-Id", clientId);
		httpHeaders.add("X-Naver-Client-Secret", clientSecret);

		return new HttpEntity<>(httpHeaders);
	}

	public NewsResponse getNews(NewsRequest newsRequest) {
		try {
			final String url = UriComponentsBuilder.fromHttpUrl(naverNewsApiUrl)
				.queryParam("query", newsRequest.getQuery())
				.queryParam("display", newsRequest.getDisplay())
				.queryParam("start", newsRequest.getStart())
				.queryParam("sort", newsRequest.getSort())
				.encode()
				.toUriString();

			return restTemplate.exchange(url, HttpMethod.GET, getRequestEntity(), NewsResponse.class).getBody();
		} catch (Exception e) {
			log.error("NaverNewsApi.getNews() {}", e.getMessage());
			throw e;
		}
	}
}

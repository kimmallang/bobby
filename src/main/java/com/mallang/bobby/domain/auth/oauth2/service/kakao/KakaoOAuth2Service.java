package com.mallang.bobby.domain.auth.oauth2.service.kakao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mallang.bobby.domain.auth.oauth2.dto.AccessTokenResponse;
import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.bobby.domain.auth.oauth2.dto.kakao.KakaoUserResponse;
import com.mallang.bobby.domain.auth.oauth2.service.OAuth2Service;
import com.mallang.bobby.domain.auth.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOAuth2Service implements OAuth2Service {
	private final RestTemplate restTemplate;

	@Value("${oauth2.kakao.client-id}")
	private String clientId;

	@Value("${oauth2.kakao.client-secret}")
	private String clientSecret;

	@Value("${oauth2.kakao.redirect-uri}")
	private String redirectUri;

	@Value("${oauth2.kakao.authorization-uri}")
	private String authorizationUri;

	@Value("${oauth2.kakao.token-uri}")
	private String tokenUri;

	@Value("${oauth2.kakao.user-info-uri}")
	private String userInfoUri;

	@Override
	public String getAuthorizeUrl() {
		return UriComponentsBuilder.fromHttpUrl(authorizationUri)
			.queryParam("response_type", "code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("client_id", clientId)
			.encode()
			.toUriString();
	}

	@Override
	public String getAccessToken(String code) {
		try {
			final MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>(){{
				add("grant_type", "authorization_code");
				add("client_id", clientId);
				add("client_secret", clientSecret);
				add("redirect_uri", redirectUri);
				add("code", code);
			}};
			return restTemplate.exchange(tokenUri, HttpMethod.POST, getRequestEntity(data), AccessTokenResponse.class).getBody().getAccessToken();
		} catch (Exception e) {
			log.error("KakaoOAuth2Service.getAccessToken() {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public UserDto getUser(String accessToken) {
		try {
			final KakaoUserResponse kakaoUserResponse = restTemplate.exchange(userInfoUri, HttpMethod.POST, getRequestEntity(accessToken), KakaoUserResponse.class).getBody();

			return UserDto.builder()
				.userId(kakaoUserResponse.getId().toString())
				.authorizedBy(OAuth2Provider.kakao)
				.nickname(kakaoUserResponse.getKakaoAccount().getProfile().getNickname())
				.profileImageUrl(kakaoUserResponse.getKakaoAccount().getProfile().getProfileImageUrl())
				.profileThumbnailUrl(kakaoUserResponse.getKakaoAccount().getProfile().getThumbnailImageUrl())
				.build();
		} catch (Exception e) {
			log.error("KakaoOAuth2Service.getUser() {}", e.getMessage());
			throw e;
		}
	}

	private HttpEntity getRequestEntity(Map data) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
		return new HttpEntity<>(data, httpHeaders);
	}

	private HttpEntity getRequestEntity(String accessToken) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
		httpHeaders.add("Authorization", "Bearer " + accessToken);
		return new HttpEntity<>(httpHeaders);
	}
}

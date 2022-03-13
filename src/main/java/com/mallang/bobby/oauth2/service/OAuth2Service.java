package com.mallang.bobby.oauth2.service;

import org.springframework.stereotype.Service;

import com.mallang.bobby.oauth2.dto.AccessTokenResponse;
import com.mallang.bobby.oauth2.dto.OAuth2Provider;
import com.mallang.bobby.oauth2.exception.NotSupportProviderException;
import com.mallang.bobby.oauth2.service.kakao.KakaoOAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {
	private final KakaoOAuth2Service kakaoOAuth2Service;

	public String getAuthorizeUrl(OAuth2Provider oAuth2Provider) {
		if (OAuth2Provider.kakao.equals(oAuth2Provider)) {
			return kakaoOAuth2Service.getKakaoAuthorizeUrl();
		}

		log.error("OAuth2Service.getAuthorizeUrl({}) error", oAuth2Provider);
		throw new NotSupportProviderException(oAuth2Provider.name());
	}

	public String getAccessToken(OAuth2Provider oAuth2Provider, String code) {
		if (OAuth2Provider.kakao.equals(oAuth2Provider)) {
			final AccessTokenResponse accessTokenResponse = kakaoOAuth2Service.getAccessToken(code);
			return accessTokenResponse.getAccessToken();
		}

		log.error("OAuth2Service.getAccessToken({}, {}) error", oAuth2Provider, code);
		throw new NotSupportProviderException(oAuth2Provider.name());
	}
}

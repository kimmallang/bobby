package com.mallang.bobby.domain.auth.oauth2.service;

import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.bobby.domain.auth.oauth2.dto.kakao.KakaoUserResponse;
import com.mallang.bobby.domain.auth.oauth2.exception.NotSupportProviderException;
import com.mallang.bobby.domain.auth.oauth2.service.kakao.KakaoOAuth2Service;
import com.mallang.bobby.domain.auth.oauth2.service.naver.NaverOAuth2Service;
import com.mallang.bobby.domain.auth.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2ServiceFactory {
	private final KakaoOAuth2Service kakaoOAuth2Service;
	private final NaverOAuth2Service naverOAuth2Service;

	public OAuth2Service getOAuth2Service(OAuth2Provider oAuth2Provider) {
		if (OAuth2Provider.kakao.equals(oAuth2Provider)) {
			return kakaoOAuth2Service;
		} else if (OAuth2Provider.naver.equals(oAuth2Provider)) {
			return naverOAuth2Service;
		}

		log.error("OAuth2ServiceFactory.getOAuth2Service({}) error", oAuth2Provider);
		throw new NotSupportProviderException(oAuth2Provider.name());
	}
}

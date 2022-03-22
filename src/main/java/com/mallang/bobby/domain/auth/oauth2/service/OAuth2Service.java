package com.mallang.bobby.domain.auth.oauth2.service;

import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.bobby.domain.auth.oauth2.dto.kakao.KakaoUserResponse;
import com.mallang.bobby.domain.auth.oauth2.exception.NotSupportProviderException;
import com.mallang.bobby.domain.auth.oauth2.service.kakao.KakaoOAuth2Service;
import com.mallang.bobby.domain.auth.user.dto.UserDto;
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

	public UserDto getUserInfo(OAuth2Provider oAuth2Provider, String code) {
		if (OAuth2Provider.kakao.equals(oAuth2Provider)) {
			final String accessToken = this.getAccessToken(oAuth2Provider, code);
			final KakaoUserResponse kakaoUserResponse = kakaoOAuth2Service.getUser(accessToken);

			return UserDto.builder()
				.userId(kakaoUserResponse.getId())
				.authorizedBy(oAuth2Provider)
				.nickname(kakaoUserResponse.getKakaoAccount().getProfile().getNickname())
				.profileImageUrl(kakaoUserResponse.getKakaoAccount().getProfile().getProfileImageUrl())
				.profileThumbnailUrl(kakaoUserResponse.getKakaoAccount().getProfile().getThumbnailImageUrl())
				.build();
		}

		throw new NotSupportProviderException(oAuth2Provider.name());
	}

	private String getAccessToken(OAuth2Provider oAuth2Provider, String code) {
		if (OAuth2Provider.kakao.equals(oAuth2Provider)) {
			final String accessToken = kakaoOAuth2Service.getAccessToken(code);
			return accessToken;
		}

		log.error("OAuth2Service.getAccessToken({}, {}) error", oAuth2Provider, code);
		throw new NotSupportProviderException(oAuth2Provider.name());
	}
}
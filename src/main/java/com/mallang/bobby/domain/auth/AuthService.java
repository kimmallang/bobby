package com.mallang.bobby.domain.auth;

import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.bobby.domain.auth.oauth2.service.OAuth2Service;
import com.mallang.bobby.domain.auth.token.UserTokenService;
import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.auth.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserService userService;
	private final OAuth2Service oAuth2Service;
	private final UserTokenService userTokenService;

	public String getAuthorizeUrl(OAuth2Provider oAuth2Provider) {
		return oAuth2Service.getAuthorizeUrl(oAuth2Provider);
	}

	public String getUserToken(OAuth2Provider oAuth2Provider, String code) {
		final UserDto userDto = oAuth2Service.getUserInfo(oAuth2Provider, code);
		userService.save(userDto);

		return userTokenService.generateUtkn(userDto);
	}
}

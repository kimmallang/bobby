package com.mallang.bobby.domain.auth.oauth2.service;

import com.mallang.bobby.domain.auth.user.dto.UserDto;

public interface OAuth2Service {
	String getAuthorizeUrl();
	String getAccessToken(String code);
	UserDto getUser(String accessToken);
}

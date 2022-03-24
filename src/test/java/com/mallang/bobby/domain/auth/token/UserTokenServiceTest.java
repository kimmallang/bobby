package com.mallang.bobby.domain.auth.token;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.bobby.domain.auth.user.dto.UserDto;

public class UserTokenServiceTest {
	private ObjectMapper objectMapper;
	private UserTokenService userTokenService;
	private UserDto userDto;

	@BeforeEach
	public void init() {
		objectMapper = new ObjectMapper();
		userTokenService = new UserTokenService(objectMapper);

		userDto = UserDto.builder()
			.id(1L)
			.userId(2L)
			.nickname("nickname")
			.authorizedBy(OAuth2Provider.kakao)
			.profileImageUrl("profileImageUrl")
			.profileThumbnailUrl("profileThumbnailUrl")
			.build();
	}

	@Test
	public void test() {
		final String utkn = userTokenService.generateUtkn(userDto);
		assertNotEquals("", utkn);

		final UserDto convertedUserDto = userTokenService.convertUtkn(utkn);
		assertEquals(userDto, convertedUserDto);
	}
}

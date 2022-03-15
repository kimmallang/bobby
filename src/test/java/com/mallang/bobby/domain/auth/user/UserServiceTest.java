package com.mallang.bobby.domain.auth.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.auth.user.repository.UserRepository;
import com.mallang.bobby.domain.auth.user.service.UserService;
import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;

@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {
	private ModelMapper modelMapper;
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void init() {
		modelMapper = new ModelMapper();
		userService = new UserService(modelMapper, userRepository);
	}

	@Test
	public void insertUser() {
		final UserDto userDto = UserDto.builder()
			.authorizedBy(OAuth2Provider.kakao)
			.userId(1234L)
			.nickname("testNickname")
			.profileImageUrl("testPOProfileImageUrl")
			.profileThumbnailUrl("testProfileThumbnailUrl")
			.build();

		userService.save(userDto);

		final UserDto savedUserDto = userService.get(OAuth2Provider.kakao, 1234L);
		assertEquals("testNickname", savedUserDto.getNickname());
		assertEquals("testPOProfileImageUrl", savedUserDto.getProfileImageUrl());
		assertEquals("testProfileThumbnailUrl", savedUserDto.getProfileThumbnailUrl());
	}

	@Test
	public void updateUser() {
		final UserDto userDto = UserDto.builder()
			.authorizedBy(OAuth2Provider.kakao)
			.userId(1234L)
			.nickname("testNickname")
			.profileImageUrl("testPOProfileImageUrl")
			.profileThumbnailUrl("testProfileThumbnailUrl")
			.build();

		userService.save(userDto);

		final UserDto savedUser = userService.get(OAuth2Provider.kakao, 1234L);
		savedUser.setNickname("testNickname2");

		userService.save(savedUser);
		final UserDto updatedUser = userService.get(savedUser.getId());

		assertEquals("testNickname2", updatedUser.getNickname());
	}
}

package com.mallang.bobby.domain.auth.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.auth.user.entity.User;
import com.mallang.bobby.domain.auth.user.repository.UserRepository;
import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;

	public UserDto get(Long id) {
		final User user = userRepository.findById(id).orElse(new User());
		return modelMapper.map(user, UserDto.class);
	}

	public UserDto get(OAuth2Provider provider, String userId) {
		final User user = userRepository.findByAuthorizedByAndUserId(provider, userId).orElse(new User());
		return modelMapper.map(user, UserDto.class);
	}

	public Long save(UserDto userDto) {
		final User user = modelMapper.map(userDto, User.class);
		final Long id = userRepository.findByAuthorizedByAndUserId(userDto.getAuthorizedBy(), userDto.getUserId()).orElse(new User()).getId();
		user.setId(id);

		return userRepository.save(user).getId();
	}
}

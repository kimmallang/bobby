package com.mallang.bobby.domain.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.user.dto.UserDto;
import com.mallang.bobby.domain.user.entity.User;
import com.mallang.bobby.domain.user.repository.UserRepository;
import com.mallang.bobby.oauth2.dto.OAuth2Provider;
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

	public UserDto get(OAuth2Provider provider, Long userId) {
		final User user = userRepository.findByAuthorizedByAndUserId(provider, userId).orElse(new User());
		return modelMapper.map(user, UserDto.class);
	}

	public void save(UserDto userDto) {
		final User user = modelMapper.map(userDto, User.class);
		final Long id = userRepository.findByUserId(userDto.getUserId()).orElse(new User()).getId();
		user.setId(id);

		userRepository.save(user);
	}
}

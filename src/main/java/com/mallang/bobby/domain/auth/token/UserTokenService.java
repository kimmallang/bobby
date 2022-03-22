package com.mallang.bobby.domain.auth.token;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.util.cipher.AES256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenService {
	private final ObjectMapper objectMapper;

	public String generateUtkn(UserDto userDto) {
		try {
			final String userDtoString = objectMapper.writeValueAsString(userDto);
			return AES256.encrypt(userDtoString);
		} catch (JsonProcessingException e) {
			log.error("UserTokenService.generateUtkn({}) fail.", userDto);
			return "";
		}
	}

	public UserDto convertUtkn(String utkn) {
		try {
			final String userDtoString = AES256.decrypt(utkn);
			return objectMapper.readValue(userDtoString, UserDto.class);
		} catch (JsonProcessingException e) {
			log.error("UserTokenService.convertUtkn({}) fail.", utkn);
			return new UserDto();
		}
	}
}

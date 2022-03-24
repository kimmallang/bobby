package com.mallang.bobby.domain.auth.token;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
			final String utknText = userDtoString+ "." + AES256.encrypt(userDto.getId().toString());
			final byte[] utknBytes = utknText.getBytes(StandardCharsets.UTF_8);
			final String utkn = Base64.getEncoder().encodeToString(utknBytes);
			return utkn;
		} catch (JsonProcessingException e) {
			log.error("UserTokenService.generateUtkn({}) fail.", userDto);
			return "";
		}
	}

	public UserDto convertUtkn(String utkn) {
		try {
			final String decodedUtkn = new String(Base64.getDecoder().decode(utkn));
			final int splitIndex = decodedUtkn.lastIndexOf('.');
			final String userDtoString = decodedUtkn.substring(0, splitIndex);
			final Long id = Long.parseLong(AES256.decrypt(decodedUtkn.substring(splitIndex + 1)));
			final UserDto userDto = objectMapper.readValue(userDtoString, UserDto.class);

			if (userDto.getId() != id.longValue()) {
				log.error("UserTokenService.convertUtkn({}) fail. 토큰 정보 이상", utkn);
				return new UserDto();
			}

			return userDto;
		} catch (JsonProcessingException e) {
			log.error("UserTokenService.convertUtkn({}) fail.", utkn);
			return new UserDto();
		}
	}
}

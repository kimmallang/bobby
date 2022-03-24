package com.mallang.bobby.domain.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
	@GetMapping("/user")
	public ResponseDto user(HttpServletRequest request) {
		final UserDto userDto = (UserDto)request.getAttribute("user");
		return ResponseDto.builder()
			.data(userDto)
			.build();
	}
}

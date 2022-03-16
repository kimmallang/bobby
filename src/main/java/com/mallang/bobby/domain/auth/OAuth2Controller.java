package com.mallang.bobby.domain.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.dto.ResponseDto;
import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/oauth2")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
	private final AuthService authService;

	@GetMapping("/authorize/{provider}")
	public ResponseDto authorize(@PathVariable OAuth2Provider provider) {
		return ResponseDto.builder()
			.data(authService.getAuthorizeUrl(provider))
			.build();
	}

	@GetMapping("/callback/{provider}")
	public ResponseDto callback(@PathVariable OAuth2Provider provider, String code, HttpServletResponse httpServletResponse) {
		return ResponseDto.builder()
			.data(authService.getUserToken(provider, code))
			.build();
	}
}

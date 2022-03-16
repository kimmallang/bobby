package com.mallang.bobby.domain.auth;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.dto.ResponseDto;
import com.mallang.bobby.dto.ResponseStatus;
import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.bobby.domain.auth.oauth2.exception.NotSupportProviderException;
import com.mallang.bobby.domain.auth.oauth2.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/oauth2")
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
	public ResponseDto callback(@PathVariable OAuth2Provider provider, String code) {
		return ResponseDto.builder()
			.data(authService.getUserToken(provider, code))
			.build();
	}
}

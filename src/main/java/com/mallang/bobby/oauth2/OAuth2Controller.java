package com.mallang.bobby.oauth2;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.dto.ResponseDto;
import com.mallang.bobby.dto.ResponseStatus;
import com.mallang.bobby.oauth2.dto.OAuth2Provider;
import com.mallang.bobby.oauth2.exception.NotSupportProviderException;
import com.mallang.bobby.oauth2.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/oauth2")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
	private final OAuth2Service oAuth2Service;

	@GetMapping("/authorize/{provider}")
	public ResponseDto authorize(@PathVariable OAuth2Provider provider) {
		return ResponseDto.builder()
			.data(oAuth2Service.getAuthorizeUrl(provider))
			.build();
	}

	@GetMapping("/callback/{provider}")
	public ResponseDto callback(@PathVariable OAuth2Provider provider, String code) {
		return ResponseDto.builder()
			.data(oAuth2Service.getUserInfo(provider, code))
			.build();
	}

	@ExceptionHandler(NotSupportProviderException.class)
	public ResponseDto handleError(NotSupportProviderException e) {
		log.error(e.getMessage());
		return ResponseDto.builder().status(ResponseStatus.ERROR).build();
	}
}

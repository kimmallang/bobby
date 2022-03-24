package com.mallang.bobby.domain.auth;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Object> callback(@PathVariable OAuth2Provider provider, String code) throws URISyntaxException {
		final String utkn = authService.getUserToken(provider, code);

		final URI redirectUri = new URI("http://localhost:3000?utkn="+utkn);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(redirectUri);

		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}
}

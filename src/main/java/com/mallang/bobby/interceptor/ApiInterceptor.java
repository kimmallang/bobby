package com.mallang.bobby.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mallang.bobby.domain.auth.token.UserTokenService;
import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.exception.NotAllowedDomainException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiInterceptor implements HandlerInterceptor {
	private final UserTokenService userTokenService;
	final String[] allowedDomain = new String[] {
		"http://localhost:3000",
		"http://localhost:8080",
		"https://mallang.herokuapp.com",
		"https://bobby-djk.herokuapp.com"
	};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NotAllowedDomainException {
		if (!isAllowedDomain(request)) {
			throw new NotAllowedDomainException(request.getHeader("Referer"));
		}

		readUtkn(request);

		return true;
	}

	private boolean isAllowedDomain(HttpServletRequest request) {
		final String referer = request.getHeader("Referer");

		if (referer == null) {
			return Arrays
				.stream(allowedDomain)
				.anyMatch(domain -> request
					.getRequestURL()
					.toString()
					.startsWith(domain + "/swagger-ui/index.html"));
		}

		return Arrays.stream(allowedDomain).anyMatch(referer::startsWith);
	}

	private void readUtkn(HttpServletRequest request) {
		final String utkn = request.getHeader("utkn");

		if (!StringUtils.hasLength(utkn)) {
			return;
		}

		final UserDto userDto = userTokenService.convertUtkn(utkn);
		request.setAttribute("user", userDto);
	}
}

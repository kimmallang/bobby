package com.mallang.bobby.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.NoHandlerFoundException;

public class ApiInterceptor implements HandlerInterceptor {
	final String[] allowedDomain = new String[] {
		"http://local.bobby.com:8080",
		"https://mallang.herokuapp.com",
		"https://bobby-djk.herokuapp.com"
	};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NoHandlerFoundException {
		if (!isAllowedDomain(request)) {
			final String httpMethod = request.getMethod();
			final String requestUrl = request.getRequestURL().toString();
			final HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();

			throw new NoHandlerFoundException(httpMethod, requestUrl, headers);
		}

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
}

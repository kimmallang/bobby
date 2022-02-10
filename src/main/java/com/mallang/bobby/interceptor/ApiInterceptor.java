package com.mallang.bobby.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.NoHandlerFoundException;

public class ApiInterceptor implements HandlerInterceptor {
	final String[] allowedDomain = new String[] {
		"http://local.bobby.com:8080",
		"https://mallang.herokuapp.com"
	};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NoHandlerFoundException {
		final String referer = request.getHeader("Referer");

		if (!isAllowedDomain(request, referer)) {
			throw new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), null);
		}

		return true;
	}

	private boolean isAllowedDomain(HttpServletRequest request, String referer) {
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

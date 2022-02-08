package com.mallang.bobby.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class ApiInterceptor implements HandlerInterceptor {
	final String[] allowedDomain = new String[] {
		"http://localhost",
		"https://mallang.herokuapp.com"
	};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		final String referer = request.getHeader("Referer");

		if (referer == null) {
			return request.getRequestURI().startsWith("/swagger-ui/index.html");
		}

		return Arrays.stream(allowedDomain).anyMatch(referer::startsWith);
	}
}

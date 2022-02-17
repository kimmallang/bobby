package com.mallang.bobby.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mallang.bobby.exception.NotAllowedDomainException;

public class ApiInterceptor implements HandlerInterceptor {
	final String[] allowedDomain = new String[] {
		"http://localhost:3000",
		"http://localhost:8080",
		"https://mallang.herokuapp.com",
		"https://bobby-djk.herokuapp.com"
	};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NoHandlerFoundException {
		if (!isAllowedDomain(request)) {
			throw new NotAllowedDomainException(request.getHeader("Referer"));
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

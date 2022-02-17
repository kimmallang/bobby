package com.mallang.bobby.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mallang.bobby.dto.ResponseDto;
import com.mallang.bobby.dto.ResponseStatus;
import com.mallang.bobby.exception.NotAllowedDomainException;

@RestControllerAdvice
class ApiExceptionHandler {
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseDto handleNotFound() {
		return ResponseDto.builder().status(ResponseStatus.BAD_REQUEST).build();
	}

	@ExceptionHandler(NotAllowedDomainException.class)
	public ResponseDto handleForbidden(NotAllowedDomainException e) {
		return ResponseDto.builder().status(ResponseStatus.FORBIDDEN)
			.data(e.getMessage())
			.build();
	}

	@ExceptionHandler(Exception.class)
	public ResponseDto handleError() {
		return ResponseDto.builder().status(ResponseStatus.ERROR).build();
	}
}

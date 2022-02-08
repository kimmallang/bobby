package com.mallang.bobby.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mallang.bobby.domain.dto.ResponseDto;
import com.mallang.bobby.domain.dto.ResponseStatus;

@RestControllerAdvice
class ApiExceptionHandler {
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseDto handleNotFound() {
		return ResponseDto.builder().status(ResponseStatus.BAD_REQUEST).build();
	}

	@ExceptionHandler(Exception.class)
	public ResponseDto handleError() {
		return ResponseDto.builder().status(ResponseStatus.ERROR).build();
	}
}

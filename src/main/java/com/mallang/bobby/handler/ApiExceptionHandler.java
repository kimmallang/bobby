package com.mallang.bobby.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mallang.bobby.dto.ResponseDto;
import com.mallang.bobby.dto.ResponseStatus;
import com.mallang.bobby.exception.NotAllowedDomainException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
class ApiExceptionHandler {
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseDto handleNotFound() {
		return ResponseDto.builder().status(ResponseStatus.BAD_REQUEST).build();
	}

	@ExceptionHandler(NotAllowedDomainException.class)
	public ResponseDto handleForbidden(NotAllowedDomainException e) {
		return ResponseDto.builder().status(ResponseStatus.FORBIDDEN).build();
	}

	@ExceptionHandler(Exception.class)
	public ResponseDto handleError(Exception e) {
		log.error(e.getMessage());
		return ResponseDto.builder().status(ResponseStatus.ERROR).build();
	}
}

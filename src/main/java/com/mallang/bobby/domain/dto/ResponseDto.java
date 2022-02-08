package com.mallang.bobby.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseDto<T> {
	@Builder.Default
	private ResponseStatus status = ResponseStatus.SUCCESS;
	private String message;
	private T data;

	public String getMessage() {
		return status.getMessage();
	}
}

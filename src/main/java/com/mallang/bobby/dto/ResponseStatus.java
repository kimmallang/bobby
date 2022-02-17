package com.mallang.bobby.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
	SUCCESS("SUCCESS", "요청이 처리되었습니다."),
	ERROR("ERROR", "요청이 실패했습니다."),
	BAD_REQUEST("BAD_REQUEST", "요청이 올바르지 않습니다."),
	FORBIDDEN("FORBIDDEN", "요청 권한이 없습니다.");

	private String status;
	private String message;
}

package com.mallang.bobby.exception;

public class NotAllowedDomainException extends RuntimeException {
	public NotAllowedDomainException(String domain) {
		super("허용되지 않은 도메인입니다: " + domain);
	}
}

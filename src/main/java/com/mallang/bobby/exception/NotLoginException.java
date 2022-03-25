package com.mallang.bobby.exception;

public class NotLoginException extends RuntimeException {
	public NotLoginException() {
		super("로그인 정보가 없습니다.");
	}
}

package com.mallang.bobby.exception;

public class UnExpectedException extends RuntimeException {
	public UnExpectedException(String message) {
		super("예상하지 못한 예외 발생: " + message);
	}
}

package com.mallang.bobby.oauth2.exception;

public class NotSupportProviderException extends RuntimeException {
	public NotSupportProviderException(String provider) {
		super("아직 지원하지 않는 provider 입니다. >> " + provider);
	}
}

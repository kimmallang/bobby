package com.mallang.bobby.util.cipher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SHA256 {
	public static String encrypt(String text) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(text.getBytes());

			return bytesToHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			log.error("SHA256.encrypt({}) fail.", text);
			return "";
		}
	}

	private static String bytesToHex(byte[] bytes) {
		final StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}

		return builder.toString();
	}
}
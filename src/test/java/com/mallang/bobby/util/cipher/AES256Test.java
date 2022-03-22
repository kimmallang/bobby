package com.mallang.bobby.util.cipher;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AES256Test {
	@Test
	public void test() {
		final String text = "helloWorld";
		final String encrypted = AES256.encrypt(text);
		final String decrypted = AES256.decrypt(encrypted);

		assertNotEquals(text, encrypted);
		assertEquals(text, decrypted);
	}
}

package com.mallang.bobby.util.cipher;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SHA256Test {
	@Test
	public void test() {
		final String text1 = "helloWorld";
		final String text2 = "helloWorld2";

		assertNotEquals(SHA256.encrypt(text1), SHA256.encrypt(text2));
		assertEquals(SHA256.encrypt(text1), SHA256.encrypt(text1));
	}
}

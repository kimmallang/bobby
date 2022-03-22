package com.mallang.bobby.util.cipher;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AES256 {
	private static final String algorithm = "AES/CBC/PKCS5Padding";
	private static final String key = "MallangBobbyMallangBobby";
	private static final String iv = key.substring(0, 16);

	public static String encrypt(String text) {
		try {
			final Cipher cipher = Cipher.getInstance(algorithm);
			final SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			final IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

			byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			log.error("AES256.encrypt({}) fail.", text);
			return "";
		}
	}

	public static String decrypt(String cipherText) {
		try {
			final Cipher cipher = Cipher.getInstance(algorithm);
			final SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			final IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

			final byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
			final byte[] decrypted = cipher.doFinal(decodedBytes);
			return new String(decrypted, "UTF-8");
		} catch (Exception e) {
			log.error("AES256.decrypt({}) fail.", cipherText);
			return "";
		}
	}
}
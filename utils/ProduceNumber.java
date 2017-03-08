package com.yyjz.icop.base.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProduceNumber {
	private static final Logger logger = LoggerFactory.getLogger(ProduceNumber.class);

	private static String SHA1PRNG = "SHA1PRNG";

	public static String produce() {
		String result = "";
		try {
			SecureRandom srandom = SecureRandom.getInstance(SHA1PRNG);
			result = srandom.nextInt(1000000) + "";
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}

		if (result.length() != 6) {
			return produce();
		}
		return result;
	}
}
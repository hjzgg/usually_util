package com.yyjz.icop.base.utils;

import java.util.regex.Pattern;

public class Validate {
	public static final String REGEX_USERNAME = "^(?!\\d+$)[\\da-zA-Z_]{4,18}$";
	public static final String REGEX_USERNAMEWITHBLANK = "^(?!\\d+$)[\\da-zA-Z_]{4,18}$";
	public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
	public static final String REGEX_MOBILE = "^(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$";
	public static final String REGEX_MOBILEWITHBLANK = "^(\\s*)|((13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$)";
	public static final String REGEX_PHONEANDMOBILE = "^(((13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8})|(\\d{3}-\\d{8}|\\d{4}-\\d{7}))$";
	public static final String REGEX_TENANTTEL = "^(\\+*(\\(\\d+\\)|\\d+)*-*\\d+-*\\d+-*\\d+,)*\\+*(\\(\\d+\\)|\\d+)*-*\\d+-*\\d+-*\\d+$";
	public static final String REGEX_MUTIPHONEANDMOBILE = "^((((13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8})|(\\d{3}-\\d{8}|\\d{4}-\\d{7})),)*(((13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8})|(\\d{3}-\\d{8}|\\d{4}-\\d{7}))$";
	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	public static final String REGEX_CHINESE = "^[一-龥],{0,}$";
	public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
	public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
	public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
	public static final String REGEX_TENANT_CODE = "^(\\s*)|[a-z][a-z0-9]{1,15}$";

	public static boolean isUsername(String username) {
		return Pattern.matches("^(?!\\d+$)[\\da-zA-Z_]{4,18}$", username);
	}

	public static boolean isPassword(String password) {
		return Pattern.matches("^[a-zA-Z0-9]{6,16}$", password);
	}

	public static boolean isMobile(String mobile) {
		return Pattern.matches("^(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$", mobile);
	}

	public static boolean isEmail(String email) {
		return Pattern.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
				email);
	}

	public static boolean isChinese(String chinese) {
		return Pattern.matches("^[一-龥],{0,}$", chinese);
	}

	public static boolean isIDCard(String idCard) {
		return Pattern.matches("(^\\d{18}$)|(^\\d{15}$)", idCard);
	}

	public static boolean isUrl(String url) {
		return Pattern.matches("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?", url);
	}

	public static boolean isIPAddr(String ipAddr) {
		return Pattern.matches("(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)", ipAddr);
	}
}
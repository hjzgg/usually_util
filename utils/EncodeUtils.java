package com.yycc.common.utils;

import java.io.UnsupportedEncodingException;

public class EncodeUtils {
	
	/**
	 * 转换字符串编码，iso-8859-1  => utf-8
	 * @param string
	 * @return
	 */
	public static String toUTF8(String string){
		String retVal = string;
		try {
			byte[] bytes = string.getBytes("iso-8859-1");
			retVal = new String(bytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			return retVal;
		}
		
	}
}

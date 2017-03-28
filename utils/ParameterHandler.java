package com.yycc.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

/**
 * JSON/Form 提交参数处理
 * 
 */
public final class ParameterHandler {

	/**
	 * 从request中取出请求的参数
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getBodyContent(HttpServletRequest request)
			throws IOException {
//		request.setCharacterEncoding("UTF-8");
		InputStream in = request.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
}

package com.yyjz.icop.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class PropertyUtil {
	private static Properties prop = null;

	public static String getPropertyByKey(String key) {
		String value = prop.getProperty(key);
		return StringUtils.isBlank(value) ? "" : value;
	}

	static {
		prop = new Properties();
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("../config/application.properties");
			if(in == null) {
				in = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
			}
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
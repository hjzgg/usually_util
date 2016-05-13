package com.yycc.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Administrator
 *
 */
public class ConvertBean {
	
	private static final Logger logger = LoggerFactory.getLogger(ConvertBean.class);
	
	public static <T> Object convert(Object entity,String className) {
		Field[] fields = entity.getClass().getDeclaredFields();
		try {
			Class<?> c = Class.forName(className);
			Object bean = c.newInstance();
			for (Field field : fields) {
				try {
					String fieldName = field.getName().
							replaceFirst(field.getName().substring(0, 1),field.getName().substring(0, 1).
									toUpperCase());
					Method getM = (Method) entity.getClass().getMethod(
						       "get" + fieldName);
					Object val = getM.invoke(entity);
					Method setM = c.getDeclaredMethod("set"+fieldName, field.getType());
					setM.invoke(bean, val);
				} catch (NoSuchMethodException e) {
					continue;
				}
			}
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("对象转换失败...");
		}
		return null;
	}

}

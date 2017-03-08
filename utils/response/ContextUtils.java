package com.yyjz.icop.base.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring应用上下文工具类
 */
public class ContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext; //应用上下文对象

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ContextUtils.applicationContext = applicationContext;
    }

    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String beanName, Class<T> cls) {
        return applicationContext.getBean(beanName, cls);
    }

    public static <T> T getBean(Class<T> cls) {
        return applicationContext.getBean(cls);
    }
}

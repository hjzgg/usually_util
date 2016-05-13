package com.yycc.common.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * <b>cookie操作</b>
 * <p>
 * @version 1.0
 * @author shl
 * @time 2016年4月23日 下午3:17:00
 */
public class CookieUtil {

    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);


    /**
     * 创建cookie，不过期，js不可操作。base64编码
     *
     * @param key
     * @param value
     * @return
     */
    public static Cookie createCookie(String key, String value) {
        return createCookie(key, value, true);
    }

    /**
     * 创建cookie，不过期，js不可操作。base64编码
     *
     * @param key
     * @param value
     * @return
     */
    public static Cookie createCookie(String key, String value, boolean httpOnly) {
        Cookie cookie = new Cookie(key, URLEncoder.encode(value));
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        return cookie;
    }


    /**
     * 查找cookie内容
     *
     * @param cookies
     * @param key
     * @return
     */
    public static String findCookieValue(Cookie[] cookies, String key) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return URLDecoder.decode(cookie.getValue());
                }
            }
        }
        return null;

    }


    /**
     * 将cookie过期
     *
     * @param key
     * @return
     */
    public static Cookie expireCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

}

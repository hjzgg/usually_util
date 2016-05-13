package com.ds.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordHelper {
	private static final String DEFAULT_SALT = "123456";
	/**
     * 算法名
     */
    private String algorithmName = "md5";
    
    /**
     * hash次数
     */
    private int hashIterations = 2;
    
	public String getEncryptPassword(String password){
        String newPassword = new SimpleHash(
                algorithmName,
                password,
                ByteSource.Util.bytes(DEFAULT_SALT),
                hashIterations).toHex();

        return newPassword;
   }
}

package com.yycc.ucenter.utils;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 
* @ClassName: PasswordHelper
* @Description: 密码加密服务
*
 */
//@Service
public class PasswordHelper {

	/**
	 * 随机数生成器
	 */
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    /**
     * 算法名
     */
    private String algorithmName = "md5";
    
    /**
     * hash次数
     */
    private int hashIterations = 2;

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    /**
     * 生成加密过的密码
     * @param user 用户
     */
    public void encryptPassword(UserCredentialEntity user) {

        user.setSalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }
    
    public void encryptPassword(UserCredentialVO user) {

        user.setSalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }
    
    public String getEncryptPassword(String password, String salt){
         String newPassword = new SimpleHash(
                 algorithmName,
                 password,
                 ByteSource.Util.bytes(salt),
                 hashIterations).toHex();

         return newPassword;
    }
}

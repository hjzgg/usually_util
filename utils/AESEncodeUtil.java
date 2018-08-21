import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESEncodeUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(AESEncodeUtil.class);

    public static final String DefaultKey = "hjzgg1qa2ws3ed!@#$%^";

    /**
     * 将base 64 code AES解密
     *
     * @param contentString 待解密的base 64 code
     * @param keyString     解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String contentString, String keyString) {
        String decryptString = "404";
        try {
            if (!"".equals(contentString)) {
                decryptString = aesDecryptByBytes(base64Decode(contentString), keyString);
            }
        } catch (Exception e) {
            LOGGER.error("解密发生异常：" + e);

            if (e instanceof BadPaddingException) {
                return "404";
            }
        }
        return decryptString;
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(decryptKey.getBytes());
        kgen.init(128, random);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws IOException
     * @throws Exception
     */
    @SuppressWarnings("restriction")
    public static byte[] base64Decode(String base64Code) throws IOException {
        return "".equals(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     * AES加密为base 64 code
     *
     * @param contentString 待加密的内容
     * @param keyString     加密密钥
     * @return 加密后的base 64 code
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public static String aesEncrypt(String contentString, String keyString) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return base64Encode(aesEncryptToBytes(contentString, keyString));
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    @SuppressWarnings("restriction")
    public static String base64Encode(Object bytes) {
        return new BASE64Encoder().encode((byte[]) bytes);
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws Exception
     * @throws Exception
     */
    public static Object aesEncryptToBytes(String content, String encryptKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(encryptKey.getBytes());
        kgen.init(128, random);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * 结合base64实现md5加密
     *
     * @param msg 待加密字符串
     * @return 获取md5后转为base64
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    public static String md5Encrypt(String msg) throws NoSuchAlgorithmException {
        return "".equals(msg) ? null : base64Encode(md5(msg.getBytes()));
    }

    /**
     * 获取byte[]的md5值
     *
     * @param bytes byte[]
     * @return md5
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    public static byte[] md5(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        return md.digest();
    }

    public static void main(String[] args) throws Exception {

        String content = "wechat2016";// 需要加密的明文
        System.out.println("加密前：" + content);

        System.out.println("默认的加密密钥和解密密钥：" + DefaultKey);

        String encrypt = aesEncrypt(content, DefaultKey);
        System.out.println("加密后密文：" + encrypt);

        String decrypt = aesDecrypt("5geddJ7Qizcd+o6UKIuMkqGJRfGJpkwOPG2d/c50TXg=", DefaultKey);
        System.out.println("解密后明文：" + decrypt);
    }

}

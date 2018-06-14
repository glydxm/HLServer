package com.glyfly.tools.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by 123 on 2017/5/9.
 * DES加密/解密工具类
 */

public class DESUtil {

    private final static String HEX = "0123456789ABCDEF";
    private final static String TRANSFORMATION = "DES/CBC/PKCS5Padding";//DES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private final static String IVPARAMETERSPEC = "01020304";////初始化向量参数，AES 为16bytes. DES 为8bytes.
    private final static String ALGORITHM = "DES";//DES是加密方式
    private final static String SHA1PRNG = "SHA1PRNG";//// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法

    /**
     * 生成随机数，可以当做动态的密钥 加密和解密的密钥必须一致，不然将不能解密
     */
    public static String generateKey() {
        try {
            SecureRandom localSecureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] bytes_key = new byte[20];
            localSecureRandom.nextBytes(bytes_key);
            String str_key = toHex(bytes_key);
            return str_key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 二进制转字符
     * @param bytes 需要转换的byte数组
     * @return 转换后的二进制字符串
     */
    public static String toHex(byte[] bytes) {
        if (bytes == null)
            return "";
        StringBuffer result = new StringBuffer(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            appendHex(result, bytes[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    // 对密钥进行处理
    private static Key getRawKey1(String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom sr = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(key.getBytes());
        kgen.init(56, sr); //DES固定格式为64bits，即8bytes。
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return new SecretKeySpec(raw, ALGORITHM);
    }

    // 对密钥进行处理
    private static Key getRawKey2(String key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     */
    public static String encode(String key, String data) {
        return encode(key, data.getBytes());
    }


    /**
     * DES算法，加密
     *
     * @param key  加密私钥，长度不能够小于8位
     * @param data 待加密byte数组
     * @return 加密后的字节数组，一般结合Base64编码使用
     */
    public static String encode(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, getRawKey1(key), iv);
            byte[] bytes = cipher.doFinal(data);
            return new BASE64Encoder().encode(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取编码后的值
     *
     * @param key 加密私钥，长度不能够小于8位
     * @param data 待解密字符串
     * @return
     */
    public static String decode(String key, String data) {
        try {
            return decode(key, new BASE64Decoder().decodeBuffer(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * DES算法，解密
     *
     * @param key  解密私钥，长度不能够小于8位
     * @param data 待解密byte数组
     * @return 解密后的字节数组
     */
    public static String decode(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, getRawKey1(key), iv);
            byte[] original = cipher.doFinal(data);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            return null;
        }
    }
}

package com.glyfly.tools.encrypt;

import java.io.UnsupportedEncodingException;

/**
 * Created by 123 on 2017/5/9.
 * 异或加密/解密工具类
 */

public class XORUtil {

    /**
     *  固定key
     *  key值 0x01-0x7f（1-127）
     *  @param bytes 需要加/解密的byte数组
     *  @return 加/解密后的byte数组
     */
    public static byte[] encryptKey(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = 0x69;
        for (int i = 0; i < len; i++) {
            bytes[i] ^= key;
        }
        return bytes;
    }

    /**
     *  固定key
     *  key值 0x01-0x7f（1-127）
     *  @param string 需要加密的字符串
     *  @return 加密后的字符串
     */
    public static String encryptKey(String string){
        if (string == null || "".equals(string.trim())){
            return "";
        }
        try {
            byte[] bytes = encryptKey(string.getBytes("UTF8"));
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  固定key
     *  key值 0x01-0x7f（1-127）
     *  @param string 需要解密的字符串
     *  @return 解密后的字符串
     */
    public static String decryptKey(String string){
        if (string == null || "".equals(string.trim())){
            return "";
        }
        try {
            byte[] bytes = encryptKey(string.getBytes("UTF8"));
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  不固定key
     *  key值 0x01-0x7f（1-127）
     *  @param bytes 需要加密的byte数组
     *  @return 加密后的byte数组
     */
    public static byte[] encrypt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = 0x69;
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (bytes[i] ^ key);
            key = bytes[i];
        }
        return bytes;
    }

    /**
     *  不固定key
     *  key值 0x01-0x7f（1-127）
     *  @param bytes 需要解密的byte数组
     *  @return 解密后的byte数组
     */
    public static byte[] decrypt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = 0x69;
        for (int i = len - 1; i > 0; i--) {
            bytes[i] = (byte) (bytes[i] ^ bytes[i - 1]);
        }
        bytes[0] = (byte) (bytes[0] ^ key);
        return bytes;
    }

    /**
     *  不固定key
     *  key值 0x01-0x7f（1-127）
     *  @param string 需要加密的字符串
     *  @return 加密后的字符串
     */
    public static String encrypt(String string){
        if (string == null || "".equals(string.trim())){
            return "";
        }
        try {
            byte[] bytes = encrypt(string.getBytes("UTF8"));
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  不固定key
     *  key值 0x01-0x7f（1-127）
     *  @param string 需要解密的字符串
     *  @return 解密后的字符串
     */
    public static String decrypt(String string){
        if (string == null || "".equals(string.trim())){
            return "";
        }
        try {
            byte[] bytes = decrypt(string.getBytes("UTF8"));
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}

package com.glyfly.tools.encrypt;

import com.glyfly.tools.ToolString;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/5/9.
 * RSA算法是最流行的公钥密码算法，使用长度可以变化的密钥。RSA是第一个既能用于数据加密也能用于数字签名的算法。
 *
 * RSA算法原理如下：
 *
 * 1.随机选择两个大质数p和q，p不等于q，计算N=pq；
 * 2.选择一个大于1小于N的自然数e，e必须与(p-1)(q-1)互素。
 * 3.用公式计算出d：d×e = 1 (mod (p-1)(q-1)) 。
 * 4.销毁p和q。
 *
 * 最终得到的N和e就是“公钥”，d就是“私钥”，发送方使用N去加密数据，接收方只有使用d才能解开数据内容。
 *
 * RSA的安全性依赖于大数分解，小于1024位的N已经被证明是不安全的，而且由于RSA算法进行的都是大数计算，使得RSA最快的情况也比DES慢上倍，
 * 这是RSA最大的缺陷，因此通常只能用于加密少量数据或者加密密钥，但RSA仍然不失为一种高强度的算法。
 */
public class RSAUtil {

    public static final String RSA = "RSA";// 非对称加密密钥算法
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    public static final int DEFAULT_KEY_SIZE = 2048;//秘钥默认长度
    public static final byte[] DEFAULT_SPLIT = "#PART#".getBytes();    // 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
    public static final int DEFAULT_BUFFERSIZE = (DEFAULT_KEY_SIZE / 8) - 11;// 当前秘钥支持加密的最大字节数
    public static final int DEFAULT = 0;
    public static final int PUBLIC_KEY_ENCRYPT = 1;
    public static final int PUBLIC_KEY_DECRYPT = 2;
    public static final int PUBLIC_KEY_SPLIT_ENCRYPT = 3;
    public static final int PUBLIC_KEY_SPLIT_DECRYPT = 4;
    public static final int PRIVATE_KEY_ENCRYPT = 5;
    public static final int PRIVATE_KEY_DECRYPT = 6;
    public static final int PRIVATE_KEY_SPLIT_ENCRYPT = 7;
    public static final int PRIVATE_KEY_SPLIT_DECRYPT = 8;

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048，一般1024
     * @return KeyPair RSA密钥对
     */
    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data 待加密字符串
     * @param type 类型
     * @return String 加密后字符串
     */
    public static String work(byte[] key, String data, int type){
        if (key == null || key.length == 0 || isEmpty(data)){
            return data;
        }
        try {
            byte[] bytes = data.getBytes();
            switch (type){
                case DEFAULT:
                    byte[] newData0 = decryptByPrivateKey(bytes, key);
                    return new String(newData0);
                case PUBLIC_KEY_ENCRYPT:
                    byte[] newData1 = encryptByPublicKey(bytes, key);
                    return new String(newData1);
                case PUBLIC_KEY_DECRYPT:
                    byte[] newData2 = decryptByPublicKey(bytes, key);
                    return new String(newData2);
                case PUBLIC_KEY_SPLIT_ENCRYPT:
                    byte[] newData3 = encryptByPublicKeyForSpilt(bytes, key);
                    return new String(newData3);
                case PUBLIC_KEY_SPLIT_DECRYPT:
                    byte[] newData4 = decryptByPublicKeyForSpilt(bytes, key);
                    return new String(newData4);
                case PRIVATE_KEY_ENCRYPT:
                    byte[] newData5 = encryptByPrivateKey(bytes, key);
                    return new String(newData5);
                case PRIVATE_KEY_DECRYPT:
                    byte[] newData6 = decryptByPrivateKey(bytes, key);
                    return new String(newData6);
                case PRIVATE_KEY_SPLIT_ENCRYPT:
                    byte[] newData7 = encryptByPrivateKeyForSpilt(bytes, key);
                    return new String(newData7);
                case PRIVATE_KEY_SPLIT_DECRYPT:
                    byte[] newData8 = decryptByPrivateKeyForSpilt(bytes, key);
                    return new String(newData8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param publicKey 公钥密钥
     * @return byte[] 加密后数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
        cp.init(Cipher.ENCRYPT_MODE, keyPublic);
        return cp.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param publicKey 公钥密钥
     * @return byte[] 解密后数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, keyPublic);
        return cipher.doFinal(data);
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param privateKey 私钥密钥
     * @return byte[] 加密后数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param privateKey 私钥密钥
     * @return byte[] 解密后数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);

        // 解密数据
        Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(data);
        return arr;
    }

    /**
     * 公钥分段加密
     *
     * @param data 待加密数据
     * @param publicKey 公钥密钥
     * @return byte[] 加密后数据
     */
    public static byte[] encryptByPublicKeyForSpilt(byte[] data, byte[] publicKey) throws Exception {
        int dataLen = data.length;
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPublicKey(data, publicKey);
        }
        List<Byte> allBytes = new ArrayList<Byte>(2048);
        int bufIndex = 0;
        int subDataLoop = 0;
        byte[] buf = new byte[DEFAULT_BUFFERSIZE];
        for (int i = 0; i < dataLen; i++) {
            buf[bufIndex] = data[i];
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++;
                if (subDataLoop != 1) {
                    for (byte b : DEFAULT_SPLIT) {
                        allBytes.add(b);
                    }
                }
                byte[] encryptBytes = encryptByPublicKey(buf, publicKey);
                for (byte b : encryptBytes) {
                    allBytes.add(b);
                }
                bufIndex = 0;
                if (i == dataLen - 1) {
                    buf = null;
                } else {
                    buf = new byte[Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1)];
                }
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        {
            int i = 0;
            for (Byte b : allBytes) {
                bytes[i++] = b.byteValue();
            }
        }
        return bytes;
    }

    /**
     * 公钥分段解密
     *
     * @param data 待解密数据
     * @param publicKey 公钥密钥
     * @return byte[] 解密后数据
     */
    public static byte[] decryptByPublicKeyForSpilt(byte[] data, byte[] publicKey) throws Exception {
        int splitLen = DEFAULT_SPLIT.length;
        if (splitLen <= 0) {
            return decryptByPublicKey(data, publicKey);
        }
        int dataLen = data.length;
        List<Byte> allBytes = new ArrayList<Byte>(1024);
        int latestStartIndex = 0;
        for (int i = 0; i < dataLen; i++) {
            byte bt = data[i];
            boolean isMatchSplit = false;
            if (i == dataLen - 1) {
                // 到data的最后了
                byte[] part = new byte[dataLen - latestStartIndex];
                System.arraycopy(data, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPublicKey(part, publicKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            } else if (bt == DEFAULT_SPLIT[0]) {
                // 这个是以split[0]开头
                if (splitLen > 1) {
                    if (i + splitLen < dataLen) {
                        // 没有超出data的范围
                        for (int j = 1; j < splitLen; j++) {
                            if (DEFAULT_SPLIT[j] != data[i + j]) {
                                break;
                            }
                            if (j == splitLen - 1) {
                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                isMatchSplit = true;
                            }
                        }
                    }
                } else {
                    // split只有一位，则已经匹配了
                    isMatchSplit = true;
                }
            }
            if (isMatchSplit) {
                byte[] part = new byte[i - latestStartIndex];
                System.arraycopy(data, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPublicKey(part, publicKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        {
            int i = 0;
            for (Byte b : allBytes) {
                bytes[i++] = b.byteValue();
            }
        }
        return bytes;
    }

    /**
     * 私钥分段加密
     *
     * @param data 待加密的数据
     * @param privateKey 秘钥密钥
     * @return byte[] 加密后数据
     */
    public static byte[] encryptByPrivateKeyForSpilt(byte[] data, byte[] privateKey) throws Exception {
        int dataLen = data.length;
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPrivateKey(data, privateKey);
        }
        List<Byte> allBytes = new ArrayList<Byte>(2048);
        int bufIndex = 0;
        int subDataLoop = 0;
        byte[] buf = new byte[DEFAULT_BUFFERSIZE];
        for (int i = 0; i < dataLen; i++) {
            buf[bufIndex] = data[i];
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++;
                if (subDataLoop != 1) {
                    for (byte b : DEFAULT_SPLIT) {
                        allBytes.add(b);
                    }
                }
                byte[] encryptBytes = encryptByPrivateKey(buf, privateKey);
                for (byte b : encryptBytes) {
                    allBytes.add(b);
                }
                bufIndex = 0;
                if (i == dataLen - 1) {
                    buf = null;
                } else {
                    buf = new byte[Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1)];
                }
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        {
            int i = 0;
            for (Byte b : allBytes) {
                bytes[i++] = b.byteValue();
            }
        }
        return bytes;
    }

    /**
     * 私钥分段解密
     *
     * @param data 待解密数据
     * @param data 私钥密钥
     * @return byte[] 解密后数据
     */
    public static byte[] decryptByPrivateKeyForSpilt(byte[] data, byte[] privateKey) throws Exception {
        int splitLen = DEFAULT_SPLIT.length;
        if (splitLen <= 0) {
            return decryptByPrivateKey(data, privateKey);
        }
        int dataLen = data.length;
        List<Byte> allBytes = new ArrayList<Byte>(1024);
        int latestStartIndex = 0;
        for (int i = 0; i < dataLen; i++) {
            byte bt = data[i];
            boolean isMatchSplit = false;
            if (i == dataLen - 1) {
                // 到data的最后了
                byte[] part = new byte[dataLen - latestStartIndex];
                System.arraycopy(data, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPrivateKey(part, privateKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            } else if (bt == DEFAULT_SPLIT[0]) {
                // 这个是以split[0]开头
                if (splitLen > 1) {
                    if (i + splitLen < dataLen) {
                        // 没有超出data的范围
                        for (int j = 1; j < splitLen; j++) {
                            if (DEFAULT_SPLIT[j] != data[i + j]) {
                                break;
                            }
                            if (j == splitLen - 1) {
                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                isMatchSplit = true;
                            }
                        }
                    }
                } else {
                    // split只有一位，则已经匹配了
                    isMatchSplit = true;
                }
            }
            if (isMatchSplit) {
                byte[] part = new byte[i - latestStartIndex];
                System.arraycopy(data, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPrivateKey(part, privateKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        {
            int i = 0;
            for (Byte b : allBytes) {
                bytes[i++] = b.byteValue();
            }
        }
        return bytes;
    }

    private static boolean isEmpty(String s){
        if (s == null || "".equals(s.trim())){
            return true;
        }else {
            return false;
        }

    }

    public static String encrypt(String data, String privateKey){
        if (ToolString.isEmpty(data)){
            return "";
        }
        if (ToolString.isEmpty(privateKey)) {
            privateKey = "";
        }
        byte[] bytes = data.getBytes();
        byte[] key = privateKey.getBytes();
        byte[] result = null;
        try {
            result = RSAUtil.encryptByPrivateKey(bytes, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == null ? "" : result.toString();
    }
}

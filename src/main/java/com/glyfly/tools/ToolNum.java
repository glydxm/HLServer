package com.glyfly.tools;

import com.glyfly.tools.encrypt.DESUtil;

/**
 * Created by Administrator on 2017/9/5.
 */
public class ToolNum {

    public static int randomNum(int n){
        if (n < 1){
            return 0;
        }
        return  (int) ((Math.random() * 9 + 1) * ((int)Math.pow(10, n - 1)));
    }

    public static String generatePin(){
        long currentTimeMillis = System.currentTimeMillis();
        return "HL_" + currentTimeMillis;
    }

    public static String encryptUserId(String userId){
        String e = DESUtil.encode("highlife", userId);
        return e;
    }

    public static String decryptUserId(String userId){
        String d = DESUtil.decode("highlife", userId);
        return d;
    }
}

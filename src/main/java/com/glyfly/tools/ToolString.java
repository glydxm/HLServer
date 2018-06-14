package com.glyfly.tools;

import com.sun.istack.internal.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/7/6.
 */
public class ToolString {

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static String getNumFromString(String s){
        String regEx="[0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        String result = "";
        while (m.find()){
            result += m.group(0);
        }
        return result;
    }
}

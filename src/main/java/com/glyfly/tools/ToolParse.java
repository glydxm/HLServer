package com.glyfly.tools;

/**
 * Created by Administrator on 2017/5/19.
 */
public class ToolParse {

    public static int parseInt(String s){
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

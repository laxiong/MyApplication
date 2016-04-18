package com.laxiong.Utils;

/**
 * Created by xiejin on 2016/4/8.
 * Types StringUtils.java
 */
public class StringUtils {
    public static boolean isBlank(String msg){
        if(msg.trim()==null||"".equals(msg.trim()))
            return true;
        return false;
    }
}

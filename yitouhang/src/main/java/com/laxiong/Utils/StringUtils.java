package com.laxiong.Utils;

import android.text.TextUtils;

/**
 * Created by xiejin on 2016/4/8.
 * Types StringUtils.java
 */
public class StringUtils {
    public static boolean isBlank(String msg) {
        if (msg == null || "".equals(msg.trim()))
            return true;
        return false;
    }

    public static boolean testBlankAll(String... msg) {
        for (String str : msg) {
            if (isBlank(str))
                return true;
        }
        return false;
    }

    public static String getProtectedMobile(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.subSequence(7, 11));
        return builder.toString();
    }

    public static String getFactor(double fee, double num) {
        if (num <= 100&&num>0)
            return "1";
        else {
            if (fee >= 100)
                return "0";
            else
                return (num * 3 / 1000) + "";
        }
    }
    public static String choose(String... strs){
        for(String str:strs){
            if(!isBlank(str))
                return str;
        }
        return null;
    }

    /*
	 * x,xxx.xxxx
	 */
    public static String FormatFloatFour(String str) {
        if (str == null)
            return "";
        if (str.contains(".")) {
            String inte = str.substring(0, str.indexOf("."));
            if (str.length() >= (str.indexOf(".") + 5)) {
                return addcommor(inte) + str.substring(str.indexOf("."), str.indexOf(".") + 5);
            } else if (str.length() > str.indexOf(".") + 2) {
                return addcommor(inte) + str.substring(str.indexOf("."), str.length());
            } else if (str.length() == str.indexOf(".") + 2) {
                return addcommor(inte) + str.substring(str.indexOf("."), str.indexOf(".") + 2) + "0";
            } else {
                return addcommor(inte) + ".00";
            }
        } else {
            return addcommor(str); // 整数
        }
    }

    /*
	 * add ,
	 */
    private static String addcommor(String str) {

        StringBuilder sb = new StringBuilder();
        String newStr = new String(str);

        if (newStr.length() > 3)
            return splitStr(newStr, sb).toString();
        else
            return str;
    }

    /*
	 * digui
	 */
    private static StringBuilder splitStr(String newStr, StringBuilder sb) {
        String temp = newStr.substring(newStr.length() - 3, newStr.length());
        newStr = newStr.substring(0, newStr.length() - 3);
        if (newStr.length() > 3) {
            splitStr(newStr, sb);
            sb.append("," + temp);
        } else {
            sb.append(newStr + "," + temp);
        }

        return sb;
    }


}

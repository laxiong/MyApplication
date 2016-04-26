package com.laxiong.Utils;

import android.text.TextUtils;

/**
 * Created by xiejin on 2016/4/8.
 * Types StringUtils.java
 */
public class StringUtils {
    public static boolean isBlank(String msg) {
        if (msg.trim() == null || "".equals(msg.trim()))
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
        if (num <= 100)
            return "1";
        else {
            if (fee >= 100)
                return "0";
            else
                return (num * 3 / 1000) + "";
        }
    }
}

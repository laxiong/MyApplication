package com.laxiong.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.laxiong.Common.Constants;

/**
 * Created by xiejin on 2016/5/24.
 * Types LogUtils.java
 */
public class LogUtils {
    public static int level = Constants.LEVEL;
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARMING = 4;
    public static final int ERROR = 5;
    public static final String TAG = "LogUtils";

    public static void e(String tag, String msg) {
        if (level > ERROR)
            return;
        StackTraceElement stackele = Thread.currentThread().getStackTrace()[3];
        if (TextUtils.isEmpty(tag))
            tag = TAG;
        Log.e(tag, getLogInfo(stackele) + msg);
    }

    public static void i(String tag, String msg) {
        if (level > INFO)
            return;
        StackTraceElement stackele = Thread.currentThread().getStackTrace()[3];
        if (TextUtils.isEmpty(tag))
            tag = TAG;
        Log.i(tag, getLogInfo(stackele) + msg);
    }

    public static void d(String tag, String msg) {
        if (level > DEBUG)
            return;
        StackTraceElement stackele = Thread.currentThread().getStackTrace()[3];
        if (TextUtils.isEmpty(tag))
            tag = TAG;
        Log.d(tag, getLogInfo(stackele) + msg);
    }

    public static void v(String tag, String msg) {
        if (level > VERBOSE)
            return;
        StackTraceElement stackele = Thread.currentThread().getStackTrace()[3];
        if (TextUtils.isEmpty(tag))
            tag = TAG;
        Log.v(tag, getLogInfo(stackele) + msg);
    }

    public static void w(String tag, String msg) {
        if (level > WARMING)
            return;
        StackTraceElement stackele = Thread.currentThread().getStackTrace()[3];
        if (TextUtils.isEmpty(tag))
            tag = TAG;
        Log.w(tag, getLogInfo(stackele) + msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static String getLogInfo(StackTraceElement stackele) {
        StringBuilder sb = new StringBuilder();
        sb.append("类名:" + stackele.getClassName());
        sb.append("方法名:" + stackele.getMethodName());
        sb.append("哪一行呢:" + stackele.getLineNumber());
        return sb.toString();
    }
}

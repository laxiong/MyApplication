package com.laxiong.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xiejin on 2016/4/18.
 * Types SpUtils.java
 */
public class SpUtils {
    private static SharedPreferences sp;
    public static final String GESTURE_KEY="patternstring";//手势密码
    private static final String SP_NAME="yitouhang";
    public static final String USER_KEY="username";//手机号
    public static final String USERLOGIN_KEY="userlogin";//登录信息(不是User)
    public static final String FIRST_CONFIRM="isfirst";
    public static final String LOGIN_KEY="logindate";//登录的年月日20160513这样的格式
    public static final String ID_KEY="userid";
    public static SharedPreferences getSp(Context context){
        if(sp==null){
            sp=context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        }
        return sp;
    }
    public static String getStrValue(SharedPreferences s,String name){
        if(s==null)
            return null;
        return s.getString(name,"");
    }
    public static void saveStrValue(SharedPreferences s,String name,String val){
        if(s==null)
            return;
        s.edit().putString(name,val).commit();
    }
    public static int getIntValue(SharedPreferences s,String name){
        if(s==null)
            return 0;
        return s.getInt(name,-1);
    }
    public static void saveIntValue(SharedPreferences s,String name,int val){
        if(s==null)
            return;
        s.edit().putInt(name,val).commit();
    }
}

package com.laxiong.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Activity.MainActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.entity.User;
import com.laxiong.entity.UserLogin;
import com.gongshidai.mistGSD.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiejin on 2016/4/22.
 * Types ValifyUtil.java
 */
public class ValifyUtil {
    public static boolean valifyIdenti(String identi) {
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher pm = idNumPattern.matcher(identi);
        if (pm.matches())
            return true;
        return false;
    }

    public static void toastResult(Context context, String pwd) {
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Pattern p2 = Pattern.compile("\\s*");
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p3 = Pattern.compile(regEx);
        Matcher mat = p.matcher(pwd);
        Matcher mat2 = p2.matcher(pwd);
        Matcher mat3 = p3.matcher(pwd);
        if (mat2.matches()) {
            ToastUtil.customAlert(context, "密码不能包含空格");
        } else if (mat3.matches()) {
            ToastUtil.customAlert(context, "密码不能包含非法字符");
        } else if (!mat.matches()) {
            ToastUtil.customAlert(context, "密码为6~20位数字和密码组合");
        }
    }

    public static boolean valifyPwd(Context context, String pwd) {
//        Pattern pat = Pattern.compile("[\\da-zA-Z]{6,20}");
//        Pattern patno = Pattern.compile(".*\\d.*");
//        Pattern paten = Pattern.compile(".*[a-zA-Z].*");
//        Matcher mat = pat.matcher(pwd);
//        Matcher matno = patno.matcher(pwd);
//        Matcher maten = paten.matcher(pwd);
//        if (matno.matches() && maten.matches() && mat.matches() && !StringUtils.isBlank(pwd)) {
//            return true;
//        }
//        Pattern p=Pattern.compile("[0-9a-zA-Z]{6,20}");
//        return false;
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Pattern p2 = Pattern.compile("\\s*");
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p3 = Pattern.compile(regEx);
        Matcher mat = p.matcher(pwd);
        Matcher mat2 = p2.matcher(pwd);
        Matcher mat3 = p3.matcher(pwd);
//        if(!mat.matches()){
//            ToastUtil.customAlert(context,"密码为6~20位数字和密码组合");
//            return false;
//        }else if(mat2.matches()){
//            ToastUtil.customAlert(context,"密码不能包含空格");
//            return false;
//        }else if(mat3.matches()){
//            ToastUtil.customAlert(context,"密码不能包含非法字符");
//            return false;
//        }else{
//            return true;
//        }
        return mat.matches() && !mat2.matches() && !mat3.matches();
    }

    public static boolean judgeLogin() {
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            return false;
        } else
            return true;
    }

    public static void setEnabled(View view, boolean flag) {
        view.setEnabled(flag);
        view.setBackgroundResource(flag ? R.drawable.button_change_bg_border : R.drawable.button_grey_corner_border);
    }

    public static boolean valifyPhoneNum(String phone) {
        Pattern p = Pattern.compile("^((14[7])|(13[0-9])|(15[^4,\\D])|(18[0,1,2,3,5-9]))\\d{8}$");
        Pattern p2 = Pattern.compile("\\s*");
        Matcher m = p.matcher(phone);
        Matcher m2 = p2.matcher(phone);
        return !StringUtils.isBlank(phone) && m.matches() && !m2.matches();
    }

    public static boolean judgeInit(Context context) {
        SharedPreferences sp = SpUtils.getSp(context);
        String userlogin = SpUtils.getStrValue(sp, SpUtils.USERLOGIN_KEY);
        if (!StringUtils.isBlank(userlogin)) {
            UserLogin user = JSONUtils.parseObject(userlogin, UserLogin.class);
            YiTouApplication.getInstance().setUserLogin(user);
        }
        String phonenum = SpUtils.getStrValue(sp, SpUtils.USER_KEY);
        String pwd = SpUtils.getStrValue(sp,
                SpUtils.GESTURE_KEY);
        if (StringUtils.isBlank(pwd) || StringUtils.isBlank(phonenum)) {
            return false;
        } else {
            return true;
        }
    }
}

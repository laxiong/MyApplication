package com.laxiong.Utils;

import android.view.View;

import com.laxiong.yitouhang.R;

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

    public static boolean valifyPwd(String pwd) {
        Pattern pat = Pattern.compile("[\\da-zA-Z]{6,20}");
        Pattern patno = Pattern.compile(".*\\d.*");
        Pattern paten = Pattern.compile(".*[a-zA-Z].*");
        Matcher mat = pat.matcher(pwd);
        Matcher matno = patno.matcher(pwd);
        Matcher maten = paten.matcher(pwd);
        if (matno.matches() && maten.matches() && mat.matches() && !StringUtils.isBlank(pwd)) {
            return true;
        }
        return false;
    }

    public static void setEnabled(View view, boolean flag) {
        view.setEnabled(flag);
        view.setBackgroundResource(flag ? R.drawable.button_change_bg_border : R.drawable.button_grey_corner_border);
    }
}

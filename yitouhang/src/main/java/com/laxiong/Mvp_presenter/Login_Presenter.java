package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewLogin;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.UserLogin;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/18.
 * Types Login_Presenter.java
 */
public class Login_Presenter {
    private IViewLogin iviewlogin;

    public Login_Presenter(IViewLogin iviewlogin) {
        this.iviewlogin = iviewlogin;
    }

    public void login(final Context context) {
        String phonnum = iviewlogin.getInputPhoneNum();
        String pwd = iviewlogin.getInputPwd();
        if (StringUtils.isBlank(phonnum) || StringUtils.isBlank(pwd)) {
            Toast.makeText(context, "账号密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("phone", phonnum);
        params.put("pwd", pwd);
        HttpUtil.post(InterfaceInfo.LOGIN_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        UserLogin userLogin = JSONUtils.parseObject(response.toString(), UserLogin.class);
                        if (userLogin != null && userLogin.getCode() == 0) {
                            SharedPreferences sp = SpUtils.getSp(context);
                            SpUtils.saveStrValue(sp,SpUtils.USERLOGIN_KEY,response.toString());
                            YiTouApplication.getInstance().setUserLogin(userLogin);
                            iviewlogin.loginsuccess();
                        } else {
                            iviewlogin.loginfailed(userLogin.getMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewlogin.loginfailed(responseString);
            }
        }, true);
    }
}

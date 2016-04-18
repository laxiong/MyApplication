package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewLogin;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.User;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xiejin on 2016/4/18.
 * Types Login_Presenter.java
 */
public class Login_Presenter {
    private IViewLogin iviewlogin;

    public Login_Presenter(IViewLogin iviewlogin) {
        this.iviewlogin = iviewlogin;
    }

    public void login(Context context) {
        String phonnum = iviewlogin.getInputPhoneNum();
        String pwd = iviewlogin.getInputPwd();
        if (StringUtils.isBlank(phonnum) || StringUtils.isBlank(pwd)) {
            Toast.makeText(context, "账号密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("phone", Base64.encode(phonnum.getBytes(), Base64.CRLF));
        params.put("pwd", Base64.encode(pwd.getBytes(), Base64.CRLF));
        HttpUtil.post(InterfaceInfo.LOGIN_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    User user = JSONUtils.parseObject(response.toString(), User.class);
                    if (user != null&&user.getCode()==0) {
                        YiTouApplication.getInstance().setUser(user);
                        iviewlogin.loginsuccess();
                    }else{
                        iviewlogin.loginfailed();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewlogin.loginfailed();
            }
        }, true);
    }
}

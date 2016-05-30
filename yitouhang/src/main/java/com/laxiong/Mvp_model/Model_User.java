package com.laxiong.Mvp_model;

import android.content.Context;
import android.content.SharedPreferences;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Inter_Req.LoginService;
import com.laxiong.Utils.BaseHelper;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.RetrofitUtils;
import com.laxiong.Utils.SpUtils;
import com.laxiong.entity.User;
import com.laxiong.entity.UserLogin;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by xiejin on 2016/5/27.
 * Types Model_User.java
 */
public class Model_User {
    public void loadUserData(int id, final OnLoadBcObjListener listener) {
        if (listener == null)
            return;
        Retrofit retrofit = RetrofitUtils.getRetrofitAuth();
        if (retrofit == null)
            return;
        LoginService ls = retrofit.create(LoginService.class);
        Call<User> call = ls.getUser(id + "");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.body() == null) {
                    listener.onFailure("无响应");
                } else {
                    User user = response.body();
                    if (user != null) {
                        if (user.getCode() == 0)
                            listener.onSuccss(user);
                        else{
                            listener.onFailure(user.getMsg());
                        }
                    } else {
                        listener.onFailure(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onFailure(t.toString());
            }
        });
    }

    public void loadUserLoginData(final Context context, final String phonnum, String pwd, final OnLoadBcObjListener<UserLogin> listener) {
        Retrofit retrofit = RetrofitUtils.getRetrofitUnAuth();
        if (retrofit == null)
            return;
        LoginService ls = retrofit.create(LoginService.class);
        Call<UserLogin> call = ls.login(phonnum, pwd);
        call.enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Response<UserLogin> response, Retrofit retrofit) {
                if (response.body() != null) {
                    UserLogin userlogin = response.body();
                    if (userlogin != null) {
                        if(userlogin.getCode() != 0) {
                            listener.onFailure(userlogin.getMsg());
                            return;
                        }
                        SharedPreferences sp = SpUtils.getSp(context);
                        SpUtils.saveStrValue(sp, SpUtils.USERLOGIN_KEY, BaseHelper.toJSONString(userlogin));
                        SpUtils.saveStrValue(sp, SpUtils.USER_KEY, phonnum);
                        YiTouApplication.getInstance().setUserLogin(userlogin);
                        listener.onSuccss(userlogin);
                    } else {
                        listener.onFailure(response.message());
                    }
                } else {
                    listener.onFailure("无响应");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onFailure(t.toString());
            }
        });
    }
}

package com.laxiong.Mvp_model;

import android.content.Context;
import android.content.Intent;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.entity.User;
import com.loopj.android.network.RequestParams;
import com.squareup.okhttp.FormEncodingBuilder;

/**
 * Created by xiejin on 2016/4/28.
 * Types Model_Invest.java
 */
public class Model_Invest<T, X> extends Model_Basic2 {
    public void loadInvestList(String tag, Context context, FormEncodingBuilder builder, OnLoadBasicListener<T> listener, Class<T> clazz) {
        setListener(listener);
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        reqAuthPostList(context, builder, InterfaceInfo.FUND_URL + user.getId(), tag, clazz);
    }

    public void loadDetaiil(int id, String tag, Context context, FormEncodingBuilder builder, OnLoadBcObjListener<X> listener, Class<X> clazz) {
        setListenerObj(listener);
        reqAuthPostObj(context, builder, InterfaceInfo.RDETAIL_URL + "/" + id, tag, clazz);
    }
}

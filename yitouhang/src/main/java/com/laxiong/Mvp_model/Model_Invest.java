package com.laxiong.Mvp_model;

import android.content.Context;
import android.content.Intent;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.entity.User;
import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types Model_Invest.java
 */
public class Model_Invest<T> extends Model_Basic {
    public void loadInvestList(String tag, Context context, RequestParams params, OnLoadBasicListener<T> listener, Class<T> clazz) {
        setListener(listener);
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        aureqByPost(InterfaceInfo.FUND_URL + user.getId(), context, params, tag, clazz);
    }
}

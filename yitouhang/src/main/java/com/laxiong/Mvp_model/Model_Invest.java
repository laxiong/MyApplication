package com.laxiong.Mvp_model;

import android.content.Context;
import android.content.Intent;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.entity.User;
import com.loopj.android.network.RequestParams;

/**
 * Created by xiejin on 2016/4/28.
 * Types Model_Invest.java
 */
public class Model_Invest<T,X> extends Model_Basic {
    public void loadInvestList(String tag, Context context, RequestParams params, OnLoadBasicListener<T> listener, Class<T> clazz) {
        setListener(listener);
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        aureqByPost(InterfaceInfo.FUND_URL + user.getId(), context, params, tag, clazz);
    }
    public void loadDetaiil(int id,String tag,Context context,RequestParams params,OnLoadBcObjListener<X> listener,Class<X> clazz){
        setListenerObj(listener);
        aureqByPostObj(InterfaceInfo.RDETAIL_URL + "/" + id, context, params, tag, clazz);
    }
}

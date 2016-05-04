package com.laxiong.Mvp_model;

import android.content.Context;

import com.laxiong.Common.InterfaceInfo;

/**
 * Created by xiejin on 2016/5/4.
 * Types Model_Hall.java
 */
public class Model_Hall extends Model_Basic<AtHall> {
    public void loadHallList(Context context, OnLoadBasicListener<AtHall> listener) {
        if (listener == null)
            return;
        setListener(listener);
        reqCommonBackByGet(InterfaceInfo.ACTIVITY_URL, context, null, "list", AtHall.class);
    }
}

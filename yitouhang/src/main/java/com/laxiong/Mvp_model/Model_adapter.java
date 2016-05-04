package com.laxiong.Mvp_model;

import android.content.Context;

import com.laxiong.Common.InterfaceInfo;

/**
 * Created by xiejin on 2016/5/4.
 * Types Model_adapter.java
 */
public class Model_adapter<T> extends Model_Basic{
    public void loadListGet(String url,Context context, OnLoadBasicListener<T> listener,Class<T> clazz) {
        if (listener == null)
            return;
        setListener(listener);
        reqCommonBackByGet(url, context, null, "list",clazz);
    }
    public void loadListPost(String url,Context context, OnLoadBasicListener<T> listener,Class<T> clazz) {
        if (listener == null)
            return;
        setListener(listener);
        reqCommonBackByPost(url, context, null, "list",clazz);
    }
    public void loadListPut(String url,Context context, OnLoadBasicListener<T> listener,Class<T> clazz) {
        if (listener == null)
            return;
        setListener(listener);
        reqCommonBackByPut(url, context, null, "list",clazz);
    }
}

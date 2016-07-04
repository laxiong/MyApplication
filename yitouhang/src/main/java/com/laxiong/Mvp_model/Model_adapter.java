package com.laxiong.Mvp_model;

import android.content.Context;

import com.loopj.android.network.RequestParams;
import com.squareup.okhttp.FormEncodingBuilder;

/**
 * Created by xiejin on 2016/5/4.
 * Types Model_adapter.java
 */
public class Model_adapter<T> extends Model_Basic2 {
    public void loadListGet(String url, Context context, OnLoadBasicListener<T> listener, Class<T> clazz) {
        if (listener == null)
            return;
        setListener(listener);
        reqAuthGetList(context, url, "list", clazz);
    }

    public void loadListPost(String url, Context context, FormEncodingBuilder builder, OnLoadBasicListener<T> listener, Class<T> clazz) {
        if (listener == null)
            return;
        setListener(listener);
        reqCommonPostList(context, builder, url, "list", clazz);
    }

    public void loadListPut(String url, Context context, FormEncodingBuilder builder, OnLoadBasicListener<T> listener, Class<T> clazz) {
        if (listener == null)
            return;
        setListener(listener);
        reqCommonPutList(context, builder, url, "list", clazz);
    }

    public void loadObjGet(String url, Context context, OnLoadBcObjListener<T> listener, String tag, Class<T> clazz) {
        if (listener == null)
            return;
        setListenerObj(listener);
        reqAuthGetObj(context,url,tag, clazz);
    }
}

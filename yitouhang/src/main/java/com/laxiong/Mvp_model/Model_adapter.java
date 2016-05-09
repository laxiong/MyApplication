package com.laxiong.Mvp_model;

import android.content.Context;

import com.laxiong.Common.InterfaceInfo;
import com.loopj.android.http.RequestParams;

/**
 * Created by xiejin on 2016/5/4.
 * Types Model_adapter.java
 */
public class Model_adapter<T> extends Model_Basic{
    public void loadListGet(String url,Context context,RequestParams params,OnLoadBasicListener<T> listener,Class<T> clazz) {
        if (listener == null)
            return;
        setListener(listener);
        reqCommonBackByGet(url, context, params, "list",clazz);
    }
    public void loadListPost(String url,Context context,RequestParams params,OnLoadBasicListener<T> listener,Class<T> clazz) {
        if (listener == null)
            return;
        setListener(listener);
        reqCommonBackByPost(url, context, null, "list", clazz);
    }
    public void loadListPut(String url,Context context,RequestParams params,OnLoadBasicListener<T> listener,Class<T> clazz) {
        if (listener == null)
            return;
        setListener(listener);
        reqCommonBackByPut(url, context, null, "list", clazz);
    }
    public void loadObjGet(String url,Context context,RequestParams params,OnLoadBcObjListener<T> listener,String tag,Class<T> clazz){
        if (listener == null)
            return;
        setListenerObj(listener);
        aureqByGetObj(url, context, params,tag, clazz);
    }
}

package com.laxiong.Mvp_model;

import android.content.Context;
import android.util.Log;

import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types Model_Basic.java
 */
public class Model_Basic<T> {
    private OnLoadBasicListener<T> listener;

    public void setListener(OnLoadBasicListener<T> listener) {
        this.listener = listener;
    }

    //以下是无授权
    public void reqCommonBackByPost(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        HttpUtil.post(url, params, new MyJSONHttp(tag, clazz), true);

    }

    public void reqCommonBackByGet(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        HttpUtil.get(url, params, new MyJSONHttp(tag, clazz), true);
    }

    public void reqCommonBackByPut(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        HttpUtil.put(url, params, new MyJSONHttp(tag, clazz), true);
    }

    // 以下是有授权
    public void aureqByPost(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.post(url, params, new MyJSONHttp(tag, clazz), authori);
    }

    public void aureqByGet(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.get(url, params, new MyJSONHttp(tag, clazz), authori);
    }

    public void aureqByPut(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.put(url, params, new MyJSONHttp(tag, clazz), authori);
    }

    class MyJSONHttp extends JsonHttpResponseHandler {
        private String tag;
        private Class<T> clazz;

        public MyJSONHttp(String tag, Class<T> clazz) {
            this.tag = tag;
            this.clazz = clazz;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            if (response != null) {
                try {
                    if (response.getInt("code") == 0) {
                        Log.i("kk",response.toString());
                        List<T> list = null;
                        if (StringUtils.isBlank(tag))
                            list = JSONUtils.parseArray(response.toString(), clazz);
                        else
                            list = JSONUtils.parseArray(response.getJSONArray(tag).toString(), clazz);
                        listener.loadOnSuccess(list);
                    } else {
                        listener.loadOnFailure(response.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.loadOnFailure(e.toString());
                }
            } else {
                listener.loadOnFailure("无响应");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            listener.loadOnFailure(responseString);
        }
    }
}

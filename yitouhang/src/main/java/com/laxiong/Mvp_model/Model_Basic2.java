package com.laxiong.Mvp_model;

import android.content.Context;

import com.laxiong.Basic.Callback;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.StringUtils;
import com.squareup.okhttp.FormEncodingBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiejin on 2016/6/29.
 * Types Model_Basic2.java
 * tag是用来标识一个数组名称或者对象名称的
 */
public class Model_Basic2<T> {
    private OnLoadBasicListener<T> listener;
    private OnLoadBcObjListener<T> listener2;
    private Context context;

    public void setListener(OnLoadBasicListener<T> listener) {
        this.listener = listener;
    }

    public void setListenerObj(OnLoadBcObjListener<T> listener2) {
        this.listener2 = listener2;
    }
    //get请求无需授权object
    public void reqCommonGetObj(Context context,String url, String tag, Class<T> clazz){
        this.context = context;
        HttpUtil2.get(url, new MyCallbackObj(tag, clazz));
    }
    //get请求有授权object
    public void reqAuthGetObj(Context context,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.get(url,new MyCallbackObj(tag,clazz),CommonReq.getAuthori(context));
    }
    //get请求无需授权list
    public void reqCommonGetList(Context context,String url, String tag, Class<T> clazz){
        this.context = context;
        HttpUtil2.get(url, new MyCallbackList(tag, clazz));
    }
    //get请求有授权list
    public void reqAuthGetList(Context context,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.get(url,new MyCallbackList(tag,clazz),CommonReq.getAuthori(context));
    }
    //put请求无需授权
    public void reqCommonPutObj(Context context,FormEncodingBuilder builder,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.put(url, builder, new MyCallbackObj(tag, clazz));
    }
    //put 请求有授权
    public void reqAuthPutObj(Context context,FormEncodingBuilder builder,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.put(url, builder, new MyCallbackObj(tag, clazz), CommonReq.getAuthori(context));
    }
    //put请求无需授权List
    public void reqCommonPutList(Context context,FormEncodingBuilder builder,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.put(url, builder, new MyCallbackList(tag, clazz));
    }
    //put 请求有授权list
    public void reqAuthPutList(Context context,FormEncodingBuilder builder,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.put(url,builder,new MyCallbackList(tag,clazz),CommonReq.getAuthori(context));
    }
    //post 请求无授权
    public void reqCommonPostObj(Context context,FormEncodingBuilder builder,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.post(url, builder, new MyCallbackObj(tag, clazz));
    }
    //post 请求有授权
    public void reqAuthPostObj(Context context,FormEncodingBuilder builder,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.post(url, builder, new MyCallbackObj(tag, clazz), CommonReq.getAuthori(context));
    }
    //post 请求无授权List
    public void reqCommonPostList(Context context,FormEncodingBuilder builder,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.post(url, builder, new MyCallbackList(tag, clazz));
    }
    //post 请求有授权List
    public void reqAuthPostList(Context context,FormEncodingBuilder builder,String url,String tag,Class<T> clazz){
        this.context=context;
        HttpUtil2.post(url,builder,new MyCallbackList(tag,clazz),CommonReq.getAuthori(context));
    }
    //请求反馈list
    public class MyCallbackList extends Callback{
        private String tag;
        private Class<T> clazz;

        public MyCallbackList(String tag, Class<T> clazz) {
            this.tag = tag;
            this.clazz = clazz;
        }

        @Override
        public void onResponse2(JSONObject response) {
            if (response != null) {
                try {
                    if (response.getInt("code") == 0) {
                        List<T> list = null;
                        if (StringUtils.isBlank(tag))
                            list = JSONUtils.parseArray(response.toString(), clazz);
                        else
                            list = JSONUtils.parseArray(response.getJSONArray(tag).toString(), clazz);
                        listener.loadOnSuccess(list);
                    } else {
                        if (response.getInt("code") == 401) {
                            CommonReq.showReLoginDialog(context);
                        } else
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
        public void onFailure(String msg) {
            listener.loadOnFailure(msg);
        }
    }
    //请求反馈object
    public class MyCallbackObj extends Callback {
        private String tag;
        private Class<T> clazz;

        public MyCallbackObj(String tag, Class<T> clazz) {
            this.tag = tag;
            this.clazz = clazz;
        }

        @Override
        public void onResponse2(JSONObject response) {
            if (response != null) {
                try {
                    if (response.getInt("code") == 0) {
                        T obj = null;
                        if (tag == null || StringUtils.isBlank(tag))
                            obj = JSONUtils.parseObject(response.toString(), clazz);
                        else
                            obj = JSONUtils.parseObject(response.getJSONObject(tag).toString(), clazz);
                        listener2.onSuccss(obj);
                    } else {
                        if (response.getInt("code") == 401) {
                            CommonReq.showReLoginDialog(context);
                        } else
                            listener2.onFailure(response.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener2.onFailure(e.toString());
                }
            } else {
                listener2.onFailure("无响应");
            }
        }

        @Override
        public void onFailure(String msg) {
            listener2.onFailure(msg);
        }
    }
}

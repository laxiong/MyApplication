package com.laxiong.Mvp_model;

import android.content.Context;

import com.laxiong.Basic.Callback;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types Model_Basic.java
 * 这是model的基础类,可以通过继承它完成一些延伸
 */
public class Model_Basic<T> {
    private OnLoadBasicListener<T> listener;
    private OnLoadBcObjListener<T> listener2;
    private Context context;

    public void setListener(OnLoadBasicListener<T> listener) {
        this.listener = listener;
    }

    public void setListenerObj(OnLoadBcObjListener<T> listener2) {
        this.listener2 = listener2;
    }

    //以下是无授权

    /**
     * 以req开头的是不需要授权的
     *
     * @param url     地址
     * @param context
     * @param params  参数
     * @param tag     JSON数组的对象名称如果为null的话默认以整个response作为parse对象 解析list对象
     * @param clazz   类的Class对象
     */
    public void reqCommonBackByPost(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        this.context = context;
        HttpUtil.post(url, params, new MyJSONHttp(tag, clazz), true);

    }

    /**
     * 以obj结尾的是解析出一个实体类对象 而不是一个list,其他同上
     *
     * @param url
     * @param params
     * @param tag
     * @param clazz
     */
    public void reqCommonGetObj(String url, RequestParams params, String tag, Class<T> clazz) {
        this.context = context;
        HttpUtil.get(url, params, new MyJSONHttp2(tag, clazz));
    }

    public void reqCommonGetObj(String url, String tag, Class<T> clazz) {
        this.context = context;
//        HttpUtil.get(url, params, new MyJSONHttp2(tag, clazz));
        HttpUtil2.get(url, new MyCallback(tag, clazz));
    }

    public class MyCallback extends Callback {
        private String tag;
        private Class<T> clazz;

        public MyCallback(String tag, Class<T> clazz) {
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

    public void reqCommonBackByGet(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        this.context = context;
        HttpUtil.get(url, params, new MyJSONHttp(tag, clazz), true);
    }

    public void reqCommonBackByPut(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        this.context = context;
        HttpUtil.put(url, params, new MyJSONHttp(tag, clazz), true);
    }

    // 以下是有授权
    public void aureqByPost(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        this.context = context;
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.post(url, params, new MyJSONHttp(tag, clazz), authori);
    }

    public void aureqByGet(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        this.context = context;
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.get(url, params, new MyJSONHttp(tag, clazz), authori);
    }

    public void aureqByPostObj(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        this.context = context;
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.post(url, params, new MyJSONHttp2(tag, clazz), authori);
    }

    public void aureqByGetObj(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        this.context = context;
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.get(url, params, new MyJSONHttp2(tag, clazz), authori);
    }

    public void aureqByPut(String url, Context context, RequestParams params, String tag, Class<T> clazz) {
        this.context = context;
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
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            listener.loadOnFailure(responseString);
        }
    }

    class MyJSONHttp2 extends JsonHttpResponseHandler {
        private String tag;
        private Class<T> clazz;

        public MyJSONHttp2(String tag, Class<T> clazz) {
            this.tag = tag;
            this.clazz = clazz;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
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
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            listener2.onFailure(responseString);
        }
    }
}

package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/22.
 * Types CommonReq_Presenter.java
 */
public class CommonReq_Presenter {
    private IViewCommonBack iviewback;

    public CommonReq_Presenter(IViewCommonBack iviewback) {
        this.iviewback = iviewback;
    }

    //以下是无授权
    public void reqCommonBackByPost(String url, Context context, RequestParams params) {
        HttpUtil.post(url, params, new MyJSONHttp(), true);

    }

    public void reqCommonBackByGet(String url, Context context, RequestParams params) {
        HttpUtil.get(url, params, new MyJSONHttp(), true);
    }

    public void reqCommonBackByPut(String url, Context context, RequestParams params) {
        HttpUtil.put(url, params, new MyJSONHttp(), true);
    }

    // 以下是有授权
    public void aureqByPost(String url, Context context, RequestParams params) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.post(url, params, new MyJSONHttp(), authori);
    }

    public void aureqByGet(String url, Context context, RequestParams params) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.get(url, params, new MyJSONHttp(), authori);
    }

    public void aureqByPut(String url, Context context, RequestParams params) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.put(url, params, new MyJSONHttp(), authori);
    }

    class MyJSONHttp extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            if (response != null) {
                try {
                    if (response.getInt("code") == 0) {
                        iviewback.reqbackSuc();
                    } else {
                        iviewback.reqbackFail(response.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    iviewback.reqbackFail(e.toString());
                }
            } else {
                iviewback.reqbackFail("无响应");
            }
        }
    }

}

package com.laxiong.Mvp_model;

import android.app.DownloadManager;
import android.content.Context;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types Model_Order.java
 */
public class Model_Order {
    public void loadOrderlist(Context context, RequestParams params, final OnLoadOrder listener) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori) || listener == null)
            return;
        HttpUtil.post(InterfaceInfo.SHOPORDER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {//成功获取
                            List<Order> list = JSONUtils.parseArray(response.getJSONArray("order").toString(), Order.class);
                            listener.onSuccess(list == null ? new ArrayList<Order>() : list);
                        } else {
                            listener.onFailure(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailure(e.toString());
                    }
                } else {
                    listener.onFailure("无响应");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.onFailure(responseString);
            }
        }, authori);
    }

    public interface OnLoadOrder {
        public void onSuccess(List<Order> list);

        public void onFailure(String msg);
    }
}

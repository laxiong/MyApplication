package com.laxiong.Mvp_model;

import android.content.Context;

import com.laxiong.Adapter.RedPaper;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.UserLogin;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiejin on 2016/4/25.
 * Types Model_RedPaper.java
 */
public class Model_RedPaper {
    private OnLoadPaperListener listener;

    public void loadAllList(final Context context, OnLoadPaperListener fuck) {
        this.listener = fuck;
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori) || listener == null)
            return;
        UserLogin login = YiTouApplication.getInstance().getUserLogin();
        RequestParams params = new RequestParams();
        params.put("id", login.getToken_id());
        params.put("type", "all");
        HttpUtil.get(InterfaceInfo.REDPAPER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            String jsonstr = response.getJSONArray("list").toString();
                            List<RedPaper> list = JSONUtils.parseArray(jsonstr, RedPaper.class);
                            listener.onSuccess(list, false, true);
                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(context);
                            } else
                                listener.onFailure(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailure(e.toString());
                    }
                } else {
                    listener.onFailure("出错无响应");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.onFailure(responseString);
            }
        }, authori);
    }

    public void loadPaperList(final boolean isused, final Context context, OnLoadPaperListener fuck) {
        this.listener = fuck;
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori) || listener == null)
            return;
        UserLogin login = YiTouApplication.getInstance().getUserLogin();
        RequestParams params = new RequestParams();
        params.put("id", login.getToken_id());
        params.put("type", isused ? "used" : "use");
        HttpUtil.get(InterfaceInfo.REDPAPER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            String jsonstr = response.getJSONArray("list").toString();
                            List<RedPaper> list = JSONUtils.parseArray(jsonstr, RedPaper.class);
                            listener.onSuccess(list, isused, false);
                        } else {
                            if (response.getInt("code") == 401) {
                                listener.onFailure("请求失败");
                                CommonReq.showReLoginDialog(context);
                            }
                            else
                                listener.onFailure(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailure(e.toString());
                    }
                } else {
                    listener.onFailure("出错无响应");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.onFailure(responseString);
            }
        }, authori);
    }

    public interface OnLoadPaperListener {
        public void onSuccess(List<RedPaper> list, boolean isused, boolean isAll);

        public void onFailure(String msg);
    }
}

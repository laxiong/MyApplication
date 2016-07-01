package com.laxiong.Mvp_model;

import android.content.Context;
import android.util.Log;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.network.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiejin on 2016/4/25.
 * Types Model_card.java
 */
public class Model_card {
    private OnLoadBankCardListener listener;

    public void loadBankList(Context context, final OnLoadBankCardListener listener) {
        this.listener = listener;
        HttpUtil.get(InterfaceInfo.BANKLIST_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            List<BindCardItem> list = JSONUtils.parseArray(response.getJSONArray("list").toString(), BindCardItem.class);
                            listener.onSuccess(list);
                        } else {
                            listener.onFailure(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailure(e.toString());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.onFailure(responseString);
            }
        });
    }
    public void loadBankCard(Context context,final OnLoadBankCardListener listener) {
        this.listener = listener;
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        HttpUtil.get(InterfaceInfo.MYCARD_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            Log.i("kk","look card"+response.toString());
                            BankCard card = JSONUtils.parseObject(response.toString(), BankCard.class);
                            listener.onSuccess(card);
                        } else {
                            listener.onFailure(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailure(e.toString());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.onFailure(responseString);
            }
        }, authori);
    }

    public interface OnLoadBankCardListener {
        public void onSuccess(BankCard card);

        public void onFailure(String msg);

        public void onSuccess(List<BindCardItem> listitem);

        public void onFailureList(String msg);
    }
}

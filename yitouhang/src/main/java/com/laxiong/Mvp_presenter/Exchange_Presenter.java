package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.laxiong.Activity.ChangeCountActivity;
import com.laxiong.Activity.ExChangeActivity;
import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewExchange;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.UserLogin;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/20.
 * Types Exchange_Presenter.java
 */
public class Exchange_Presenter {
    private IViewExchange iviewexc;

    public Exchange_Presenter(IViewExchange iviewexc) {
        this.iviewexc = iviewexc;
    }

    public void exchange(final Context context, String num, int id, String pwd) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        RequestParams params = new RequestParams();
        params.put("amount", Integer.parseInt(num));
        params.put("product", id);
        params.put("type", Constants.ReqEnum.BUY);
        params.put("pay_pwd", pwd);
        HttpUtil.post(InterfaceInfo.SHOPORDER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewexc.exchangeSuc();
                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(context);
                            } else
                                iviewexc.exchangeFail(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewexc.exchangeFail(e.toString());
                    }
                } else {
                    iviewexc.exchangeFail("出错");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewexc.exchangeFail(responseString);
            }
        }, authori);
    }
}

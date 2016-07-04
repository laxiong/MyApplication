package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Basic.Callback;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewExchange;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;
import com.squareup.okhttp.FormEncodingBuilder;

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
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("amount", num + "");
        builder.add("product", id + "");
        builder.add("type", Constants.ReqEnum.BUY + "");
        builder.add("pay_pwd", pwd + "");
        HttpUtil2.post(InterfaceInfo.SHOPORDER_URL, builder, new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
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
            public void onFailure(String msg) {
                iviewexc.exchangeFail(msg);
            }
        }, authori);
    }
}

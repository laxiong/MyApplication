package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewSecurity;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/22.
 * Types SecurityVali_Presenter.java
 */
public class SecurityVali_Presenter {
    private IViewSecurity iviewsecure;

    public SecurityVali_Presenter(IViewSecurity iviewback) {
        this.iviewsecure = iviewback;
    }

    public void valifySecurity(final Context context) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        String name = iviewsecure.getName();
        String identi = iviewsecure.getIdenti();
        String code = iviewsecure.getPwd();
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.CPHONE.getVal());
        params.put("pay_pwd", code);
        params.put("realname", name);
        params.put("idc", identi);
        HttpUtil.post(InterfaceInfo.VERIFY_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewsecure.setToken(response.getString("token"));
                            iviewsecure.reqbackSuc(null);
                        } else {
                            if (response.getInt("code") == 401)
                                CommonReq.showReLoginDialog(context);
                            else
                                iviewsecure.reqbackFail(response.getString("msg"), null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewsecure.reqbackFail(e.toString(), null);
                    }
                } else {
                    iviewsecure.reqbackFail("无响应", null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewsecure.reqbackFail(responseString, null);
            }
        }, authori);
    }
}

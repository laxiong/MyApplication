package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewExit;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.entity.UserLogin;
import com.loopj.android.network.Base64;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/18.
 * Types Exit_Presenter.java
 */
public class Exit_Presenter {
    private IViewExit iviewexit;

    public Exit_Presenter(IViewExit iviewexit) {
        this.iviewexit = iviewexit;
    }

    public void exit(final Context context) {
        UserLogin userLogin = YiTouApplication.getInstance().getUserLogin();
        if (userLogin == null)
            return;
        RequestParams params = new RequestParams();
        String str = userLogin.getToken_id() + ":" + userLogin.getToken();
        String author = Base64.encodeToString(str.getBytes(), Base64.CRLF);
        HttpUtil.get(InterfaceInfo.LOGINOUT_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response != null && response.getInt("code") == 0) {
                        iviewexit.logoutsuccess();
                    } else {
                        if (response.getInt("code") == 401) {
                            CommonReq.showReLoginDialog(context);
                        } else
                            iviewexit.logoutfailed(response.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewexit.logoutfailed(responseString);
            }
        }, author);
    }
}

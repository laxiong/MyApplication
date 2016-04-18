package com.laxiong.Mvp_presenter;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewExit;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.entity.User;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

    public void exit() {
        User user = YiTouApplication.getInstance().getUser();
        if (user == null)
            return;
        RequestParams params = new RequestParams();
        String authori = user.getToken_id() + ":" + user.getToken();
        params.put("Authorization", Base64.encode(authori.getBytes(), Base64.CRLF));
        HttpUtil.get(InterfaceInfo.LOGINOUT_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response != null && response.getInt("code") == 0) {
                        iviewexit.logoutsuccess();
                    } else {
                        iviewexit.logoutfailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewexit.logoutfailed();
            }
        }, true);
    }
}

package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.text.TextUtils;

import com.laxiong.Basic.Callback;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewExit;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil2;

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
        String autho = CommonReq.getAuthori(context);
        if (TextUtils.isEmpty(autho)) return;
        HttpUtil2.get(InterfaceInfo.LOGINOUT_URL, new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
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
            public void onFailure(String msg) {
                iviewexit.logoutfailed(msg);
            }
        }, autho);
    }
}

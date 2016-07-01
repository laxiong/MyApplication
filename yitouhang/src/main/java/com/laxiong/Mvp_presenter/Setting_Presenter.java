package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewSetting;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.User;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/18.
 * Types Setting_Presenter.java
 */
public class Setting_Presenter {
    private IViewSetting iviewset;

    public Setting_Presenter(IViewSetting iviewset) {
        this.iviewset = iviewset;
    }

    public void reqSetNickName(final Context context) {
        final String nickname = iviewset.getNickName();
        if (!valify((nickname)))
            return;
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori))
            return;
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.NICK.getVal());
        params.put("nickname", nickname);
        HttpUtil.put(InterfaceInfo.USER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response != null) {
                        if (response.getInt("code") == 0) {
                            User user = YiTouApplication.getInstance().getUser();
                            if (user != null)
                                user.setNickname(nickname);
                            iviewset.setNickSuccess();
                        } else {
                            if (response.getInt("code") == 401)
                                CommonReq.showReLoginDialog(context);
                            else
                                iviewset.setNickFailure(response.getString("msg"));
                        }
                    } else {
                        iviewset.setNickFailure(response.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    iviewset.setNickFailure(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewset.setNickFailure(responseString);
            }
        }, authori);
    }

    private boolean valify(String nickname) {
        if (StringUtils.isBlank(nickname)) {
            return false;
        }
        return true;
    }
}

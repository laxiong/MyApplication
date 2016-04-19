package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.laxiong.Activity.ChangeCountActivity;
import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewSetting;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.User;
import com.laxiong.entity.UserLogin;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

    public void reqSetNickName(Context context) {
        final String nickname = iviewset.getNickName();
        if (!valify((nickname)))
            return;
        UserLogin user = YiTouApplication.getInstance().getUserLogin();
        if (user == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return;
        }
        int tokenid = user.getToken_id();
        String token = user.getToken();
        if (tokenid == -1) {
            Intent intent = new Intent(context, ChangeCountActivity.class);
            context.startActivity(intent);
            return;
        }
        final String str = tokenid + ":" + token;
        String autori = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.NICK.getVal());
        params.put("nickname", nickname);
        HttpUtil.put(InterfaceInfo.USER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response != null) {
                        if(response.getInt("code") == 0) {
                            User user = YiTouApplication.getInstance().getUser();
                            if (user != null)
                                user.setNickname(nickname);
                            iviewset.setNickSuccess();
                        }else{
                            iviewset.setNickFailure(response.getString("msg"));
                        }
                    }else{
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
        }, autori);
    }

    private boolean valify(String nickname) {
        if (StringUtils.isBlank(nickname)) {
            return false;
        }
        return true;
    }
}

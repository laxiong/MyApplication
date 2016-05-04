package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.laxiong.Activity.ChangeCountActivity;
import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewCount;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
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
 * Types UserCount_Presenter.java
 */
public class UserCount_Presenter {
    private IViewCount iviewcount;

    public UserCount_Presenter(IViewCount iviewcount) {
        this.iviewcount = iviewcount;
    }
    public void reqUserCountMsg(Context context) {
        SharedPreferences sp = SpUtils.getSp(context);
        String userlogins = SpUtils.getStrValue(sp, SpUtils.USERLOGIN_KEY);
        UserLogin userlogin=null;
        if (!StringUtils.isBlank(userlogins)) {
            userlogin = JSONUtils.parseObject(userlogins, UserLogin.class);
            YiTouApplication.getInstance().setUserLogin(userlogin);
        }else{
            return;
        }
        int tokenid = userlogin.getToken_id();
        String token = userlogin.getToken();
        final String str = tokenid + ":" + token;
        String autori = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        User user = YiTouApplication.getInstance().getUser();
        if (user != null) {
            iviewcount.getCountMsgSuc();
            return;
        }
        HttpUtil.get(InterfaceInfo.GETCOUNT_URL + tokenid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if(response.getInt("code")==0) {
                            User user = JSONUtils.parseObject(response.toString(), User.class);
                            YiTouApplication.getInstance().setUser(user);
                            iviewcount.getCountMsgSuc();
                        }else{
                            iviewcount.getCountMsgFai();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewcount.getCountMsgFai();
            }
        }, autori);
    }
}

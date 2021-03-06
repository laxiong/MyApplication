package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewCount;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.User;
import com.laxiong.entity.UserLogin;
import com.loopj.android.network.Base64;
import com.loopj.android.network.JsonHttpResponseHandler;

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
    public void reqUserCountMsg(final Context context) {
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
        HttpUtil2.get(InterfaceInfo.GETCOUNT_URL + tokenid, new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            User user = JSONUtils.parseObject(response.toString(), User.class);
                            YiTouApplication.getInstance().setUser(user);
                            iviewcount.getCountMsgSuc();
                        } else {
                            if (response.getInt("code") == 401)
                                CommonReq.showReLoginDialog(context);
                            iviewcount.getCountMsgFai();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(String msg) {
                iviewcount.getCountMsgFai();
            }
        }, autori);
    }
}

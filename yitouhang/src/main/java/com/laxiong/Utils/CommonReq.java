package com.laxiong.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.laxiong.Activity.ChangeCountActivity;
import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.entity.User;
import com.laxiong.entity.UserLogin;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.gongshidai.mistGSD.R;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/20.
 * Types CommonReq.java
 */
public class CommonReq {
    public static void recordLogin(Context context){
        String autori= CommonReq.getAuthori(context);
        HttpUtil.post(InterfaceInfo.RECORDLOGIN_URL,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        },autori);
    }
    public static void reqUserMsg(Context context) {
        UserLogin userlogin = YiTouApplication.getInstance().getUserLogin();
        if (userlogin == null || StringUtils.isBlank(userlogin.getToken_id() + "") || StringUtils.isBlank(userlogin.getToken())) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return;
        }
        int tokenid = userlogin.getToken_id();
        String token = userlogin.getToken();
        User user = YiTouApplication.getInstance().getUser();
        if (user != null)
            return;
        final String str = tokenid + ":" + token;
        String autori = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        HttpUtil.get(InterfaceInfo.GETCOUNT_URL + tokenid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    User user = JSONUtils.parseObject(response.toString(), User.class);
                    YiTouApplication.getInstance().setUser(user);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        }, autori);
    }
    public static String getAuthori(Context context){
        UserLogin user = YiTouApplication.getInstance().getUserLogin();
        if (user == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return"";
        }
        int tokenid = user.getToken_id();
        String token = user.getToken();
        if (tokenid == -1) {
            Intent intent = new Intent(context, ChangeCountActivity.class);
            context.startActivity(intent);
            return"";
        }
        final String str = tokenid + ":" + token;
        String autori = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        return autori;
    }
}

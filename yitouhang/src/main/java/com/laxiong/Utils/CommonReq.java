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
import com.laxiong.yitouhang.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/20.
 * Types CommonReq.java
 */
public class CommonReq {
    public static void reqLoadImageView(String url, final ImageView iv) {
        HttpUtil.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody != null) {
                    if (responseBody == null) {
                        setImageFailure(R.drawable.ic_launcher, iv);
                    } else {
                        Bitmap bm = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                        if (bm == null)
                            setImageFailure(R.drawable.ic_launcher, iv);
                        else
                            setImageSuccess(bm, iv);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                setImageFailure(R.drawable.gongshi_mr, iv);
            }
        });
    }
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
    public static void setImageFailure(int id, ImageView iv) {
        iv.setImageResource(id);
    }

    public static void setImageSuccess(Bitmap bm, ImageView iv) {
        iv.setImageBitmap(bm);
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

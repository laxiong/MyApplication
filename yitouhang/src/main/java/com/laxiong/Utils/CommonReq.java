package com.laxiong.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.laxiong.Activity.ChangeCountActivity;
import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.entity.User;
import com.laxiong.entity.UserLogin;
import com.loopj.android.network.Base64;
import com.loopj.android.network.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/20.
 * Types CommonReq.java
 * 请求的工具类
 */
public class CommonReq {
    /**
     * 用来记录登录
     *
     * @param context
     */
    private static AlertDialog.Builder dialog;
    public static void recordLogin(final Context context) {
        String autori = CommonReq.getAuthori(context);
        HttpUtil.post(InterfaceInfo.RECORDLOGIN_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response!=null&&response.getInt("code")==401){
                        showReLoginDialog(context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        }, autori);
    }

    /**
     * 当账号在别处登录的时候进行重新登录
     *
     * @param context
     */
    public static void showReLoginDialog(final Context context) {
        User.clearLogin(context);
        if(dialog!=null) {
            dialog=null;
            return;
        }
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle("提示");
        dialog.setMessage("账号在别处登录,是否重新登录?");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean flag = context instanceof Activity;
                Intent intent = new Intent(context, ChangeCountActivity.class);
                if (flag) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 用来获取User信息的请求
     *
     * @param context
     */
    public static void reqUserMsg(final Context context) {
        UserLogin userlogin = YiTouApplication.getInstance().getUserLogin();
        if (userlogin == null || StringUtils.isBlank(userlogin.getToken_id() + "") || StringUtils.isBlank(userlogin.getToken())) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return;
        }
        int tokenid = userlogin.getToken_id();
        String token = userlogin.getToken();
        final String str = tokenid + ":" + token;
        String autori = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        HttpUtil.get(InterfaceInfo.GETCOUNT_URL + tokenid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            User user = JSONUtils.parseObject(response.toString(), User.class);
                            YiTouApplication.getInstance().setUser(user);
                        }else if(response.getInt("code")==401){
                            showReLoginDialog(context);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        }, autori);
    }

    /**
     * 获取授权
     *
     * @param context
     * @return
     */
    public static String getAuthori(Context context) {
        UserLogin user = YiTouApplication.getInstance().getUserLogin();
        if (user == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return "";
        }
        int tokenid = user.getToken_id();
        String token = user.getToken();
        if (tokenid == -1) {
            Intent intent = new Intent(context, ChangeCountActivity.class);
            context.startActivity(intent);
            return "";
        }
        final String str = tokenid + ":" + token;
        String autori = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        return autori;
    }
}

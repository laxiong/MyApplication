package com.laxiong.Mvp_presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewChangePwd;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.UserLogin;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/19.
 * Types Password_Presenter.java
 */
public class Password_Presenter {
    private IViewChangePwd  iviewchange;
    public Password_Presenter(IViewChangePwd iviewchange){
        this.iviewchange=iviewchange;
    }
    public void reqChangePwd(Context context){
        if(iviewchange==null)
            return;
        String oldpwd=iviewchange.getOldPwd();
        String newpwd=iviewchange.getNewPwd();
        if(StringUtils.isBlank(oldpwd)||StringUtils.isBlank(newpwd))
            return;
        RequestParams params=new RequestParams();
        params.put("type","edpwd");
        params.put("old_pwd",oldpwd);
        params.put("pwd",newpwd);
        params.put("repwd",newpwd);
        UserLogin userlogin= YiTouApplication.getInstance().getUserLogin();
        if (userlogin == null || StringUtils.isBlank(userlogin.getToken_id() + "") || StringUtils.isBlank(userlogin.getToken())) {
            Intent intent=new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
        int tokenid=userlogin.getToken_id();
        String token=userlogin.getToken();
        String str=tokenid+":"+token;
        String authori= Base64.encodeToString(str.getBytes(),Base64.NO_WRAP);
        HttpUtil.put(InterfaceInfo.USER_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response!=null&&response.getInt("code")==0){
                        iviewchange.updateSuc();
                    }else{
                        iviewchange.updateFailure(response.getString("msg"));
                    }
                } catch (JSONException e) {
                    iviewchange.updateFailure(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewchange.updateFailure(responseString);
            }
        },authori);
    }
}

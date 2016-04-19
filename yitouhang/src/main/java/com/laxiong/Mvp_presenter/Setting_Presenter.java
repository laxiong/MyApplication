package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.laxiong.Activity.ChangeCountActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewSetting;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.User;
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
    public Setting_Presenter(IViewSetting iviewset){
        this.iviewset=iviewset;
    }
    public void reqSetNickName(Context context){
        final String nickname=iviewset.getNickName();
        if(!valify((nickname)))
            return;
        SharedPreferences sp= SpUtils.getSp(context);
        int tokenid= SpUtils.getIntValue(sp, SpUtils.TOKENID_KEY);
        String token=SpUtils.getStrValue(sp, SpUtils.TOKEN_KEY);
        if(tokenid==-1){
            Intent intent=new Intent(context, ChangeCountActivity.class);
            context.startActivity(intent);
            return;
        }
        final String str=tokenid+":"+token;
        String autori= Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        RequestParams params=new RequestParams();
        params.put("type","nick");
        params.put("nickname",nickname);
        HttpUtil.put(InterfaceInfo.USER_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response!=null&&response.getInt("code")==0){
                        User user=YiTouApplication.getInstance().getUser();
                        if(user!=null)
                            user.setNickname(nickname);
                        iviewset.setNickSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewset.setNickFailure(responseString);
            }
        },autori);
    }
    private boolean valify(String nickname){
        if(StringUtils.isBlank(nickname)){
            return false;
        }
        return true;
    }
}

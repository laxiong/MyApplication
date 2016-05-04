package com.laxiong.Mvp_model;

import android.content.Context;
import android.util.Log;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiejin on 2016/5/3.
 * Types Model_Welcome.java
 */
public class Model_Welcome{
    public void loadWelList(Context context, final OnLoadWelcomeListener listener){
        HttpUtil.get(InterfaceInfo.WELCOME_URL,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(response!=null){
                    try {
                        if(response.getInt("code")==0){
                            WelcImg wel=JSONUtils.parseObject(response.getJSONObject("list").toString(),WelcImg.class);
                            listener.loadWelcomeSuc(wel);
                        }else{
                            listener.loadOnFailure(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.loadOnFailure(e.toString());
                    }
                }else{
                    listener.loadOnFailure("无响应");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.loadOnFailure(responseString);
            }
        });
    }
    public interface OnLoadWelcomeListener {
        public void loadWelcomeSuc(WelcImg wel);
        public void loadOnFailure(String msg);
    }
}

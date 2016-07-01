package com.laxiong.Basic;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by xiejin on 2016/6/13.
 * Types Callback.java
 */
public abstract class Callback implements com.squareup.okhttp.Callback{
    private Handler handler=new Handler(Looper.getMainLooper());
    @Override
    public void onFailure(Request request, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(final Response response) throws IOException {
//        Looper.prepare();
        if(response.isSuccessful()) {
            try {
                final JSONObject json = new JSONObject(response.body().string());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResponse2(json);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                onFailure("参数异常");
            } catch (IOException e) {
                e.printStackTrace();
                onFailure("流异常");
            }
        }else{
            onFailure("请求失败");
        }
//        Looper.loop();
    }
    public abstract void onResponse2(JSONObject response);
    public abstract void onFailure(String msg);
}

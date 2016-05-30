package com.laxiong.Inter_Req;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.laxiong.Activity.YibiDetailActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.CommonUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by xiejin on 2016/5/27.
 * Types MyInterceptor.java
 */
public class MyInterceptor implements Interceptor{
    private boolean isauth;
    public MyInterceptor(boolean isauth){
        this.isauth=isauth;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder nb = chain.request().newBuilder()
                .addHeader("imei-Agent", getIdentifier())
                .addHeader("client","android")
                .addHeader("ACCEPT", "*/*")
                .addHeader("Bear", "9cfe953a2a6aef02aaf6971bc7ca62c3f0167e67");
        if(isauth){
            String auth=CommonReq.getAuthori(YiTouApplication.getInstance().getApplicationContext());
            if(!TextUtils.isEmpty(auth))
                nb.addHeader("Authorization", "Basic " + auth);
        }
        return chain.proceed(nb.build());
    }
    private static String getIdentifier() {
        String identifier = null;
        Context context = YiTouApplication.getInstance().getApplicationContext();
        TelephonyManager tm =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null
                || identifier.length() == 0
                || identifier.equals("000000000000000")
                )
            identifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }
    /**
     * 检测网络
     **/
    public static void checkNet() {
        /*
         * check network state
		 */
        try {
            if (!CommonUtils.checkNetworkState(YiTouApplication.getInstance())) {
                Toast.makeText(YiTouApplication.getInstance(), "当前没有网络，请检查网络后再试", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
        }
    }
}

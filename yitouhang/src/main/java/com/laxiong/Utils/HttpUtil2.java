package com.laxiong.Utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class HttpUtil2 {
    public static OkHttpClient client=new OkHttpClient();
    public static Request.Builder request=new Request.Builder();
    public final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static void put(String urlString,FormEncodingBuilder builder,Callback callback, String authorization) {
        checkNet();
        request.addHeader("client", "android");
        request.addHeader("ACCEPT", "*/*");
        request.addHeader("Authorization", "Basic " + authorization);
        request.addHeader("Content-Type", CONTENT_TYPE);
        request.url(urlString);
        request.put(builder.build());
        Call call=client.newCall(request.build());
        call.enqueue(callback);
    }
    public static void put(String urlString,FormEncodingBuilder builder,Callback callback) {
        checkNet();
        request.addHeader("client", "android");
        request.addHeader("ACCEPT", "*/*");
        request.addHeader("Content-Type", CONTENT_TYPE);
        request.url(urlString);
        request.put(builder.build());
        Call call=client.newCall(request.build());
        call.enqueue(callback);
    }
    public static void get(String urlString,Callback callback){
        checkNet();
        request.addHeader("imei", getIdentifier());
        request.addHeader("client", "android");
        request.addHeader("ACCEPT", "*/*");
        request.url(urlString);
        request.get();
        Call call=client.newCall(request.build());
        call.enqueue(callback);
    }
    public static void post(String urlString,FormEncodingBuilder builder,Callback callback){
        checkNet();
        request.addHeader("imei", getIdentifier());
        request.addHeader("client", "android");
        request.addHeader("ACCEPT", "*/*");
        request.url(urlString);
        request.post(builder.build());
        Call call=client.newCall(request.build());
        call.enqueue(callback);
    }
    public static void get(String urlString,Callback callback, String authorization) // 用一个完整url获取一个string对象
    {
        checkNet();
        request.addHeader("imei", getIdentifier());
        request.addHeader("client", "android");
        request.addHeader("ACCEPT", "*/*");
        request.addHeader("Authorization", "Basic " + authorization);
        request.url(urlString);
        request.get();
        Call call=client.newCall(request.build());
        call.enqueue(callback);
    }
    public static void post(String urlString,FormEncodingBuilder builder,Callback callback, String authorization) // 用一个完整url获取一个string对象
    {
        checkNet();
        request.addHeader("imei", getIdentifier());
        request.addHeader("client", "android");
        request.addHeader("ACCEPT", "*/*");
        request.addHeader("Authorization", "Basic " + authorization);
        request.url(urlString);
        request.post(builder.build());
        Call call=client.newCall(request.build());
        call.enqueue(callback);
    }
    public static void post(String url,FormEncodingBuilder builder,Callback callback,
                            boolean needBear) {
        checkNet();
        request.addHeader("imei", getIdentifier());
        request.addHeader("client", "android");
        request.addHeader("ACCEPT", "*/*");
        request.url(url);
        request.post(builder.build());
        if (needBear)
            request.addHeader("Bear", "9cfe953a2a6aef02aaf6971bc7ca62c3f0167e67");
        Call call=client.newCall(request.build());
        call.enqueue(callback);
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

}

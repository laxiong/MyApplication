package com.laxiong.Utils;

import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Inter_Req.MyInterceptor;
import com.squareup.okhttp.OkHttpClient;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by xiejin on 2016/5/27.
 * Types RetrofitUtils.java
 */
public class RetrofitUtils {
    private static OkHttpClient client = new OkHttpClient(), client2 = new OkHttpClient();
    private static Retrofit.Builder retrofit;

    static {
        client.interceptors().add(new MyInterceptor(false));
        client2.interceptors().add(new MyInterceptor(true));
    }

    public static Retrofit getRetrofitUnAuth() {
        return getRetrofit(client);
    }

    public static Retrofit getRetrofitAuth() {
        return getRetrofit(client2);
    }

    private static Retrofit getRetrofit(OkHttpClient client) {
        checkNet();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(InterfaceInfo.BASE_URL);
        }
        retrofit.client(client);
        return retrofit.build();
    }

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

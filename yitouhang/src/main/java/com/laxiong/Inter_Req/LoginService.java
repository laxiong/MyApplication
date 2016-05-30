package com.laxiong.Inter_Req;

import com.laxiong.Calender.CalendarView;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.entity.User;
import com.laxiong.entity.UserLogin;
import com.sina.weibo.sdk.api.share.BaseResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by xiejin on 2016/5/27.
 * Types LoginService.java
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("/v4_1/login")
    Call<UserLogin> login(@Field("phone") String phone,@Field("pwd") String pwd);

    @GET("/v4_1/user/{tokenid}")
    Call<User> getUser(@Path("tokenid") String id);

}

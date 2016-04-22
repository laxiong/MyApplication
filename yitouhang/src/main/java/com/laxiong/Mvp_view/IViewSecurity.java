package com.laxiong.Mvp_view;

/**
 * Created by xiejin on 2016/4/22.
 * Types IViewSecurity.java
 */
public interface IViewSecurity extends IViewCommonBack {
    public void setToken(String token);

    public String getName();

    public String getIdenti();

    public String getPwd();
}

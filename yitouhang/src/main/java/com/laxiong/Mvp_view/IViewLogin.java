package com.laxiong.Mvp_view;

/**
 * Created by xiejin on 2016/4/18.
 * Types IViewLogin.java
 */
public interface IViewLogin {
    public String getInputPhoneNum();
    public String getInputPwd();
    public void loginsuccess();
    public void loginfailed(String msg);
    public void updateButton(boolean isabled);
}

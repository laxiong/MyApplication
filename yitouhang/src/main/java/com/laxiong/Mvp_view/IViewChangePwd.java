package com.laxiong.Mvp_view;

/**
 * Created by xiejin on 2016/4/19.
 * Types IViewChangePwd.java
 */
public interface IViewChangePwd {
    public String getOldPwd();
    public String getNewPwd();
    public void updateSuc();
    public void updateFailure(String msg);
}

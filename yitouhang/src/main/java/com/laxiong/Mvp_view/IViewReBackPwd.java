package com.laxiong.Mvp_view;

/**
 * Created by xiejin on 2016/4/19.
 * Types IViewReBackPwd.java
 */
public interface IViewReBackPwd {
    public String getTextPhone();
    public String getTextPwd();
    public String getValiCode();
    public void getCodeSuccess();
    public void getCodeFailure(String msg);
    public void reBackSuccess();
    public void reBackFailure(String msg);
}

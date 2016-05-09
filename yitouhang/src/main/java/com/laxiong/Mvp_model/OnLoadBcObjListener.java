package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/5/5.
 * Types OnLoadBcObjListener.java
 */
public interface OnLoadBcObjListener<T> {
    public void onSuccss(T obj);
    public void onFailure(String msg);
}

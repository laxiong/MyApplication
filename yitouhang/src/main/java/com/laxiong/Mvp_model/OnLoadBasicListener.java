package com.laxiong.Mvp_model;

import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types OnLoadBasicListener.java
 */
public interface OnLoadBasicListener<T>{
    public void loadOnSuccess(List<T> list);
    public void loadOnFailure(String msg);
}

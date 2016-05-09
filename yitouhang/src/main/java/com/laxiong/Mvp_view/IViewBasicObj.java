package com.laxiong.Mvp_view;

/**
 * Created by xiejin on 2016/5/5.
 * Types IViewBasicObj.java
 */
public interface IViewBasicObj<T> {
    public void loadObjSuc(T obj);
    public void loadObjFail(String msg);
}

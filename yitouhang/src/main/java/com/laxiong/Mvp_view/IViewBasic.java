package com.laxiong.Mvp_view;

import java.util.List;

/**
 * Created by xiejin on 2016/5/4.
 * Types IViewBasic.java
 */
public interface IViewBasic<T> {
    public void loadListSuc(List<T> list);
    public void loadListFail(String msg);
}

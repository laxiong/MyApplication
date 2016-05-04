package com.laxiong.Mvp_view;

import com.laxiong.Mvp_model.WelcImg;

import java.util.List;

/**
 * Created by xiejin on 2016/5/3.
 * Types IViewWelcome.java
 */
public interface IViewWelcome {
    public void loadListSuc(WelcImg list);

    public void loadListFail(String msg);
}

package com.laxiong.Mvp_view;

import com.laxiong.Mvp_model.Order;
import com.laxiong.Mvp_model.Score;

import java.util.List;

/**
 * Created by xiejin on 2016/4/29.
 * Types IViewYibi.java
 */
public interface IViewYibi {
    public void loadListSuc(List<Score> list);
    public void loadListFailure(String msg);
}

package com.laxiong.Mvp_view;

import com.laxiong.Mvp_model.Order;

import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types IViewOrder.java
 */
public interface IViewOrder{
    public void loadListOrder(List<Order> list);
    public void loadListFailure(String msg);
}

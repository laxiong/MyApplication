package com.laxiong.Mvp_view;

import com.laxiong.Mvp_model.InvestItem;

import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types IViewInvest.java
 */
public interface IViewInvest {
    public void loadListInvest(List<InvestItem> list);
    public void loadListFailure(String msg);
}

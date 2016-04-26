package com.laxiong.Mvp_view;

import com.laxiong.Mvp_model.BankCard;

/**
 * Created by xiejin on 2016/4/26.
 * Types IViewWithdraw.java
 */
public interface IViewWithdraw extends IViewCommonBack{
    public void loadCardData(BankCard card);

    public void loadCardFailure(String msg);
}

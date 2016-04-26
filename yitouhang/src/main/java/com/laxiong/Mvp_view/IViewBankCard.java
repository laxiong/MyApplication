package com.laxiong.Mvp_view;

import com.laxiong.Mvp_model.BankCard;

/**
 * Created by xiejin on 2016/4/25.
 * Types IViewBankCard.java
 */
public interface IViewBankCard{
    public void loadCardData(BankCard card);
    public void loadCardFailure(String msg);
}

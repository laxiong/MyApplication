package com.laxiong.Mvp_view;

import com.laxiong.Mvp_model.BindCardItem;

import java.util.List;

/**
 * Created by xiejin on 2016/4/25.
 * Types IViewCardList.java
 */
public interface IViewCardList {
    public void loadCardListData(List<BindCardItem> listitem);
    public void loadCardListFailure(String msg);
}

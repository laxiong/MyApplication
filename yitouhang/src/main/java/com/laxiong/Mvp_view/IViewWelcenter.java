package com.laxiong.Mvp_view;

import android.view.View;

/**
 * Created by xiejin on 2016/4/14.
 * Types IViewWelcenter.java
 */
public interface IViewWelcenter {
    public void setMaxPage(int num);
    public int getMaxPage();
    public int getPageNow();
    public void addFootView(View view);
    public void addList(boolean init,boolean isused);
    public void setEmptyView(View emptyview);
    public void setBottomTipVisibily(boolean flag);
}

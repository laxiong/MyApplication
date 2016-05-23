package com.laxiong.Mvp_view;

import android.view.View;

import com.laxiong.Adapter.RedPaper;

import java.util.List;

/**
 * Created by xiejin on 2016/4/14.
 * Types IViewWelcenter.java
 */
public interface IViewWelcenter {
    public void setMaxPage(int num);
    public int getMaxPage();
    public int getPageNow();
    public void setPageNow(int pagenow);
    public void addFootView(View view);
    public void addList(boolean init, boolean isused, List<RedPaper> list);
    public void setEmptyView();
    public void setBottomTipVisibily(boolean flag);
    public void reqListFailure(String msg);
    public void loadAll(List<RedPaper> list);
}

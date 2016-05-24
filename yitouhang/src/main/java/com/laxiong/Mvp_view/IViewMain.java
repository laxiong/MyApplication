package com.laxiong.Mvp_view;

import android.view.View;

import com.laxiong.Mvp_model.UpdateInfo;
import com.laxiong.entity.Banner;

import java.util.List;

/**
 * Created by xiejin on 2016/5/17.
 * Types IViewMain.java
 */
public interface IViewMain {
    public void loadUpdateInfo(UpdateInfo info);
    public void loadListSuc(List<Banner> list);
    public void loadListFail(String msg);
    public void registerUpdateReceiver(View dialog);
}

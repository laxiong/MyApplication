package com.laxiong.Mvp_presenter;

import android.os.Handler;

import com.laxiong.Adapter.CalenderBean;
import com.laxiong.Calender.ListViewCalenderUtil;
import com.laxiong.Mvp_view.IViewMonthCal;

import java.util.ArrayList;

/**
 * Created by xiejin on 2016/4/14.
 * Types MonthCal_Presenter.java
 */
public class MonthCal_Presenter {
    private IViewMonthCal iviewmonth;
    public MonthCal_Presenter(IViewMonthCal iviewmonth){
        this.iviewmonth=iviewmonth;
    }
    public void loadmore(){
        long start = System.currentTimeMillis();
        iviewmonth.refreshOrLoadList(false);
        long end = System.currentTimeMillis();
        if (end - start < 2000) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iviewmonth.refreshOrLoadComplete(true);
                }
            }, 2000 - (end - start));
        }
    }
    public void refresh(){
        long start = System.currentTimeMillis();
        iviewmonth.refreshOrLoadList(true);
        long end = System.currentTimeMillis();
        if (end - start < 2000) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iviewmonth.refreshOrLoadComplete(true);
                }
            }, 2000 - (end - start));
        }
    }
}

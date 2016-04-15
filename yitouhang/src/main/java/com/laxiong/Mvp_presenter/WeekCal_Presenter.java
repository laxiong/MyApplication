package com.laxiong.Mvp_presenter;

import com.laxiong.Calender.CustomDate;
import com.laxiong.Mvp_view.IWeekCalendar;

import java.util.ArrayList;

/**
 * Created by xiejin on 2016/4/14.
 * Types WeekCal_Presenter.java
 */
public class WeekCal_Presenter {
    private IWeekCalendar iweek;
    public WeekCal_Presenter(IWeekCalendar iweek){
        iweek=iweek;
    }
    //根据当前日期获取日程
    public ArrayList<String> reqDataList(CustomDate date){
        ArrayList<String> list = new ArrayList<String>();
        list.add("爱上对方你哦啊是发送的非农");
        list.add("爱上对方你哦啊是发送的非农dfg ");
        list.add("爱上对方你哦啊是发送的非农dfg ");
        list.add("爱上对方你哦啊是发aa送的非农dfg ");
        return list;
    }
}

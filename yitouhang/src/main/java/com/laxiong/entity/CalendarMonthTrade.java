package com.laxiong.entity;

import java.util.List;

/**
 * Created by admin on 2016/5/12.
 */
public class CalendarMonthTrade {
    /***
     * 用户月投资日历内容
     */
    private  List<String> keys ;
    private List<Integer> values ;

    private static CalendarMonthTrade instance ;
    private CalendarMonthTrade(){}
    public synchronized static CalendarMonthTrade getInstance(){
        if (instance==null){
            instance = new CalendarMonthTrade();
        }
        return instance ;
    }
    public List<String> getKeys() {
        return keys;
    }
    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
    public List<Integer> getValues() {
        return values;
    }
    public void setValues(List<Integer> values) {
        this.values = values;
    }
}

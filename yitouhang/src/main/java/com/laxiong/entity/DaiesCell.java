package com.laxiong.entity;

import java.util.List;

/**
 * Created by admin on 2016/5/16.
 */
public class DaiesCell {
    /***
     * 日历的日的点击显示的详细内容
     */
    private List<DayDetails> list ;

    public List<DayDetails> getList() {
        return list;
    }

    public void setList(List<DayDetails> list) {
        this.list = list;
    }
}

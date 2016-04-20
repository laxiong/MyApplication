package com.laxiong.entity;

/**
 * Created by admin on 2016/4/20.
 */
public class Notice {
    /***
     * 公告
     */
    private  int index ;
    private String notice;

    public Notice(int index,String notice){
        this.index=index;
        this.notice=notice;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}

package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/5/4.
 * Types AtHall.java
 */
public class AtHall {
    private String title;
    private String href;
    private String imageurl;
    private String begin_date;

    public AtHall() {
    }

    public AtHall(String title, String href, String imageurl, String begin_date) {
        this.title = title;
        this.href = href;
        this.imageurl = imageurl;
        this.begin_date = begin_date;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}

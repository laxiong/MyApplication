package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/5/3.
 * Types WelcImg.java
 */
public class WelcImg {
    private String title;
    private String href;
    private String imageurl;
    private int duration;

    public WelcImg() {
    }

    public WelcImg(String title, String href, String imageurl,int duration) {
        this.title = title;
        this.href = href;
        this.imageurl = imageurl;
        this.duration=duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    @Override
    public String toString() {
        return super.toString();
    }
}

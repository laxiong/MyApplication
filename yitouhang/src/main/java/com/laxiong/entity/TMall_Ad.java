package com.laxiong.entity;

/**
 * Created by xiejin on 2016/4/19.
 * Types TMall_Ad.java
 * 壹币商城的广告
 */
public class TMall_Ad {
    private String title;
    private String imageurl;
    private String href;
    private int is_share;
    private int is_skip;
    private String shareimageurl;
    private String content;
    private long duration;
    private String color;

    public TMall_Ad() {
    }

    public TMall_Ad(String title, String imageurl, String href, int is_share, int is_skip, String shareimageurl, String content, long duration, String color) {
        this.title = title;
        this.imageurl = imageurl;
        this.href = href;
        this.is_share = is_share;
        this.is_skip = is_skip;
        this.shareimageurl = shareimageurl;
        this.content = content;
        this.duration = duration;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getIs_share() {
        return is_share;
    }

    public void setIs_share(int is_share) {
        this.is_share = is_share;
    }

    public int getIs_skip() {
        return is_skip;
    }

    public void setIs_skip(int is_skip) {
        this.is_skip = is_skip;
    }

    public String getShareimageurl() {
        return shareimageurl;
    }

    public void setShareimageurl(String shareimageurl) {
        this.shareimageurl = shareimageurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

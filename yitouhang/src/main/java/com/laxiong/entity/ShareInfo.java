package com.laxiong.entity;

/**
 * Created by xiejin on 2016/4/29.
 * Types ShareInfo.java
 */
public class ShareInfo {
    private String title;
    private String content;
    private String imgurl;
    private String url;

    public ShareInfo(String title, String content, String imgurl, String url) {
        this.title = title;
        this.content = content;
        this.imgurl = imgurl;
        this.url = url;
    }

    public ShareInfo() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

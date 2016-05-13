package com.laxiong.entity;

import java.io.Serializable;

/**
 * Created by xiejin on 2016/4/29.
 * Types ShareInfo.java
 */
public class ShareInfo implements Serializable{
    private String title;
    private String content;
    private String img;
    private String url;

    public ShareInfo(String title, String content, String img, String url) {
        this.title = title;
        this.content = content;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

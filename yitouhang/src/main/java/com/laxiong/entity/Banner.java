package com.laxiong.entity;

import java.io.Serializable;

/**
 * Created by admin on 2016/4/20.
 */
public class Banner implements Serializable{
    private String title ;
    private String href ;
    private String content;
    private String shareimageurl;
    private String imageurl;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShareimageurl() {
        return shareimageurl;
    }

    public void setShareimageurl(String shareimageurl) {
        this.shareimageurl = shareimageurl;
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

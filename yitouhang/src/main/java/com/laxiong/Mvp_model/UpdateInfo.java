package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/5/17.
 * Types UpdateInfo.java
 */
public class UpdateInfo {
    private String newversion;
    private String nowversion;
    private int status;
    private String info;
    private String url;

    public UpdateInfo() {
    }

    public UpdateInfo(String newversion, String nowversion, int status, String info, String url) {
        this.newversion = newversion;
        this.nowversion = nowversion;
        this.status = status;
        this.info = info;
        this.url = url;
    }

    public String getNewversion() {
        return newversion;
    }

    public void setNewversion(String newversion) {
        this.newversion = newversion;
    }

    public String getNowversion() {
        return nowversion;
    }

    public void setNowversion(String nowversion) {
        this.nowversion = nowversion;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

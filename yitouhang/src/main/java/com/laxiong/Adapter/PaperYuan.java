package com.laxiong.Adapter;

/**
 * Created by xiejin on 2016/4/8.
 * Types PaperYuan.java
 */
public class PaperYuan {
    private String path;
    private int num;

    public PaperYuan() {
    }

    public PaperYuan(String path, int num) {
        this.path = path;
        this.num = num;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

package com.laxiong.entity;

/**
 * Created by xiejin on 2016/4/18.
 * Types User.java
 */
public class User {
    private int token_id;
    private int token;
    private int code;
    private String msg;
    private long time;
    private String photo;
    private String name;

    public User() {
    }

    public User(int token_id, int token, int code, String msg, long time, String photo, String name) {
        this.token_id = token_id;
        this.token = token;
        this.code = code;
        this.msg = msg;
        this.time = time;
        this.photo = photo;
        this.name = name;
    }

    public int getToken_id() {
        return token_id;
    }

    public void setToken_id(int token_id) {
        this.token_id = token_id;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

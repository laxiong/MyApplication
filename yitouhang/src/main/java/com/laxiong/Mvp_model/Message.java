package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/4/29.
 * Types Message.java
 */
public class Message {
    private String add_time;
    private int id;
    private String intro;
    private boolean is_active;
    private String text;
    private String title;
    private int type_id;
    private String url;
    public enum EntryType{
        GONGGAO(2,"公告");
        int name;
        String val;

        public int getName() {
            return name;
        }

        public void setName(int name) {
            this.name = name;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        private EntryType(int name,String val){
            this.name=name;
            this.val=val;
        }
    }
    public Message(String add_time, int id, String intro, boolean is_active, String text, String title, int type_id, String url) {
        this.add_time = add_time;
        this.id = id;
        this.intro = intro;
        this.is_active = is_active;
        this.text = text;
        this.title = title;
        this.type_id = type_id;
        this.url = url;
    }

    public Message() {

    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public boolean is_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

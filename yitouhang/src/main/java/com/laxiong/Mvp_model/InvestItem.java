package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/4/28.
 * Types InvestItem.java
 */
public class InvestItem {
    private int id;
    private String types;
    private String title;
    private String amount;
    private String add_time;
    private String mark;

    public InvestItem() {
    }

    public InvestItem(int id,String types,String title, String amount, String add_time, String mark) {
        this.id=id;
        this.types=types;
        this.title = title;
        this.amount = amount;
        this.add_time = add_time;
        this.mark = mark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}

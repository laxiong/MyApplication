package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/5/18.
 * Types InvestDetail.java
 */
public class InvestDetail {
    private int id;
    private int borrow_id;
    private int ttnum;
    private String add_time;
    private String type;
    private String collection_time;
    private double amount;
    private String routine;
    private String name;
    private String phone;
    private String time;
    private String idc;

    public InvestDetail() {
    }

    public InvestDetail(int id,int borrow_id,int ttnum,String add_time, String type, String collection_time, double amount, String routine, String name, String phone, String time, String idc) {
        this.id = id;
        this.borrow_id=borrow_id;
        this.ttnum=ttnum;
        this.add_time = add_time;
        this.type = type;
        this.collection_time = collection_time;
        this.amount = amount;
        this.routine = routine;
        this.name = name;
        this.phone = phone;
        this.time = time;
        this.idc = idc;
    }

    public int getBorrow_id() {
        return borrow_id;
    }

    public void setBorrow_id(int borrow_id) {
        this.borrow_id = borrow_id;
    }

    public int getTtnum() {
        return ttnum;
    }

    public void setTtnum(int ttnum) {
        this.ttnum = ttnum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCollection_time() {
        return collection_time;
    }

    public void setCollection_time(String collection_time) {
        this.collection_time = collection_time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRoutine() {
        return routine;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }
}

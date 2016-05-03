package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/4/28.
 * Types Order.java
 */
public class Order {
    private String title;
    private int event_amount;
    private String add_time;
    private int rental;

    public Order() {
    }

    public Order(String title, int event_amount, String add_time, int rental) {
        this.title = title;
        this.event_amount = event_amount;
        this.add_time = add_time;
        this.rental = rental;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEvent_amount() {
        return event_amount;
    }

    public void setEvent_amount(int event_amount) {
        this.event_amount = event_amount;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getRental() {
        return rental;
    }

    public void setRental(int rental) {
        this.rental = rental;
    }
}

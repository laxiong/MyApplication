package com.laxiong.entity;

/**
 * Created by xiejin on 2016/4/18.
 * Types Amount.java
 */
public class Amount {
    private String key;
    private String name;
    private int amount;

    public Amount() {
    }

    public Amount(String key, String name, int amount) {
        this.key = key;
        this.name = name;
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

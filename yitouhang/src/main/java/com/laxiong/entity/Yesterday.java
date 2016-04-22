package com.laxiong.entity;

/**
 * Created by xiejin on 2016/4/22.
 * Types Yesterday.java
 */
public class Yesterday {
    private float sxt;
    private float gxb;
    private float rm;
    private float total;

    public Yesterday() {
    }

    public Yesterday(float sxt, float gxb, float rm, float total) {
        this.sxt = sxt;
        this.gxb = gxb;
        this.rm = rm;
        this.total = total;
    }

    public float getSxt() {
        return sxt;
    }

    public void setSxt(float sxt) {
        this.sxt = sxt;
    }

    public float getGxb() {
        return gxb;
    }

    public void setGxb(float gxb) {
        this.gxb = gxb;
    }

    public float getRm() {
        return rm;
    }

    public void setRm(float rm) {
        this.rm = rm;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}

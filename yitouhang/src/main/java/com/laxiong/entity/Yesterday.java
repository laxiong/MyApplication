package com.laxiong.entity;

/**
 * Created by xiejin on 2016/4/22.
 * Types Yesterday.java
 */
public class Yesterday {
    private double sxt;
    private double gxb;
    private double rm;
    private double total;

    public Yesterday() {
    }

    public Yesterday(double sxt, double gxb, double rm, double total) {
        this.sxt = sxt;
        this.gxb = gxb;
        this.rm = rm;
        this.total = total;
    }

    public double getSxt() {
        return sxt;
    }

    public void setSxt(double sxt) {
        this.sxt = sxt;
    }

    public double getGxb() {
        return gxb;
    }

    public void setGxb(float gxb) {
        this.gxb = gxb;
    }

    public double getRm() {
        return rm;
    }

    public void setRm(double rm) {
        this.rm = rm;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

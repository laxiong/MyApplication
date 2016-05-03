package com.laxiong.entity;

/**
 * Created by xiejin on 2016/4/18.
 * Types Profit.java
 */
public class Profit {
    private double sxt;
    private double renmai;
    private double licaijin;
    private double xianjin;
    private double tiyanjin;
    private double gxb;

    public Profit() {
    }

    public Profit(double sxt, double renmai, double licaijin, double xianjin, double tiyanjin, double gxb) {
        this.sxt = sxt;
        this.renmai = renmai;
        this.licaijin = licaijin;
        this.xianjin = xianjin;
        this.tiyanjin = tiyanjin;
        this.gxb = gxb;
    }

    public double getSxt() {
        return sxt;
    }

    public void setSxt(double sxt) {
        this.sxt = sxt;
    }

    public double getRenmai() {
        return renmai;
    }

    public void setRenmai(double renmai) {
        this.renmai = renmai;
    }

    public double getLicaijin() {
        return licaijin;
    }

    public void setLicaijin(double licaijin) {
        this.licaijin = licaijin;
    }

    public double getXianjin() {
        return xianjin;
    }

    public void setXianjin(double xianjin) {
        this.xianjin = xianjin;
    }

    public double getTiyanjin() {
        return tiyanjin;
    }

    public void setTiyanjin(double tiyanjin) {
        this.tiyanjin = tiyanjin;
    }

    public double getGxb() {
        return gxb;
    }

    public void setGxb(double gxb) {
        this.gxb = gxb;
    }
}

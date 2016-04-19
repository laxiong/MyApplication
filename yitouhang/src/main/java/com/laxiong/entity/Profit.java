package com.laxiong.entity;

/**
 * Created by xiejin on 2016/4/18.
 * Types Profit.java
 */
public class Profit {
    private float sxt;
    private float licaijin;
    private float xianjin;
    private float tiyanjin;
    private float gxb;

    public Profit() {
    }

    public Profit(float sxt, float licaijin, float xianjin, float tiyanjin, float gxb) {
        this.sxt = sxt;
        this.licaijin = licaijin;
        this.xianjin = xianjin;
        this.tiyanjin = tiyanjin;
        this.gxb = gxb;
    }

    public float getSxt() {
        return sxt;
    }

    public void setSxt(float sxt) {
        this.sxt = sxt;
    }

    public float getLicaijin() {
        return licaijin;
    }

    public void setLicaijin(float licaijin) {
        this.licaijin = licaijin;
    }

    public float getXianjin() {
        return xianjin;
    }

    public void setXianjin(float xianjin) {
        this.xianjin = xianjin;
    }

    public float getTiyanjin() {
        return tiyanjin;
    }

    public void setTiyanjin(float tiyanjin) {
        this.tiyanjin = tiyanjin;
    }

    public float getGxb() {
        return gxb;
    }

    public void setGxb(float gxb) {
        this.gxb = gxb;
    }

    @Override
    public String toString() {
        return "Profit{" +
                "sxt=" + sxt +
                ", licaijin=" + licaijin +
                ", xianjin=" + xianjin +
                ", tiyanjin=" + tiyanjin +
                ", gxb=" + gxb +
                '}';
    }
}

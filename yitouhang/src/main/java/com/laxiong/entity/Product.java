package com.laxiong.entity;

import java.io.Serializable;

/**
 * Created by xiejin on 2016/4/19.
 * Types Product.java
 */
public class Product implements Serializable{
    private int id;
    private String title;
    private float pay;
    private String img;
    private int num;
    private int type;
    private String inn_img;

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pay=" + pay +
                ", img='" + img + '\'' +
                ", num=" + num +
                ", type=" + type +
                ", inn_img='" + inn_img + '\'' +
                '}';
    }

    public Product(int id, String title, float pay, String img, int num, int type,String inn_img) {
        this.id = id;
        this.title = title;
        this.pay = pay;
        this.img = img;
        this.num = num;
        this.type = type;
        this.inn_img=inn_img;
    }
    public String getInn_img() {
        return inn_img;
    }

    public void setInn_img(String inn_img) {
        this.inn_img = inn_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

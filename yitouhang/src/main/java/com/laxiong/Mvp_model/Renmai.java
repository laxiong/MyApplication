package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/5/4.
 * Types Renmai.java
 */
public class Renmai {
    private int id;
    private String name;
    private String date;
    private int weight;
    private double investment;
    private int score;
    private int interest;
    private String last_investment;

    public Renmai() {
    }

    public Renmai(int id, String name, int weight,String date, double investment, int score, int interest, String last_investment) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.date=date;
        this.investment = investment;
        this.score = score;
        this.interest = interest;
        this.last_investment = last_investment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getInvestment() {
        return investment;
    }

    public void setInvestment(double investment) {
        this.investment = investment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public String getLast_investment() {
        return last_investment;
    }

    public void setLast_investment(String last_investment) {
        this.last_investment = last_investment;
    }
}

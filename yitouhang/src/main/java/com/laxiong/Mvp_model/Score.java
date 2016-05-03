package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/4/29.
 * Types Score.java
 */
public class Score {
    private int id;
    private int score;
    private int total_score;
    private String dates;
    private String type;
    private String desc;
    private int status;

    public Score() {
    }

    public Score(int id, int score, int total_score, String dates, String type, String desc, int status) {
        this.id = id;
        this.score = score;
        this.total_score = total_score;
        this.dates = dates;
        this.type = type;
        this.desc = desc;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", score=" + score +
                ", total_score=" + total_score +
                ", date='" + dates + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", status=" + status +
                '}';
    }
}

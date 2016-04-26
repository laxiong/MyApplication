package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/4/25.
 * Types BankCard.java
 */
public class BankCard extends BaseCard{
    private int code;
    private long time;
    private String name;
    private int id;
    private boolean is_main;
    private int number;
    private int snumber;
    private String logo;
    private String type;
    private boolean is_modify;
    private String day_limit;
    private String one_limit;
    private String month_limit;
    private String logoKey;

    public BankCard() {
    }

    /**
     * @param code
     * @param time
     * @param name
     * @param id
     * @param is_main
     * @param number
     * @param snumber
     * @param logo
     * @param type        银行卡类型
     * @param is_modify
     * @param day_limit   日限制
     * @param one_limit   一次限制
     * @param month_limit 月限制
     * @param logoKey
     */
    public BankCard(int code, long time, String name, int id, boolean is_main, int number, int snumber, String logo, String type, boolean is_modify, String day_limit, String one_limit, String month_limit, String logoKey) {
        this.code = code;
        this.time = time;
        this.name = name;
        this.id = id;
        this.is_main = is_main;
        this.number = number;
        this.snumber = snumber;
        this.logo = logo;
        this.type = type;
        this.is_modify = is_modify;
        this.day_limit = day_limit;
        this.one_limit = one_limit;
        this.month_limit = month_limit;
        this.logoKey = logoKey;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean is_main() {
        return is_main;
    }

    public void setIs_main(boolean is_main) {
        this.is_main = is_main;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSnumber() {
        return snumber;
    }

    public void setSnumber(int snumber) {
        this.snumber = snumber;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean is_modify() {
        return is_modify;
    }

    public void setIs_modify(boolean is_modify) {
        this.is_modify = is_modify;
    }

    public String getDay_limit() {
        return day_limit;
    }

    public void setDay_limit(String day_limit) {
        this.day_limit = day_limit;
    }

    public String getOne_limit() {
        return one_limit;
    }

    public void setOne_limit(String one_limit) {
        this.one_limit = one_limit;
    }

    public String getMonth_limit() {
        return month_limit;
    }

    public void setMonth_limit(String month_limit) {
        this.month_limit = month_limit;
    }

    public String getLogoKey() {
        return logoKey;
    }

    public void setLogoKey(String logoKey) {
        this.logoKey = logoKey;
    }
}

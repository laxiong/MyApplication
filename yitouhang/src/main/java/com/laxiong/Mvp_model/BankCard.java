package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/4/25.
 * Types BankCard.java
 */
public class BankCard extends BaseCard {
    private String name;
    private int id;
    private boolean is_main;
    private String cardnum;
    private String number;
    private int leftsub;
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

    public BankCard(String name, int id, boolean is_main, String cardnum, String number, int leftsub, int snumber, String logo, String type, boolean is_modify, String day_limit, String one_limit, String month_limit, String logoKey) {
        this.name = name;
        this.id = id;
        this.is_main = is_main;
        this.cardnum = cardnum;
        this.number = number;
        this.leftsub = leftsub;
        this.snumber = snumber;
        this.logo = logo;
        this.type = type;
        this.is_modify = is_modify;
        this.day_limit = day_limit;
        this.one_limit = one_limit;
        this.month_limit = month_limit;
        this.logoKey = logoKey;
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

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getLeftsub() {
        return leftsub;
    }

    public void setLeftsub(int leftsub) {
        this.leftsub = leftsub;
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

package com.laxiong.entity;

import java.util.List;

/**
 * Created by xiejin on 2016/4/18.
 * Types User.java
 */
public class User {
    private int code;
    private String msg;
    private int id;
    private float available_amount;
    private float total_amount;
    private String nickname;
    private String idc;
    private String realname;
    private String named;
    private String email;
    private String phone;
    private int pay_pwd;
    private int is_vip;
    private int contacts;
    private float friendsprofit;
    private int experience;
    private int salt;
    private int score;
    private int expire;
    private int notify;
    private int quota;
    private float daishou;
    private int is_first;
    private String address;
    private String last_login;
    private int current;
    private int bankcount;
    private int packetcount;
    private int cards;
    private int amount;
    private float profit;
    private List<Amount> amount_list;
    private List<Profit> profit_list;
    private String security_level;
    private int fee;

    public User() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAvailable_amount() {
        return available_amount;
    }

    public void setAvailable_amount(float available_amount) {
        this.available_amount = available_amount;
    }

    public float getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getNamed() {
        return named;
    }

    public void setNamed(String named) {
        this.named = named;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPay_pwd() {
        return pay_pwd;
    }

    public void setPay_pwd(int pay_pwd) {
        this.pay_pwd = pay_pwd;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getContacts() {
        return contacts;
    }

    public void setContacts(int contacts) {
        this.contacts = contacts;
    }

    public float getFriendsprofit() {
        return friendsprofit;
    }

    public void setFriendsprofit(float friendsprofit) {
        this.friendsprofit = friendsprofit;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public float getDaishou() {
        return daishou;
    }

    public void setDaishou(float daishou) {
        this.daishou = daishou;
    }

    public int getIs_first() {
        return is_first;
    }

    public void setIs_first(int is_first) {
        this.is_first = is_first;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getBankcount() {
        return bankcount;
    }

    public void setBankcount(int bankcount) {
        this.bankcount = bankcount;
    }

    public int getPacketcount() {
        return packetcount;
    }

    public void setPacketcount(int packetcount) {
        this.packetcount = packetcount;
    }

    public int getCards() {
        return cards;
    }

    public void setCards(int cards) {
        this.cards = cards;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public List<Amount> getAmount_list() {
        return amount_list;
    }

    public void setAmount_list(List<Amount> amount_list) {
        this.amount_list = amount_list;
    }

    public List<Profit> getProfit_list() {
        return profit_list;
    }

    public void setProfit_list(List<Profit> profit_list) {
        this.profit_list = profit_list;
    }

    public String getSecurity_level() {
        return security_level;
    }

    public void setSecurity_level(String security_level) {
        this.security_level = security_level;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "User{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", id=" + id +
                ", available_amount=" + available_amount +
                ", total_amount=" + total_amount +
                ", nickname='" + nickname + '\'' +
                ", idc='" + idc + '\'' +
                ", realname='" + realname + '\'' +
                ", named='" + named + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", pay_pwd=" + pay_pwd +
                ", is_vip=" + is_vip +
                ", contacts=" + contacts +
                ", friendsprofit=" + friendsprofit +
                ", experience=" + experience +
                ", salt=" + salt +
                ", score=" + score +
                ", expire=" + expire +
                ", notify=" + notify +
                ", quota=" + quota +
                ", daishou=" + daishou +
                ", is_first=" + is_first +
                ", address='" + address + '\'' +
                ", last_login='" + last_login + '\'' +
                ", current=" + current +
                ", bankcount=" + bankcount +
                ", packetcount=" + packetcount +
                ", cards=" + cards +
                ", amount=" + amount +
                ", profit=" + profit +
                ", amount_list=" + amount_list +
                ", profit_list=" + profit_list +
                ", security_level='" + security_level + '\'' +
                ", fee=" + fee +
                '}';
    }
}

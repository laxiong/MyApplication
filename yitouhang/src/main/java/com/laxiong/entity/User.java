package com.laxiong.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiejin on 2016/4/18.
 * Types User.java
 */
public class User implements Serializable {
    private int code;
    private String assess;
    private String msg;
    private String time;
    private int id;
    private float available_amount;
    private float block_amount;
    private float total_amount;
    private String nickname;
    private String idc;
    private int is_idc;
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
    private Yesterday yesterday;
    private float total_get;
    private float total_pay;
    private float withdraw_amount;
    private String name;
    private String place;
    private String phones;
    private List<Amount> amount_list;
    private Profit profit_list;
    private String security_level;
    private double fee;

    public User() {
    }

    public User(String assess,int code, String msg, String time, int id, float available_amount, float block_amount, float total_amount, String nickname, String idc, int is_idc, String realname, String named, String email, String phone, int pay_pwd, int is_vip, int contacts, float friendsprofit, int experience, int salt, int score, int expire, int notify, int quota, float daishou, int is_first, String address, String last_login, int current, int bankcount, int packetcount, int cards, int amount, float profit, Yesterday yesterday, float total_get, float total_pay, float withdraw_amount, String name, String place, String phones, List<Amount> amount_list, Profit profit_list, String security_level, double fee) {
        this.assess=assess;
        this.code = code;
        this.msg = msg;
        this.time = time;
        this.id = id;
        this.available_amount = available_amount;
        this.block_amount = block_amount;
        this.total_amount = total_amount;
        this.nickname = nickname;
        this.idc = idc;
        this.is_idc = is_idc;
        this.realname = realname;
        this.named = named;
        this.email = email;
        this.phone = phone;
        this.pay_pwd = pay_pwd;
        this.is_vip = is_vip;
        this.contacts = contacts;
        this.friendsprofit = friendsprofit;
        this.experience = experience;
        this.salt = salt;
        this.score = score;
        this.expire = expire;
        this.notify = notify;
        this.quota = quota;
        this.daishou = daishou;
        this.is_first = is_first;
        this.address = address;
        this.last_login = last_login;
        this.current = current;
        this.bankcount = bankcount;
        this.packetcount = packetcount;
        this.cards = cards;
        this.amount = amount;
        this.profit = profit;
        this.yesterday = yesterday;
        this.total_get = total_get;
        this.total_pay = total_pay;
        this.withdraw_amount = withdraw_amount;
        this.name = name;
        this.place = place;
        this.phones = phones;
        this.amount_list = amount_list;
        this.profit_list = profit_list;
        this.security_level = security_level;
        this.fee = fee;
    }

    public String getAssess() {
        return assess;
    }

    public void setAssess(String assess) {
        this.assess = assess;
    }

    public boolean is_idc() {
        return is_idc==0?false:true;
    }

    public void setIs_idc(boolean is_idc) {
        this.is_idc = is_idc?1:0;
    }

    public boolean is_vip() {
        return is_vip==0?false:true;
    }

    public void setIs_vip(boolean is_vip) {
        this.is_vip = is_vip?1:0;
    }

    public boolean is_first() {
        return is_first==0?false:true;
    }

    public void setIs_first(boolean is_first) {
        this.is_first = is_first?1:0;;
    }

    public boolean isPay_pwd() {
        return pay_pwd==0?false:true;
    }

    public void setPay_pwd(boolean pay_pwd) {
        this.pay_pwd = pay_pwd?1:0;;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public float getBlock_amount() {
        return block_amount;
    }

    public void setBlock_amount(float block_amount) {
        this.block_amount = block_amount;
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

    public Yesterday getYesterday() {
        return yesterday;
    }

    public void setYesterday(Yesterday yesterday) {
        this.yesterday = yesterday;
    }

    public float getTotal_get() {
        return total_get;
    }

    public void setTotal_get(float total_get) {
        this.total_get = total_get;
    }

    public float getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(float total_pay) {
        this.total_pay = total_pay;
    }

    public float getWithdraw_amount() {
        return withdraw_amount;
    }

    public void setWithdraw_amount(float withdraw_amount) {
        this.withdraw_amount = withdraw_amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public List<Amount> getAmount_list() {
        return amount_list;
    }

    public void setAmount_list(List<Amount> amount_list) {
        this.amount_list = amount_list;
    }

    public Profit getProfit_list() {
        return profit_list;
    }

    public void setProfit_list(Profit profit_list) {
        this.profit_list = profit_list;
    }

    public String getSecurity_level() {
        return security_level;
    }

    public void setSecurity_level(String security_level) {
        this.security_level = security_level;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}

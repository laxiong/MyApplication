package com.laxiong.Mvp_model;

/**
 * Created by xiejin on 2016/4/25.
 * Types BindCardItem.java
 */
public class BindCardItem extends BaseCard{
    private String id;
    private String name;
    private String code;
    private String logo;
    private String type;
    private String sort;
    private String is_active;
    private String is_quick;
    private String is_withdraw;
    private String is_delete;
    private String add_time;
    private String logoKey;
    private String day_limit;
    private String one_limit;
    private String month_limit;
    private String img;

    public BindCardItem() {
    }

    public BindCardItem(String id, String name, String code, String logo, String type, String sort, String is_active, String is_quick, String is_withdraw, String is_delete, String add_time, String logoKey, String day_limit, String one_limit, String month_limit, String img) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.logo = logo;
        this.type = type;
        this.sort = sort;
        this.is_active = is_active;
        this.is_quick = is_quick;
        this.is_withdraw = is_withdraw;
        this.is_delete = is_delete;
        this.add_time = add_time;
        this.logoKey = logoKey;
        this.day_limit = day_limit;
        this.one_limit = one_limit;
        this.month_limit = month_limit;
        this.img = img;
    }

    @Override
    public String toString() {
        return "BindCardItem{" +
                "logoKey='" + logoKey + '\'' +
                ", logo='" + logo + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getIs_quick() {
        return is_quick;
    }

    public void setIs_quick(String is_quick) {
        this.is_quick = is_quick;
    }

    public String getIs_withdraw() {
        return is_withdraw;
    }

    public void setIs_withdraw(String is_withdraw) {
        this.is_withdraw = is_withdraw;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getLogoKey() {
        return logoKey;
    }

    public void setLogoKey(String logoKey) {
        this.logoKey = logoKey;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

package com.laxiong.Adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiejin on 2016/4/8.
 * Types RedPaper.java
 */
public class RedPaper implements Parcelable {
    public enum UsetypeEnum {
        /**
         * @param USED 已经用过的红包
         * @param UNUSED 没用过的红包
         */
        USED(3, "已用过"), UNUSED(0, "没用过");
        private int val;
        private String name;

        private UsetypeEnum(int val, String name) {
            this.val = val;
            this.name = name;
        }

        public int getVal() {
            return val;
        }


        public String getName() {
            return name;
        }

    }

    private static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            int in1 = source.readInt();
            int in2 = source.readInt();
            String str1 = source.readString();
            String str2 = source.readString();
            String str3 = source.readString();
            Integer in3 = source.readInt();
            Integer in4 = source.readInt();
            return new RedPaper(in1, in2, str1, str2, str3, in3, in4);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public RedPaper() {
    }

    private int id;
    private int min;
    private String redtype;
    private String date;
    private String rule;
    private Integer is_used;
    private Integer amount;
    private boolean selected;

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.min);
        dest.writeString(this.redtype);
        dest.writeString(this.date);
        dest.writeString(this.rule);
        dest.writeInt(this.is_used);
        dest.writeInt(this.amount);
    }

    public RedPaper(int id, int min, String redtype, String date, String rule, Integer is_used, Integer amount) {
        this.id = id;
        this.min = min;
        this.redtype = redtype;
        this.date = date;
        this.rule = rule;
        this.is_used = is_used;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getRedtype() {
        return redtype;
    }

    public void setRedtype(String redtype) {
        this.redtype = redtype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getIs_used() {
        return is_used;
    }

    public void setIs_used(Integer is_used) {
        this.is_used = is_used;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

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
        USED(0, "已用过"), UNUSED(1, "没用过");
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
            String str1 = source.readString();
            String str2 = source.readString();
            String str3 = source.readString();
            Integer in1 = source.readInt();
            Integer in2 = source.readInt();
            return new RedPaper(str1, str2, str3, in1, in2);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public RedPaper() {
    }

    private String papername;
    private String expire;
    private String explain;
    private Integer type;
    private Integer yuan;
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
        dest.writeString(this.papername);
        dest.writeString(this.expire);
        dest.writeString(this.explain);
        dest.writeInt(this.type);
        dest.writeInt(this.yuan);
    }

    public RedPaper(String papername, String expire, String explain, Integer type, Integer yuan) {
        this.papername = papername;
        this.expire = expire;
        this.explain = explain;
        this.type = type;
        this.yuan = yuan;
    }

    @Override
    public String toString() {
        return "type="+this.type;
    }

    public String getPapername() {
        return papername;
    }

    public void setPapername(String papername) {
        this.papername = papername;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getYuan() {
        return yuan;
    }

    public void setYuan(Integer yuan) {
        this.yuan = yuan;
    }
}

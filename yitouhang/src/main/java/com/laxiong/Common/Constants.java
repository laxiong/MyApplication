package com.laxiong.Common;

/**
 * Created by xiejin on 2016/4/13.
 * Types Constants.java
 */
public class Constants {
    public static final String KEY_DATE="clickdate";
    public  enum ReqEnum{
        PWD(0,"pwd"),NICK(1,"nick");
        int name;
        String val;
        private ReqEnum(int name,String val){
            this.name=name;
            this.val=val;
        }

        public int getName() {
            return name;
        }

        public String getVal() {
            return val;
        }
    }
}

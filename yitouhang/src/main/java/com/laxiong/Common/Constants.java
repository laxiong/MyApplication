package com.laxiong.Common;

/**
 * Created by xiejin on 2016/4/13.
 * Types Constants.java
 */
public class Constants {
    public static final String KEY_DATE = "clickdate";
    public static final long INTERVAL=1000;
    public static final long TIME=30000;
    public static boolean isRead=false;

    public enum ReqEnum {
        PWD(0, "pwd"), NICK(1, "nick"), BUY(2, "buy"), RPAY(3, "fddeal"), CPWD(4, "chdeal"),CPHONE(5,"phone"),PAYPWD(6,"deals"),ORDER(7,"order");
        int name;
        String val;

        private ReqEnum(int name, String val) {
            this.name = name;
            this.val = val;
        }

        public int getName() {
            return name;
        }

        public String getVal() {
            return val;
        }
    }

    public enum ReqTypeEnum {
        POST(0,"post"),GET(1,"get"),PUT(2,"put");
        int name;
        String val;

        private ReqTypeEnum(int name, String val) {
            this.name = name;
            this.val = val;
        }

        public int getName() {
            return name;
        }

        public void setName(int name) {
            this.name = name;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
}

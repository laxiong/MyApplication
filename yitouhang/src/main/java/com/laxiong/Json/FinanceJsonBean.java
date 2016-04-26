package com.laxiong.Json;


import com.laxiong.entity.FinanceInfo;

import java.util.List;

/**
 * Created by WangK on 2016/4/11.
 */
public class FinanceJsonBean {
    /****
     * 这个是理财产品的JSONBean
     */

//    private static FinanceJsonBean instance ;
//    public FinanceJsonBean(){}
//    public synchronized static FinanceJsonBean getInstance(){
//        if (instance == null){
//            instance = new FinanceJsonBean();
//        }
//        return instance;
//    }

    private  int code ;
    private String msg ;
    private String time ;
    private FinanceInfo sxt ;
    private List<FinanceInfo> gxb ;

    public int getTtnum() {
        return ttnum;
    }

    public void setTtnum(int ttnum) {
        this.ttnum = ttnum;
    }

    private int ttnum ;

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

    public FinanceInfo getSxt() {
        return sxt;
    }

    public void setSxt(FinanceInfo sxt) {
        this.sxt = sxt;
    }

    public List<FinanceInfo> getGxb() {
        return gxb;
    }

    public void setGxb(List<FinanceInfo> gxb) {
        this.gxb = gxb;
    }

    //为空处理
//    public void setNullFinanceJsonBean(){
//        if(instance!=null){
//            instance = null ;
//        }
//    }


}

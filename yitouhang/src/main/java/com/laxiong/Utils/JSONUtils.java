package com.laxiong.Utils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiejin on 2016/4/18.
 * Types JSONUtils.java
 */
public class JSONUtils {
    public static <T> T parseObject(String jsonstr,Class<T> clazz){
        T t=null;
        try{
            t=JSON.parseObject(jsonstr, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return t;
    }
    public static <T> List<T> parseArray(String jsonstr,Class<T> clazz){
        List<T> list=null;
        try{
            list=JSON.parseArray(jsonstr,clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}

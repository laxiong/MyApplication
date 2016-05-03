package com.laxiong.Mvp_model;

import android.content.Context;

import com.laxiong.Common.InterfaceInfo;
import com.loopj.android.http.RequestParams;

/**
 * Created by xiejin on 2016/4/29.
 * Types Model_Msg.java
 */
public class Model_Msg extends Model_Basic<Message>{
    public void loadMsgList(Context context,RequestParams params,OnLoadBasicListener<Message> listener){
        if(listener==null)
            return;
        setListener(listener);
        reqCommonBackByGet(InterfaceInfo.NOTICE_URL,context,params,"list",Message.class);
    }
}

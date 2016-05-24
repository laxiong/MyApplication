package com.laxiong.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.laxiong.Utils.NotificationUtil;
import com.laxiong.Utils.ToastUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by xiejin on 2016/5/5.
 * Types PushCustomReceiver.java
 */
public class PushCustomReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //普通的通知
        if(intent.getAction().equals("cn.jpush.android.intent.NOTIFICATION_RECEIVED")){
            Bundle bundle=intent.getExtras();
            String title= bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content= bundle.getString(JPushInterface.EXTRA_ALERT);
            int id = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            NotificationUtil.sendNotiToMain(context,title,content,id);
        }//自定义消息
        else if(intent.getAction().equals("cn.jpush.android.intent.MESSAGE_RECEIVED")){
            Bundle bundle=intent.getExtras();
            String title= bundle.getString(JPushInterface.EXTRA_TITLE);
            String content= bundle.getString(JPushInterface.EXTRA_MESSAGE);
            NotificationUtil.sendNotiToMain(context,title,content,-1);
        }
    }
}

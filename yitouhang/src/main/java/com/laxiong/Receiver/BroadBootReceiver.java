package com.laxiong.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.laxiong.Application.YiTouApplication;


/**
 * Created by xiejin on 2016/5/6.
 * Types BroadBootReceiver.java
 */
public class BroadBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            YiTouApplication.getInstance().initPush();
        }
    }
}

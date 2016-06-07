package com.laxiong.Basic;

import android.content.Context;
import android.view.View;

import com.laxiong.Utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xiejin on 2016/6/1.
 * Types OnSingleClickListener.java
 */
public abstract class OnSingleClickListener implements View.OnClickListener {
    private boolean enabled=true;
    private Context context;
    private static final int INTERVAL =1000;
    public OnSingleClickListener(Context context){
        this.context=context;
    }
    @Override
    public void onClick(View v) {
        if (enabled) {
            onSingleClick(v);
            enabled=false;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    enabled=true;
                }
            }, INTERVAL);
        }else{
            ToastUtil.customAlert(context,"按得太快了");
        }
    }

    public abstract void onSingleClick(View v);
}

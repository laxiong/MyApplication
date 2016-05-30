package com.laxiong.Utils;

import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2016/5/27.
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    public boolean mEnable = true;

    public static final int mDelay = 300; //毫秒

    @Override
    public void onClick(View v) {
        if (mEnable) {
            mEnable = false;
            doOnClick(v);
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    mEnable = true;
                }
            }, mDelay);
        }
    }

    public abstract void doOnClick(View v);
}
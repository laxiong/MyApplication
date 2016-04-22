package com.laxiong.Mvp_presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.laxiong.Mvp_view.IViewTimerHandler;

/**
 * Created by xiejin on 2016/4/21.
 * Types Handler_Presenter.java
 */
public class Handler_Presenter {
    private IViewTimerHandler iviewtime;

    public Handler_Presenter(IViewTimerHandler iviewtime) {
        this.iviewtime = iviewtime;
    }

    public void loadHandlerTimer(final long interval, final long time) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                iviewtime.handlerViewByTime(msg.what / 1000);
            }
        };
        new Thread() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                long now = start;
                long t = time;
                Looper.prepare();
                while (now - start <= time) {
                    try {
                        Thread.sleep(interval);
                        now += interval;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message ms = handler.obtainMessage();
                    ms.what = (int) (t -= interval);
                    ms.sendToTarget();
                }
                Looper.loop();
            }
        }.start();

    }
}

package com.laxiong.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.laxiong.Activity.MainActivity;
import com.laxiong.yitouhang.R;

/**
 * Created by xiejin on 2016/5/6.
 * Types NotificationUtil.java
 */
public class NotificationUtil {
    public static void sendNotiToMain(Context context, String title, String content) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true).setContentTitle(title).setContentText(content)
                .setTicker("你有新的消息")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        Notification notifi = builder.build();
        manager.notify(10, notifi);
    }
}

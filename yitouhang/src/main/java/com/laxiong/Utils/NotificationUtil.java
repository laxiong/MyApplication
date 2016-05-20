package com.laxiong.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.laxiong.Activity.MainActivity;
import com.gongshidai.mistGSD.R;
import com.laxiong.Common.*;
import com.laxiong.Common.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xiejin on 2016/5/6.
 * Types NotificationUtil.java
 */
public class NotificationUtil {
    private static NotificationManager manager;
    private static final int UPDATE_ID = 100;
    private Notification noti;
    public static void sendNotiToMain(Context context, String title, String content, int id) {
        if (manager == null)
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true).setContentTitle(title).setContentText(content)
                .setTicker("你有新的消息")
                .setSmallIcon(R.drawable.gongshi_licon)
                .setDeleteIntent(PendingIntent.getBroadcast(context, 0, new Intent("com.deletenoti.action"), PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        Notification notifi = builder.build();
//        try {
//
//            Field field = notifi.getClass().getDeclaredField("extraNotification");
//            Object extraNotification = field.get(notifi);
//            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
//            method.invoke(extraNotification, Constants.count);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        manager.notify(id == -1 ? 0 : id, notifi);
    }
    public void sendNotiUpdate(Context context,int progress) {
        if (manager == null)
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews rv;
        if(noti==null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.download);
            rv=new RemoteViews(context.getPackageName(), R.layout.notify_update);
            rv.setTextViewText(R.id.tv_notifi_descible, "正在下载更新包");
            builder.setContent(rv);
            noti = builder.build();
        }else{
            rv=noti.contentView;
        }
        rv.setTextViewText(R.id.tv_percent, progress + "%");
        rv.setProgressBar(R.id.pb_notifi_pb, 100, progress, false);
        manager.notify(UPDATE_ID, noti);
    }
    public static void cancelNoti(Context context, int id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(id);
    }
}

package com.laxiong.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.laxiong.Common.Settings;
import com.laxiong.Utils.CommonUtils;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.NotificationUtil;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.network.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xiejin on 2016/5/19.
 * Types DownService.java
 */
public class DownService extends Service {
    private static final int UPDATE_ID = 100;
    private NotificationUtil nutil;
    private long lasttime;
    private static final long UPDATE_INTERVAL = 1000;//每秒刷新一次

    public void downloadApk(String path, final int status) {
        HttpUtil.get(path, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(responseBody, 0, responseBody.length);
                        BufferedOutputStream bos = null;
                        String apkname = "yitouhang_release.apk";
                        File file = new File(Settings.APK_SAVE);
                        file.mkdirs();
                        file = new File(file, apkname);
                        try {
                            bos = new BufferedOutputStream(new FileOutputStream(
                                    file));
                            byte[] flush = new byte[1024];
                            int length = 0;
                            while ((length = bis.read(flush)) != -1) {
                                bos.write(flush, 0, length);
                            }
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()),
                                    "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (bos != null) {
                                try {
                                    bos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (bis != null) {
                                try {
                                    bis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }

                    @Override
                    public void onProgress(final int bytesWritten, final int totalSize) {
                        long time = System.currentTimeMillis();
                        if (time - lasttime <= UPDATE_INTERVAL)
                            return;
                        lasttime = time;
                        if (status == 3) {//强制更新
                            Intent update = new Intent("com.update.action");
                            int progress = (int) ((float) bytesWritten * 100 / totalSize);
                            update.putExtra("progress", progress);
                            update.putExtra("nowsize", CommonUtils.calculateByte(bytesWritten));
                            update.putExtra("totalsize", "/" + CommonUtils.calculateByte(totalSize));
                            sendBroadcast(update);
                        } else if (status == 2) {//建议更新
                            int progress = (int) ((float) bytesWritten * 100 / totalSize);
                            if (nutil != null)
                                nutil.sendNotiUpdate(DownService.this, progress);
                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (status == 3) {//强制更新

                        } else if (status == 2) {//建议更新
                            nutil = new NotificationUtil();
                            nutil.sendNotiUpdate(DownService.this, 0);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (status == 3) {//强制更新

                        } else if (status == 2) {//建议更新
                            NotificationUtil.cancelNoti(DownService.this, UPDATE_ID);
                        }
                        stopSelf();
                    }
                }

        );

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("com.download.action")) {
            String path = intent.getStringExtra("path");
            int status = intent.getIntExtra("status", -1);
            if (!StringUtils.isBlank(path)) {
                downloadApk(path, status);
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

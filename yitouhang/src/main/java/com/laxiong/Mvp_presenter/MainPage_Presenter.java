package com.laxiong.Mvp_presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.Model_Basic;
import com.laxiong.Mvp_model.OnLoadBasicListener;
import com.laxiong.Mvp_model.OnLoadBcObjListener;
import com.laxiong.Mvp_model.UpdateInfo;
import com.laxiong.Mvp_view.IViewMain;
import com.laxiong.Utils.CommonUtils;
import com.laxiong.Utils.DialogUtils;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.View.PayPop;
import com.laxiong.entity.Banner;
import com.laxiong.service.DownService;
import com.loopj.android.http.RequestParams;
import com.squareup.okhttp.FormEncodingBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xiejin on 2016/5/13.
 * Types MainPage_Presenter.java
 */
public class MainPage_Presenter implements OnLoadBasicListener<Banner>, OnLoadBcObjListener<UpdateInfo> {
    private IViewMain iViewBasic;
    private Model_Basic<Banner> mbasic;
    private SharedPreferences sp;
    private Model_Basic<UpdateInfo> mbc;
    private PayPop dialog;

    public MainPage_Presenter(IViewMain iViewBasic) {
        this.iViewBasic = iViewBasic;
        mbasic = new Model_Basic<Banner>();
        mbasic.setListener(this);
        mbc = new Model_Basic<UpdateInfo>();
        mbc.setListenerObj(this);
    }

    public void showForceDialog(final Activity context, UpdateInfo info, View parent) {
        if (dialog != null)
            dialog = null;
        DialogUtils.bgalpha(context, 0.3f);
        View view = LayoutInflater.from(context).inflate(R.layout.forceupdate_dialog, null);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.bgalpha(context, 1.0f);
                dialog.dismiss();
                dialog = null;
                context.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        DisplayMetrics metrix = new DisplayMetrics();
        context.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrix);
        int width = (int) (3.0f * metrix.widthPixels / 4);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog = new PayPop(view, width, height, context);
        dialog.showAtLocation(parent, Gravity.CENTER, 0, 0);
        beginDown(view, context, info);
    }

    public void showRecDialog(final Activity context, final UpdateInfo info, View parent) {
        if (dialog != null)
            dialog = null;
        DialogUtils.bgalpha(context, 0.3f);
        final View view = LayoutInflater.from(context).inflate(R.layout.recupdate_dialog, null);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setText(info.getInfo());
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.bgalpha(context, 1.0f);
                dialog.dismiss();
                dialog = null;
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.bgalpha(context, 1.0f);
                dialog.dismiss();
                dialog = null;
                beginDown(view, context, info);
            }
        });
        DisplayMetrics metrix = new DisplayMetrics();
        context.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrix);
        int width = (int) (3.0f * metrix.widthPixels / 4);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog = new PayPop(view, width, height, context);
        dialog.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void beginDown(View v, Activity context, final UpdateInfo info) {
        if(info.getStatus()==3) {//强制更新
            iViewBasic.registerUpdateReceiver(v);
        }
        Intent intent = new Intent(context, DownService.class);
        intent.putExtra("path", info.getUrl());
        intent.putExtra("status", info.getStatus());
        intent.setAction("com.download.action");
        context.startService(intent);
    }

    @Override
    public void onSuccss(UpdateInfo obj) {
        iViewBasic.loadUpdateInfo(obj);
    }

    @Override
    public void onFailure(String msg) {
        iViewBasic.loadListFail(msg);
    }

    public void checkUpdate(Context context) {
        String version = "v1";
        try {
            version = "v" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String channel = CommonUtils.getMetaData(context, "CHANNEL");
        String url=InterfaceInfo.CKUPDATE_URL+"?version="+version+"&&channel="+channel;
        mbc.reqCommonGetObj(url,"", UpdateInfo.class);
    }

    @Override
    public void loadOnSuccess(List<Banner> list) {
        if (sp != null) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            sp.edit().putString(SpUtils.LOGIN_KEY, sf.format(new Date())).commit();
        }
        iViewBasic.loadListSuc(list);
    }

    @Override
    public void loadOnFailure(String msg) {
        iViewBasic.loadListFail(msg);
    }

    public void loadPageAd(Context context) {
        sp = SpUtils.getSp(context);
        String date = sp.getString(SpUtils.LOGIN_KEY, "");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String today = sf.format(new Date());
        if (StringUtils.isBlank(date) || !today.equals(date)) {//加载首页广告
            mbasic.reqCommonBackByGet(InterfaceInfo.FIRSTAD_URL, context, null, "list", Banner.class);
        } else {//不加载
            return;
        }
    }
}

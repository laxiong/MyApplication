package com.laxiong.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.WelcImg;
import com.laxiong.Mvp_presenter.Login_Presenter;
import com.laxiong.Mvp_presenter.UserCount_Presenter;
import com.laxiong.Mvp_presenter.WelCenter_Presenter;
import com.laxiong.Mvp_presenter.Welcome_Presenter;
import com.laxiong.Mvp_view.IViewCount;
import com.laxiong.Mvp_view.IViewWelcome;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.laxiong.yitouhang.R;

import java.util.List;

public class SplashActivity extends BaseActivity implements IViewWelcome, IViewCount {
    private Welcome_Presenter presenter;
    private ImageView iv_bg;
    private UserCount_Presenter lgpresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    @Override
    public void getCountMsgSuc() {
    }

    @Override
    public void getCountMsgFai() {
    }

    @Override
    public void loadListSuc(WelcImg wel) {
        final boolean flag = ValifyUtil.judgeInit(SplashActivity.this);
        if (wel == null) {
            if (flag) {
                startActivity(new Intent(this, PatternViewActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
            return;
        } else {
            String imgurl = wel.getImageurl();
            int time = wel.getDuration();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (flag) {
                        startActivity(new Intent(SplashActivity.this, PatternViewActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                }
            }, time * 1000);
            presenter.loadImage(imgurl, this, iv_bg);
            lgpresenter.reqUserCountMsg(this);
        }
    }

    @Override
    public void loadListFail(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    private void init() {
        presenter = new Welcome_Presenter(this);
        lgpresenter = new UserCount_Presenter(this);
        SharedPreferences sp = SpUtils.getSp(this);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        if (!sp.getBoolean(SpUtils.FIRST_CONFIRM, false)) {
            sp.edit().putBoolean(SpUtils.FIRST_CONFIRM, true).commit();
            startActivity(new Intent(this, GuideActivity.class));
            finish();
        } else {
            presenter.loadWelcomeData(this);
        }
    }
}

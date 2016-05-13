package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.Welcome_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.gongshidai.mistGSD.R;

/**
 * Created by admin on 2016/4/26.
 */
public class VersionManageActivity extends BaseActivity implements View.OnClickListener, IViewCommonBack {
    /****
     * 版本管理的
     */
    private FrameLayout mBack;
    private TextView mTitle, tv_judge;
    private RelativeLayout rl_update, rl_wel, rl_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_manage);
        initView();

    }

    @Override
    public void reqbackSuc(String tag) {
        if (StringUtils.isBlank(tag))
            return;
        int flag = Integer.parseInt(tag);
        tv_judge.setText(flag == 0 ? "有更新" : "去评分");
    }

    @Override
    public void reqbackFail(String msg, String tag) {
        ToastUtil.customAlert(this, msg);
    }

    private void initView() {
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        mTitle = (TextView) findViewById(R.id.title);
        tv_judge = (TextView) findViewById(R.id.tv_judge);
        rl_update = (RelativeLayout) findViewById(R.id.rl_update);
        rl_wel = (RelativeLayout) findViewById(R.id.rl_wel);
        rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        mTitle.setText("版本管理");

        mBack.setOnClickListener(this);
        rl_update.setOnClickListener(this);
        rl_wel.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
            case R.id.rl_update:
                startActivity(new Intent(VersionManageActivity.this, WebViewActivity.class).putExtra("url", InterfaceInfo.UPDATE_URL));
                break;
            case R.id.rl_wel:
                startActivity(new Intent(VersionManageActivity.this, GuideActivity.class));
                break;
            case R.id.rl_feedback:
                startActivity(new Intent(VersionManageActivity.this, FeedBackActivity.class));
                break;
        }
    }
}

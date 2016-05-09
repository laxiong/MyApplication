package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.View.CircularScaleView;
import com.laxiong.entity.Profit;
import com.laxiong.entity.User;
import com.laxiong.yitouhang.R;

public class AssetActivity extends BaseActivity implements OnClickListener {
    /***
     * 资产的页面
     */
    private CircularScaleView mCircularView;
    private FrameLayout mBack;
    private TextView mNotice, tv_sxttouru, tv_sxtshouyi, tv_gxbtouru, tv_gxbshouyi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        initView();
        initData();
    }

    private void initData() {
        mCircularView.setProgressFrist(65.0f);
        mCircularView.setProgressSecond(15.0f);
        mBack.setOnClickListener(this);
        mNotice.setOnClickListener(this);
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            mCircularView.setTotalMoney(user.getAmount() + "");
            tv_sxttouru.setText(user.getCurrent() + "");
            tv_gxbtouru.setText(user.getAmount() - user.getCurrent() + "");
            Profit profit = user.getProfit_list();
            if (profit != null) {
                tv_sxtshouyi.setText(profit.getSxt() + "");
                tv_gxbshouyi.setText(profit.getGxb() + "");

            }
        }
    }

    private void initView() {
        mCircularView = (CircularScaleView) findViewById(R.id.circularscaleview);
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        mNotice = (TextView) findViewById(R.id.said);
        tv_sxtshouyi = (TextView) findViewById(R.id.tv_sxtshouyi);
        tv_sxttouru = (TextView) findViewById(R.id.tv_sxttouru);
        tv_gxbshouyi = (TextView) findViewById(R.id.tv_gxbshouyi);
        tv_gxbtouru = (TextView) findViewById(R.id.tv_gxbtouru);
    }

    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
            case R.id.said:
                mTishi();
                break;
        }
    }

    // 提示部分出现 提示框
    private PopupWindow mTsWindow;
    private View mTiView;

    private void mTishi() {

        mTiView = LayoutInflater.from(this).inflate(R.layout.assettishi_popwindow, null);
        TextView Btn = (TextView) mTiView.findViewById(R.id.btn);
        Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mTsWindow != null && mTsWindow.isShowing()) {
                    mTsWindow.dismiss();
                    mTsWindow = null;
                }
            }
        });

        mTsWindow = new PopupWindow(mTiView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        mTsWindow.setTouchable(true);
        mTsWindow.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mTsWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mTsWindow.showAtLocation(mTiView, Gravity.BOTTOM, 0, 0);
    }


}

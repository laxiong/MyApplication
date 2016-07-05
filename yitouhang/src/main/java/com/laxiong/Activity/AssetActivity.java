package com.laxiong.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.carfriend.mistCF.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.View.CircularScaleView;
import com.laxiong.entity.Profit;
import com.laxiong.entity.User;

public class AssetActivity extends BaseActivity implements OnClickListener {
    /***
     * 资产的页面
     */
    private CircularScaleView mCircularView;
    private FrameLayout mBack;
    private TextView mNotice, tv_sxttouru, tv_sxtshouyi, tv_gxbtouru, tv_gxbshouyi;
    private TextView mSeeMoreSxt ,mSeeMoreGxb ; //时息通 股息宝 的查看更多

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

        // 在投资产的点击跳转问题
        mSeeMoreSxt.setOnClickListener(this);
        mSeeMoreGxb.setOnClickListener(this);

        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            mCircularView.setTotalMoney(user.getAmount() + "");
            float sxttouru=user.getCurrent();
            float gxbtouru=user.getAmount() - user.getCurrent();
            tv_sxttouru.setText(sxttouru + "");
            tv_gxbtouru.setText(gxbtouru + "");
            mCircularView.setDegree(sxttouru,gxbtouru);
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
        mSeeMoreSxt =(TextView)findViewById(R.id.see_more_sxt);
        mSeeMoreGxb = (TextView)findViewById(R.id.see_more_gxb);
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
            case R.id.see_more_sxt:
                SharedPreferences ssf = getSharedPreferences("SXT_ID", Context.MODE_PRIVATE);
                int id = ssf.getInt("sxt_id",-1);
                startActivity(new Intent(AssetActivity.this,TimeXiTongActivity.class).
                        putExtra("id", id));
                break;
            case R.id.see_more_gxb:
                startActivity(new Intent(AssetActivity.this,
                        InvestmentRecordActivity.class));
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

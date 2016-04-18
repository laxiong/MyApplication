package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.laxiong.Calender.CustomDate;
import com.laxiong.Common.Constants;
import com.laxiong.Fragment.SelectMonthCalenderFragment;
import com.laxiong.Fragment.SelectWeekCalenderFragment;
import com.laxiong.yitouhang.R;

public class CalanderCountActivity extends BaseActivity implements OnClickListener {
    /****
     * 日历账单
     */
    private TextView mYearChanageBtn, mCalenderPoint, mCalenderToday, mCalenderMonth;
    private ImageView img1, img2;
    private boolean isClick = true;
    private CustomDate clickdate;
    private FrameLayout back_layout;
    private SelectMonthCalenderFragment mMonthFrag;
    private SelectWeekCalenderFragment mWeekFrag;

    private FragmentManager mFragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_count_layout);
        initView();
        initData();
        initListener();
        initFristFrag();
    }

    public void setClickDate(CustomDate date) {
        this.clickdate = date;
    }

    @SuppressLint("NewApi")
    private void initData() {
        mFragmentManager = this.getFragmentManager();

        mYearChanageBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
//                hideFragment(mTransaction);

                if (!isClick) {    // 月
                    selectColor(1);
                    if (mMonthFrag == null) {
                        mMonthFrag = new SelectMonthCalenderFragment();
                    } else {
//                        mTransaction.show(mMonthFrag);
                    }
                    mTransaction.replace(R.id.setCalander, mMonthFrag);
                    isClick = true;
                } else {       // 周
                    selectColor(2);
                    Bundle bundle = new Bundle();
                    if (clickdate != null)
                        bundle.putSerializable(Constants.KEY_DATE, clickdate);
                    if (mWeekFrag == null) {
                        mWeekFrag = new SelectWeekCalenderFragment();
                        mWeekFrag.setArguments(bundle);
                    } else {
                        mWeekFrag.getArguments().putSerializable(Constants.KEY_DATE, clickdate);
                    }
                    mTransaction.replace(R.id.setCalander, mWeekFrag);
                    isClick = false;
                }
                mTransaction.commit();
            }
        });


    }
    private void initListener(){
        mCalenderToday.setOnClickListener(this);
        mCalenderPoint.setOnClickListener(this);
        back_layout.setOnClickListener(this);
    }
    private void initView() {
        mYearChanageBtn = (TextView) findViewById(R.id.year_btn);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        back_layout= (FrameLayout) findViewById(R.id.back_layout);
        mCalenderMonth = (TextView) findViewById(R.id.month_change);
        mCalenderToday = (TextView) findViewById(R.id.calender_today);
        mCalenderPoint = (TextView) findViewById(R.id.calender_point);

    }

    @SuppressLint("NewApi")
    private void hideFragment(FragmentTransaction mTransaction) {
        if (mMonthFrag != null) {
            mTransaction.hide(mMonthFrag);
        }
        if (mWeekFrag != null) {
            mTransaction.hide(mWeekFrag);
        }
    }

    @SuppressLint("NewApi")
    private void initFristFrag() {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mMonthFrag = new SelectMonthCalenderFragment();
        mTransaction.add(R.id.setCalander, mMonthFrag);
        mTransaction.commit();
        selectColor(1);
    }

    private void selectColor(int index) {
        initColor();
        switch (index) {
            case 1:    //月
                img1.setBackgroundColor(Color.parseColor("#83c254"));
                img2.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 2:   //周
                img2.setBackgroundColor(Color.parseColor("#83c254"));
                img1.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }
    }

    //　初始化的颜色
    private void initColor() {
        img1.setBackgroundColor(Color.parseColor("#ffffff"));
        img2.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calender_today:  // 显示今天的

                break;
            case R.id.calender_point:  // 提示的小的按钮

                break;
            case R.id.back_layout:
                this.finish();
                break;
        }
    }


}
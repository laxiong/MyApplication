package com.laxiong.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.laxiong.Adapter.CmViewPagerAdapter;
import com.laxiong.Basic.BasicPageListener;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.yitouhang.R;
import com.sina.weibo.sdk.api.share.Base;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {
    private TextView tv_jump;
    private ViewPager vp_guide;
    private CmViewPagerAdapter adapter;
    private List<ImageView> listimg;
    private static final int JUMP_INDEX = 3;
    private int[] imgs = new int[]{R.drawable.guide, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tv_jump = (TextView) findViewById(R.id.tv_jump);
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
    }


    private void initData() {
        listimg = new ArrayList<ImageView>();
        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(imgs[i]);
            listimg.add(iv);
        }
        adapter = new CmViewPagerAdapter(listimg);
        vp_guide.setAdapter(adapter);
        vp_guide.setOnPageChangeListener(new BasicPageListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == JUMP_INDEX) {
                    tv_jump.setEnabled(true);
                } else {
                    tv_jump.setEnabled(false);
                }
            }
        });
    }


    private void initListener() {
        tv_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, PatternViewActivity.class));
                finish();
            }
        });
    }
}

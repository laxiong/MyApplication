package com.laxiong.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.laxiong.Fragment.RmFragment;
import com.laxiong.View.CommonActionBar;
import com.carfriend.mistCF.R;
import java.util.ArrayList;
import java.util.List;

public class RmDetailActivity extends FragmentActivity implements View.OnClickListener {
    private CommonActionBar actionbar;
    private ViewPager vp_rm;
    private FragmentPagerAdapter padapter;
    private TextView tv_first, tv_second, tv_1du, tv_2du;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rm_detail);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1du:
                vp_rm.setCurrentItem(0);
                break;
            case R.id.tv_2du:
                vp_rm.setCurrentItem(1);
                break;
        }
    }

    private void initListener() {
        actionbar.setBackListener(this);
        tv_1du.setOnClickListener(this);
        tv_2du.setOnClickListener(this);
        vp_rm.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tv_first.setVisibility(View.VISIBLE);
                    tv_second.setVisibility(View.GONE);
                } else {
                    tv_first.setVisibility(View.GONE);
                    tv_second.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", -1);
        vp_rm.setCurrentItem(type != -1 ? type : 0);
    }

    private void initData() {
        final List<Fragment> list = new ArrayList<Fragment>();
        RmFragment rm1 = new RmFragment();
        RmFragment rm2 = new RmFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("type", "one");
        Bundle bundle2 = new Bundle();
        bundle2.putString("type", "two");
        rm1.setArguments(bundle1);
        rm2.setArguments(bundle2);
        list.add(rm1);
        list.add(rm2);
        padapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        vp_rm.setAdapter(padapter);
    }

    private void initView() {
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        vp_rm = (ViewPager) findViewById(R.id.vp_rm);
        tv_first = (TextView) findViewById(R.id.tv_first);
        tv_second = (TextView) findViewById(R.id.tv_second);
        tv_1du = (TextView) findViewById(R.id.tv_1du);
        tv_2du = (TextView) findViewById(R.id.tv_2du);
    }
}

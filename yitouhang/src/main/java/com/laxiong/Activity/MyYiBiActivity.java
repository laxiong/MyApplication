package com.laxiong.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.View.CommonActionBar;
import com.laxiong.entity.User;
import com.laxiong.yitouhang.R;

public class MyYiBiActivity extends BaseActivity implements View.OnClickListener {
    private CommonActionBar actionbar;
    private RelativeLayout rl_shouru, rl_zhichu;
    private TextView tv_yibi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_yi_bi);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        User user= YiTouApplication.getInstance().getUser();
        if(user==null){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else{
            tv_yibi.setText(user.getScore()+"");
        }
    }

    private void initListener() {
        actionbar.setBackListener(this);
        rl_shouru.setOnClickListener(this);
        rl_zhichu.setOnClickListener(this);
    }

    private void initView() {
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        rl_shouru = (RelativeLayout) findViewById(R.id.rl_shouru);
        rl_zhichu = (RelativeLayout) findViewById(R.id.rl_zhichu);
        tv_yibi= (TextView) findViewById(R.id.tv_yibi);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MyYiBiActivity.this, YibiDetailActivity.class);
        switch (v.getId()) {
            case R.id.rl_shouru:
                intent.putExtra("type", "shouru");
                break;
            case R.id.rl_zhichu:
                intent.putExtra("type", "zhuchu");
                break;
        }
        startActivity(intent);
    }
}

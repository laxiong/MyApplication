package com.laxiong.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.laxiong.View.CommonActionBar;
import com.laxiong.yitouhang.R;

public class MyYiBiActivity extends BaseActivity {
    private CommonActionBar actionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_yi_bi);
        init();
    }

    private void init() {
        actionbar= (CommonActionBar) findViewById(R.id.actionbar);
        actionbar.setBackListener(this);
    }
}

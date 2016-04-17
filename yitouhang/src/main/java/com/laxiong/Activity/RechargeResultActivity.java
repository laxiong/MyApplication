package com.laxiong.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laxiong.yitouhang.R;

/**
 * Created by admin on 2016/4/8.
 */
public class RechargeResultActivity extends BaseActivity {
    /***
     * 充值结果页面
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_result);
        initView();

    }

    private void initView(){
        TextView mTitle = (TextView)findViewById(R.id.title);
        mTitle.setText("充值结果");
        FrameLayout mBack = (FrameLayout)findViewById(R.id.back_layout);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RechargeResultActivity.this.finish();
            }
        });

    }
}

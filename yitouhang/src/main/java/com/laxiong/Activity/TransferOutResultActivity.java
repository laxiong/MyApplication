package com.laxiong.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.carfriend.mistCF.R;

/**
 * Created by admin on 2016/5/24.
 */
public class TransferOutResultActivity extends BaseActivity implements View.OnClickListener{
    /***
     * 转出结果页面
     */

    private TextView mTitle ,finish_ok;
    private FrameLayout mBackLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferoutresult);
        initView();
    }

    private void initView(){
        mTitle = (TextView)findViewById(R.id.title);
        mTitle.setText("转出结果");
        mBackLayout =(FrameLayout)findViewById(R.id.back_layout);
        mBackLayout.setOnClickListener(this);
        TextView tranferout = (TextView)findViewById(R.id.tranferout_money);
        String money = getIntent().getStringExtra("money");
        tranferout.setText(money);
        finish_ok = (TextView)findViewById(R.id.finish_ok);
        finish_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                TransferOutResultActivity.this.finish();
                break;
            case R.id.finish_ok:
                TransferOutResultActivity.this.finish();
                break;

        }
    }
}

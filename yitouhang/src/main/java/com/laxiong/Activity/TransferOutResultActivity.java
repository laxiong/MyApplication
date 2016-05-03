package com.laxiong.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laxiong.yitouhang.R;

/**
 * Created by admin on 2016/4/29.
 */
public class TransferOutResultActivity extends BaseActivity implements View.OnClickListener{
    /***
     * 转出结果
     */
    private FrameLayout mBack ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranferout_result);
        initView();
        initData();
    }

    private void initData() {
        mBack.setOnClickListener(this);
    }

    private void initView() {
        TextView mTitle = (TextView)findViewById(R.id.title);
        mTitle.setText("转入");

        mBack = (FrameLayout)findViewById(R.id.back_layout);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_layout:
                this.finish();
                break;
        }
    }

}

package com.laxiong.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laxiong.yitouhang.R;

/**
 * Created by admin on 2016/4/26.
 */
public class VersionManageActivity extends BaseActivity implements View.OnClickListener{
    /****
     * 版本管理的
     */
    private FrameLayout mBack ;
    private TextView mTitle ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_manage);
        initView();

    }
    private void initView(){
        mBack =(FrameLayout)findViewById(R.id.back_layout);
        mTitle =(TextView)findViewById(R.id.title);
        mTitle.setText("版本管理");

        mBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                this.finish();
                break;
        }
    }
}

package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.laxiong.Fragment.ResetPayPwdFrag1;
import com.laxiong.yitouhang.R;

public class ResetPayPwdActivity extends BaseActivity implements View.OnClickListener{
    private TextView mText;
    private FrameLayout mBack;
    private FragmentManager manager;
    private FragmentTransaction transac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pay_pwd);
        initView();
        initData();
        initListener();
    }
    public void initView(){
        mBack = (FrameLayout)findViewById(R.id.back_layout);
        mText = (TextView)findViewById(R.id.title);
    }
    @SuppressLint("NewApi")
    private void initData(){
        mText.setText("重置支付密码");
        manager=this.getFragmentManager();
        transac=manager.beginTransaction();
        ResetPayPwdFrag1 frag1=new ResetPayPwdFrag1();
        transac.replace(R.id.ll_fragment,frag1,"flag1");
        transac.addToBackStack("flag1");
        transac.commit();
    }
    @SuppressLint("NewApi")
    public void setFragment(Fragment fragment,String name){
        transac=manager.beginTransaction();
        transac.replace(R.id.ll_fragment,fragment,name);
        transac.addToBackStack(name);
        transac.commit();
    }
    private void initListener(){
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

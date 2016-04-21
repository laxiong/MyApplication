package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.laxiong.Fragment.ChangePayPwdFragment1;
import com.laxiong.Fragment.ChangePayPwdFragment2;
import com.laxiong.Mvp_presenter.Password_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.yitouhang.R;

public class ChangePayPwdActivity extends BaseActivity implements View.OnClickListener, ChangePayPwdFragment1.InterChangePwd1
        , ChangePayPwdFragment2.InterChangePwd2, IViewCommonBack {
    private FrameLayout mBack;
    private FragmentManager manager;
    private FragmentTransaction transac;
    private String pwd, code, newpwd;
    private Password_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pay_pwd);
        initView();
        initData();
        initListener();
    }

    //记录新密码并提交
    @Override
    public void recordPwdNew(String newpwd) {
        this.newpwd = newpwd;
        presenter.reqChangePayPwd(this,pwd,newpwd,code);
    }

    @Override
    public void recordCode(String code) {
        this.code = code;
    }

    @Override
    public void reqbackSuc() {
        ToastUtil.customAlert(this, "提交成功");
        Intent intent = new Intent(this, PswdConturalActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    @Override
    public void reqbackFail(String msg) {
        ToastUtil.customAlert(this, msg);
        finish();
        return;
    }

    public void initView() {
        mBack = (FrameLayout) findViewById(R.id.back_layout);
    }

    public void initListener() {
        mBack.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    public void initData() {
        presenter = new Password_Presenter(this);
        Intent intent = getIntent();
        pwd = intent.getStringExtra("pwd");
        if (StringUtils.isBlank(pwd)) {
            intent = new Intent(this, PswdConturalActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        manager = this.getFragmentManager();
        transac = manager.beginTransaction();
        ChangePayPwdFragment1 fragment1 = new ChangePayPwdFragment1();
        transac.add(R.id.ll_frag, fragment1, "frag1");
        transac.commit();
    }

    @SuppressLint("NewApi")
    public void setFragment(Fragment fragment, String name) {
        transac = manager.beginTransaction();
        transac.replace(R.id.ll_frag, fragment, name);
        transac.addToBackStack(name);
        transac.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
        }
    }
}

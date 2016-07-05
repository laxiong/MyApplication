package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Fragment.ResetPayPwdFrag1;
import com.laxiong.Fragment.ResetPayPwdFrag2;
import com.laxiong.Fragment.ResetPayPwdFrag3;
import com.laxiong.Mvp_presenter.Password_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.carfriend.mistCF.R;
public class ResetPayPwdActivity extends BaseActivity implements View.OnClickListener, ResetPayPwdFrag1.InterFirst,
        ResetPayPwdFrag2.InterSecond, ResetPayPwdFrag3.InterThird, IViewCommonBack {
    private TextView mText;
    private FrameLayout mBack;
    private FragmentManager manager;
    private FragmentTransaction transac;
    private String name, identi, vali, pwd;
    private Password_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pay_pwd);
        initView();
        initData();
        initListener();
    }

    @Override
    public void reqbackSuc(String tag) {
        ToastUtil.customAlert(this, "找回密码成功");
        this.finish();
    }

    @Override
    public void reqbackFail(String msg,String tag) {
        ToastUtil.customAlert(this, msg);
    }

    public void initView() {
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        mText = (TextView) findViewById(R.id.title);
    }

    @SuppressLint("NewApi")
    private void initData() {
        presenter=new Password_Presenter(this);
        mText.setText("重置支付密码");
        manager = this.getFragmentManager();
        transac = manager.beginTransaction();
        ResetPayPwdFrag1 frag1 = new ResetPayPwdFrag1();
        transac.add(R.id.ll_fragment, frag1, "flag1");
        transac.commit();
    }

    //记录姓名和身份证号
    @Override
    public void recordNameIdenti(String name, String identi) {
        this.name = name;
        this.identi = identi;
    }

    //记录验证码
    @Override
    public void recordVali(String vali) {
        this.vali = vali;
    }

    //记录新的支付密码
    @Override
    public void recordNewPwd(String pwd) {
        this.pwd = pwd;
        resetPwd();
    }

    @SuppressLint("NewApi")
    public void setFragment(Fragment fragment, String name) {
        transac = manager.beginTransaction();
        transac.replace(R.id.ll_fragment, fragment, name);
        transac.addToBackStack(name);
        transac.commit();
    }

    private void initListener() {
        mBack.setOnClickListener(this);
    }

    public void resetPwd() {
        ToastUtil.customAlert(this,"有点问题");
        if (StringUtils.testBlankAll(name, vali, identi, pwd)) {
            Toast.makeText(this, "重置支付密码失败,参数有错误", Toast.LENGTH_LONG).show();
            ResetPayPwdActivity.this.finish();
        } else {
            presenter.reqResetPayPwd(this,name,vali,identi,pwd);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
        }
    }
}

package com.laxiong.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Basic.OnSingleClickListener;
import com.laxiong.Common.Common;
import com.laxiong.Mvp_presenter.Login_Presenter;
import com.laxiong.Mvp_view.IViewLogin;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.gongshidai.mistGSD.R;
public class LoginActivity extends BaseActivity implements OnClickListener, IViewLogin {
    /***
     * 登录
     */
    private TextView mRegistBtn, mLoginBtn, mFindPswd, mChangeCount, tv_phonenum;
    private FrameLayout mBack;
    private ImageView mShowPswd;
    private EditText mPswd;
    private Login_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        initView();
        initData();
    }

    @Override
    public String getInputPhoneNum() {
        return SpUtils.getStrValue(SpUtils.getSp(this), SpUtils.USER_KEY);
    }

    @Override
    public String getInputPwd() {
        return mPswd.getText().toString();
    }

    private void initData() {
        presenter = new Login_Presenter(this);
        mRegistBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(new OnSingleClickListener(this) {
            @Override
            public void onSingleClick(View v) {
                if (valifiylogin())
                    presenter.login(LoginActivity.this);
            }
        });
        mFindPswd.setOnClickListener(this);
        mChangeCount.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
        updateButton(false);
        mPswd.addTextChangedListener(presenter.getTextWatcher());
        if (StringUtils.isBlank(getInputPhoneNum())) {
            Intent intent = new Intent(this, ChangeCountActivity.class);
            startActivity(intent);
            finish();
            return;
        } else
            tv_phonenum.setText(StringUtils.getProtectedMobile(getInputPhoneNum()));
    }

    @Override
    public void loginsuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this, MainActivity.class);
//        this.startActivity(intent);
        boolean isBack=getIntent().getBooleanExtra("isBack",false);
        Intent intent=new Intent(this,ModifyGestureActivity.class);
        if(isBack)
            setResult(RESULT_OK);
        else {
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void updateButton(boolean isabled) {
        if (!isabled) {
            mLoginBtn.setEnabled(false);
            mLoginBtn.setBackgroundResource(R.drawable.button_grey_corner_border);
        } else {
            mLoginBtn.setEnabled(true);
            mLoginBtn.setBackgroundResource(R.drawable.button_change_bg_border);
        }
    }

    @Override
    public void loginfailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void initView() {
        mRegistBtn = (TextView) findViewById(R.id.registBtn);
        mLoginBtn = (TextView) findViewById(R.id.loginBtn);
        mChangeCount = (TextView) findViewById(R.id.changecount);
        mFindPswd = (TextView) findViewById(R.id.findpswd);
        mBack = (FrameLayout) findViewById(R.id.backlayout);
        mShowPswd = (ImageView) findViewById(R.id.img_showpswd);
        mPswd = (EditText) findViewById(R.id.password);
        tv_phonenum = (TextView) findViewById(R.id.tv_phonenum);
    }

    public boolean valifiylogin() {
        if (StringUtils.isBlank(mPswd.getText().toString())) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registBtn:
                startActivity(new Intent(LoginActivity.this,
                        RegistActivity.class));
                break;
            case R.id.changecount:
                startActivity(new Intent(LoginActivity.this,
                        ChangeCountActivity.class));
                break;
            case R.id.findpswd:
                startActivity(new Intent(LoginActivity.this,
                        FoundPswdActivity.class));
                break;
            case R.id.backlayout:
                this.finish();
                break;
            case R.id.img_showpswd:
                showPassWord();
                break;
        }
    }

//    private boolean valifiylogin() {
//        if (StringUtils.isBlank(mPswd.getText().toString())) {
//            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        return true;
//    }

    // show password
    private boolean isShowed = false;

    private void showPassWord() {
        if (isShowed) { // 隐藏
            mShowPswd.setImageResource(R.drawable.img_eye_close);
            mPswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isShowed = false;
        } else {        //显示
            mShowPswd.setImageResource(R.drawable.img_eye_open);
            mPswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isShowed = true;
        }
    }


}

package com.laxiong.Activity;

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

import com.laxiong.Mvp_presenter.Login_Presenter;
import com.laxiong.Mvp_view.IViewLogin;
import com.laxiong.Utils.SpUtils;
import com.laxiong.yitouhang.R;

public class ChangeCountActivity extends BaseActivity implements OnClickListener, IViewLogin {
    /***
     * 切换账号
     */
    private TextView mRegistBtn, mFindPswd, mComplete;
    private FrameLayout mBack;
    private ImageView mShowPswd;
    private EditText mPswd, mphone;
    private Login_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changecount_layout);
        initView();
        initData();
    }

    @Override
    public String getInputPhoneNum() {
        Log.i("kk", mphone.getText().toString());
        return mphone.getText().toString();
    }

    @Override
    public String getInputPwd() {
        Log.i("kk", mPswd.getText().toString());
        return mPswd.getText().toString();
    }

    @Override
    public void loginsuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void loginfailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void initData() {
        presenter = new Login_Presenter(this);
        mFindPswd.setOnClickListener(this);
        mRegistBtn.setOnClickListener(this);
        mComplete.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
    }

    private void initView() {
        mphone = (EditText) findViewById(R.id.et_phone);
        mRegistBtn = (TextView) findViewById(R.id.registBtn);
        mFindPswd = (TextView) findViewById(R.id.findpswd);
        mComplete = (TextView) findViewById(R.id.completeBtn);
        mBack = (FrameLayout) findViewById(R.id.backlayout);

        mPswd = (EditText) findViewById(R.id.pswd);
        mShowPswd = (ImageView) findViewById(R.id.img_showpswd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registBtn:
                startActivity(new Intent(ChangeCountActivity.this,
                        RegistActivity.class));
                break;
            case R.id.findpswd:
                startActivity(new Intent(ChangeCountActivity.this,
                        FoundPswdActivity.class));
                break;
            case R.id.backlayout:
                ChangeCountActivity.this.finish();
                break;
            case R.id.completeBtn:
                presenter.login(ChangeCountActivity.this);
                break;
            case R.id.img_showpswd:
                showPassWord();
                break;
        }

    }

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

package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Mvp_presenter.Login_Presenter;
import com.laxiong.Mvp_view.IViewLogin;
import com.laxiong.Utils.StringUtils;
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
        initListener();
    }

    @Override
    public String getInputPhoneNum() {
        return mphone.getText().toString();
    }

    @Override
    public String getInputPwd() {
        return mPswd.getText().toString();
    }

    //当TextChange的时候改变button
    @Override
    public void updateButton(boolean isabled) {
        if (!isabled) {
            mComplete.setEnabled(false);
            mComplete.setBackgroundResource(R.drawable.button_grey_corner_border);
        } else {
            mComplete.setEnabled(true);
            mComplete.setBackgroundResource(R.drawable.button_change_bg_border);
        }
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
        updateButton(false);
    }

    private void initListener() {
        mFindPswd.setOnClickListener(this);
        mRegistBtn.setOnClickListener(this);
        mComplete.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
        TextWatcher watcher = presenter.getTextWatcher();
        mphone.addTextChangedListener(watcher);
        mPswd.addTextChangedListener(watcher);
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
                if (valifyLogin())
                    presenter.login(ChangeCountActivity.this);
                break;
            case R.id.img_showpswd:
                showPassWord();
                break;
        }

    }

    public boolean valifyLogin() {
        if (StringUtils.isBlank(mPswd.getText().toString()) || StringUtils.isBlank(mphone.getText().toString())) {
            Toast.makeText(this, "账号密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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

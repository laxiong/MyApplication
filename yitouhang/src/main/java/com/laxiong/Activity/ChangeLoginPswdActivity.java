package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Mvp_presenter.Password_Presenter;
import com.laxiong.Mvp_view.IViewChangePwd;
import com.laxiong.Utils.StringUtils;
import com.gongshidai.mistGSD.R;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;

public class ChangeLoginPswdActivity extends BaseActivity implements OnClickListener, IViewChangePwd {
    /***
     * 修改登录密码
     */
    private FrameLayout mBack;
    private TextView mComplete;
    private EditText mPswd, mOldPwd;
    private ImageView mShowPswd;
    private Password_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_login_pswd);
        initView();
        initData();
    }

    private void initData() {
        presenter = new Password_Presenter(this);
        mBack.setOnClickListener(this);
        mComplete.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
    }

    @Override
    public String getOldPwd() {
        return mOldPwd.getText().toString();
    }

    @Override
    public String getNewPwd() {
        return mPswd.getText().toString();
    }

    @Override
    public void updateSuc() {
        Toast.makeText(this, "密码修改成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void updateFailure(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }

    private void initView() {
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        mComplete = (TextView) findViewById(R.id.complete);

        TextView mText = (TextView) findViewById(R.id.title);
        mText.setText("修改登录密码");

        mPswd = (EditText) findViewById(R.id.pswd);
        mOldPwd = (EditText) findViewById(R.id.et_oldpwd);
        mShowPswd = (ImageView) findViewById(R.id.img_showpswd);
    }

    public boolean validatePwd() {
        if (StringUtils.isBlank(mOldPwd.getText().toString()) || StringUtils.isBlank(mPswd.getText().toString())) {
            Toast.makeText(this, "新旧密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ValifyUtil.valifyPwd(mOldPwd.getText().toString())||!ValifyUtil.valifyPwd(mPswd.getText().toString()))
            return false;
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
            case R.id.complete:
                if (validatePwd())
                    presenter.reqChangePwd(this);
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

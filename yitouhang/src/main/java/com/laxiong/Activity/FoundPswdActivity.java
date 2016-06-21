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

import com.laxiong.Common.Constants;
import com.laxiong.Mvp_presenter.Handler_Presenter;
import com.laxiong.Mvp_presenter.Password_Presenter;
import com.laxiong.Mvp_view.IViewReBackPwd;
import com.laxiong.Mvp_view.IViewTimerHandler;
import com.laxiong.Utils.StringUtils;
import com.gongshidai.mistGSD.R;
import com.laxiong.Utils.ValifyUtil;

public class FoundPswdActivity extends BaseActivity implements OnClickListener, IViewReBackPwd,IViewTimerHandler{
    /****
     * 找回密码
     */
    private TextView mCompletBtn, mGetValidation;
    private FrameLayout mBack;
    private EditText mPswd, et_phonenum, et_valicode;
    private ImageView mShowPswd;
    private Password_Presenter presenter;
    private Handler_Presenter timepresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundpswd_layout);
        initView();
        initData();
    }

    private void initData() {
        mCompletBtn.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
        mGetValidation.setOnClickListener(this);
        presenter = new Password_Presenter(this);
        timepresenter=new Handler_Presenter(this);
    }

    @Override
    public void handlerViewByTime(int seconds) {
        if(seconds>0){
            mGetValidation.setEnabled(false);
            mGetValidation.setText(seconds+"s");
        }else{
            mGetValidation.setEnabled(true);
            mGetValidation.setText("获取验证码");
        }
    }

    @Override
    public String getTextPhone() {
        return et_phonenum.getText().toString();
    }

    @Override
    public void getCodeSuccess() {
        timepresenter.loadHandlerTimer(Constants.INTERVAL,Constants.TIME);
        Toast.makeText(this, "获取验证码成功,将会发送到手机", Toast.LENGTH_LONG).show();
    }

    @Override
    public void getCodeFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void initView() {
        mCompletBtn = (TextView) findViewById(R.id.complete_Btn);
        mBack = (FrameLayout) findViewById(R.id.back);
        mShowPswd = (ImageView) findViewById(R.id.img_showpswd);
        mPswd = (EditText) findViewById(R.id.pswd);
        mGetValidation = (TextView) findViewById(R.id.tv_getVali);
        et_phonenum = (EditText) findViewById(R.id.et_phonenum);
        et_valicode = (EditText) findViewById(R.id.et_valicode);
    }

    //密码找回成功
    @Override
    public void reBackSuccess() {
        Toast.makeText(this, "找回密码成功!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ChangeCountActivity.class);
        startActivity(intent);
    }

    //密码找回失败
    @Override
    public void reBackFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //获取输入的密码
    @Override
    public String getTextPwd() {
        return mPswd.getText().toString();
    }

    //获取输入的验证码
    @Override
    public String getValiCode() {
        return et_valicode.getText().toString();
    }

    //校验手机号
    public boolean validatePhone() {
        if (!ValifyUtil.valifyPhoneNum(et_phonenum.getText().toString())) {
            Toast.makeText(this, "手机号码格式不对", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    //校验所有
    public boolean validateAll() {
        boolean flag=StringUtils.testBlankAll(et_phonenum.getText().toString(),
                et_valicode.getText().toString(), mPswd.getText().toString());
        if(flag)
            Toast.makeText(this,"有必选项未填!",Toast.LENGTH_LONG).show();
        return !flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete_Btn:
                if(validateAll())
                    presenter.reqReBackPwd(this);
                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.img_showpswd:
                showPassWord();
                break;
            case R.id.tv_getVali:
                if (validatePhone()) {
                    presenter.reqValidation(FoundPswdActivity.this);
                }
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

package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ValifyUtil;
import com.carfriend.mistCF.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiejin on 2016/4/20.
 * Types ResetPayPwdFrag1.java
 */

@SuppressLint("NewApi")
public class ResetPayPwdFrag3 extends Fragment implements View.OnClickListener {
    /****
     * 重置支付密码第三层
     */
    private TextView mComplete;
    private ImageView mShowPswd;
    private EditText mPswdEd;
    private View layout;
    private InterThird interthird;

    public interface InterThird {
        public void recordNewPwd(String pwd);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.activity_resetpay_pswd3, null);
        initView();
        initData();
        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        interthird = (InterThird) activity;
    }

    private void initData() {
        Bundle bundle = getArguments();
        mPswdEd.addTextChangedListener(new BasicWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String pwd = mPswdEd.getText().toString();
                if (!ValifyUtil.valifyPwd(pwd)) {
                    ValifyUtil.setEnabled(mComplete, false);
                } else {
                    ValifyUtil.setEnabled(mComplete, true);
                }
            }
        });
        mComplete.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
        ValifyUtil.setEnabled(mComplete, false);
    }

    private void initView() {
        mComplete = (TextView) layout.findViewById(R.id.complete);

        mShowPswd = (ImageView) layout.findViewById(R.id.img_showpswd);
        mPswdEd = (EditText) layout.findViewById(R.id.pswd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete:
                if (interthird != null)
                    interthird.recordNewPwd(mPswdEd.getText().toString());
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
            mPswdEd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isShowed = false;
        } else {        //显示
            mShowPswd.setImageResource(R.drawable.img_eye_open);
            mPswdEd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isShowed = true;
        }
    }
}

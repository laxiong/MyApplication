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

import com.laxiong.Activity.ChangePayPwdActivity;
import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ValifyUtil;
import com.gongshidai.mistGSD.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiejin on 2016/4/21.
 * Types ChangePayPwdFragment1.java
 */
@SuppressLint("NewApi")
public class ChangePayPwdFragment2 extends Fragment implements View.OnClickListener {
    private View layout;
    private TextView mComplete;
    private ImageView mShowPswd;
    private EditText mPswdEd;
    private InterChangePwd2 inter2;

    public interface InterChangePwd2 {
        public void recordPwdNew(String pwd);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_change_pay_pwd_fragment2, null);
        initView();
        initData();
        return layout;
    }

    private void initData() {
        mComplete.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inter2 = (InterChangePwd2) activity;
    }

    private void initView() {
        mComplete = (TextView) layout.findViewById(R.id.complete);

        mShowPswd = (ImageView) layout.findViewById(R.id.img_showpswd);
        mPswdEd = (EditText) layout.findViewById(R.id.pswd);
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
        ValifyUtil.setEnabled(mComplete, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete:
                if (inter2 != null)
                    inter2.recordPwdNew(mPswdEd.getText().toString());
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

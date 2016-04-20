package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.yitouhang.R;

/**
 * Created by xiejin on 2016/4/20.
 * Types ResetPayPwdFrag1.java
 */

@SuppressLint("NewApi")
public class ResetPayPwdFrag3 extends Fragment implements View.OnClickListener{
    /****
     * 重置支付密码第三层
     */
    private TextView mComplete ;
    private ImageView mShowPswd ;
    private EditText mPswdEd ;
    private View layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout=inflater.inflate(R.layout.activity_resetpay_pswd3,null);
        initView();
        initData();
        return layout;
    }
    private void initData() {
        mComplete.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
    }

    private void initView() {
        mComplete = (TextView)layout.findViewById(R.id.complete);

        mShowPswd = (ImageView)layout.findViewById(R.id.img_showpswd);
        mPswdEd = (EditText)layout.findViewById(R.id.pswd);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.complete:
                Toast.makeText(getActivity(), "完成", Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_showpswd:
                showPassWord();
                break;
        }
    }
    // show password
    private boolean isShowed = false ;
    private void showPassWord(){
        if(isShowed){ // 隐藏
            mShowPswd.setImageResource(R.drawable.img_eye_close);
            mPswdEd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isShowed = false;
        }else{		//显示
            mShowPswd.setImageResource(R.drawable.img_eye_open);
            mPswdEd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isShowed = true;
        }
    }
}

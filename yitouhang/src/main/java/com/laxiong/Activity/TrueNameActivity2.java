package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.CommonReq_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.loopj.android.http.RequestParams;
import com.gongshidai.mistGSD.R;
public class TrueNameActivity2 extends BaseActivity implements OnClickListener, IViewCommonBack {
    /***
     * 实名认证第二步
     */
    private TextView mNextPage;
    private FrameLayout mBack;
    private ImageView mShowPswd;
    private EditText mPswdEd;
    private CommonReq_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_name2);
        initView();
        initData();
    }

    @Override
    public void reqbackSuc(String tag) {
        ToastUtil.customAlert(this, "设置支付密码成功!");
        startActivity(new Intent(TrueNameActivity2.this,
                TrueNameActivity3.class));
        this.finish();
    }

    @Override
    public void reqbackFail(String msg, String tag) {
        ToastUtil.customAlert(this, msg);
    }

    private void initData() {
        presenter = new CommonReq_Presenter(this);
        mNextPage.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
    }

    private void initView() {
        mNextPage = (TextView) findViewById(R.id.nextpager);
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        TextView mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("实名认证");

        mShowPswd = (ImageView) findViewById(R.id.img_showpswd);
        mPswdEd = (EditText) findViewById(R.id.pswd);
        ValifyUtil.setEnabled(mNextPage, false);
        mPswdEd.addTextChangedListener(new BasicWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isBlank(s.toString()) && ValifyUtil.valifyPwd(s.toString())) {
                    ValifyUtil.setEnabled(mNextPage, true);
                } else {
                    ValifyUtil.setEnabled(mNextPage, false);
                }
            }
        });
    }

    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.nextpager:
                RequestParams params = new RequestParams();
                params.put("type", Constants.ReqEnum.PAYPWD.getVal());
                params.put("pay_pwd", mPswdEd.getText().toString());
                presenter.aureqByPut(InterfaceInfo.USER_URL, TrueNameActivity2.this, params, null);
                break;
            case R.id.back_layout:
                this.finish();
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

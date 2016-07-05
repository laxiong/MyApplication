package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.BindPhone_Presenter;
import com.laxiong.Mvp_presenter.CommonReq_Presenter;
import com.laxiong.Mvp_presenter.Handler_Presenter;
import com.laxiong.Mvp_view.IViewBindPhone;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Mvp_view.IViewTimerHandler;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.carfriend.mistCF.R;
import com.laxiong.Utils.ValifyUtil;
import com.loopj.android.http.RequestParams;

public class ChangeBindPhoneActivity2 extends BaseActivity implements OnClickListener, IViewBindPhone, IViewTimerHandler {
    /***
     * 修改手机号的第二层
     */
    private TextView finishBtn, mCode;
    private FrameLayout mBack;
    private EditText et_phone, et_code;
    private BindPhone_Presenter bindpresenter;
    private Handler_Presenter timepresenter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changebindphone2);
        initView();
        initData();
    }

    @Override
    public String getToken() {
        return token;
    }

    private void initData() {
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        bindpresenter = new BindPhone_Presenter(this);
        timepresenter = new Handler_Presenter(this);
        finishBtn.setOnClickListener(this);
        mCode.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public String getPhone() {
        return et_phone.getText().toString().trim();
    }

    @Override
    public String getCode() {
        return et_code.getText().toString().trim();
    }

    @Override
    public void reqbackSuc(String tag) {
        if (tag.equals(bindpresenter.TYPE_CODE)) {
            ToastUtil.customAlert(this, "获取验证码成功!");
            timepresenter.loadHandlerTimer(Constants.INTERVAL, Constants.TIME);
        } else if (tag.equals(bindpresenter.TYPE_BIND)) {
            ToastUtil.customAlert(this, "更换手机号码成功!");
            SpUtils.saveStrValue(SpUtils.getSp(this), SpUtils.USER_KEY, getPhone());
            Intent intent = new Intent(this, PersonalSettingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void reqbackFail(String msg, String tag) {
        ToastUtil.customAlert(this, msg);
    }

    @Override
    public void handlerViewByTime(int seconds) {
        if (seconds > 0) {
            mCode.setEnabled(false);
            mCode.setText(seconds + "s");
        } else {
            mCode.setEnabled(true);
            mCode.setText("获取验证码");
        }
    }

    private void initView() {

        finishBtn = (TextView) findViewById(R.id.finishBtn);
        mCode = (TextView) findViewById(R.id.code);
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        et_code = (EditText) findViewById(R.id.et_code);
        et_phone = (EditText) findViewById(R.id.et_phone);
        TextView mText = (TextView) findViewById(R.id.title);
        mText.setText("修改手机");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finishBtn:        /** 完成按钮**/
                bindpresenter.bindOtherPhone(this);
                break;
            case R.id.code:
                if (StringUtils.isBlank(getPhone())) {
                    ToastUtil.customAlert(ChangeBindPhoneActivity2.this, "请先输入手机号码");
                } else {
                    if (!ValifyUtil.valifyPhoneNum(getPhone())) {
                        ToastUtil.customAlert(ChangeBindPhoneActivity2.this, "手机号码格式不对");
                    } else
                        bindpresenter.sendCode(ChangeBindPhoneActivity2.this);
                }
                break;
            case R.id.back_layout:
                this.finish();
                break;
        }
    }

}

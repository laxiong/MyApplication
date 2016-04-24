package com.laxiong.Activity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Common.Constants;
import com.laxiong.Mvp_presenter.BankCard_Presenter;
import com.laxiong.Mvp_presenter.Handler_Presenter;
import com.laxiong.Mvp_view.IViewBankCard;
import com.laxiong.Mvp_view.IViewTimerHandler;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.laxiong.yitouhang.R;

public class TrueNameActivity3 extends BaseActivity implements OnClickListener, IViewTimerHandler, IViewBankCard {
    /****
     * 实名认证第三步
     */
    private TextView mFinish;
    private FrameLayout mBack;
    private ImageView toggleRead, mShowPswd;
    private EditText et_card, et_phone, et_name, et_code;
    private TextView tv_getCode;
    private Handler_Presenter timepresenter;
    private BankCard_Presenter bpresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_name3);
        initView();
        initData();
    }

    @Override
    public String getPhone() {
        return et_phone.getText().toString();
    }

    @Override
    public void reqbackSuc(String tag) {
        if (BankCard_Presenter.TYPE_CODE.equals(tag)) {
            ToastUtil.customAlert(this, "验证码获取成功!");
        } else if (BankCard_Presenter.TYPE_CARD.equals(tag)) {
            ToastUtil.customAlert(this, "绑定银行卡成功!");
        }
    }

    @Override
    public void reqbackFail(String msg, String tag) {
        ToastUtil.customAlert(this, msg);
    }

    @Override
    public void handlerViewByTime(int seconds) {
        if (seconds > 0) {
            tv_getCode.setText(seconds + "s");
        } else {
            tv_getCode.setText("获取验证码");
        }
    }

    private void initData() {
        timepresenter = new Handler_Presenter(this);
        bpresenter = new BankCard_Presenter(this);
        mFinish.setOnClickListener(this);
        mBack.setOnClickListener(this);
        toggleRead.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
        ValifyUtil.setEnabled(mFinish, false);
        TextWatcher tw = new BasicWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.testBlankAll(et_name.getText().toString(), et_card.getText().toString()
                        , et_code.getText().toString()) && ValifyUtil.valifyPhoneNum(et_phone.getText().toString())) {
                    ValifyUtil.setEnabled(mFinish, true);
                } else {
                    ValifyUtil.setEnabled(mFinish, false);
                }
            }
        };
        et_card.addTextChangedListener(tw);
        et_code.addTextChangedListener(tw);
        et_name.addTextChangedListener(tw);
        et_phone.addTextChangedListener(tw);
    }

    private void initView() {
        mFinish = (TextView) findViewById(R.id.fininsh);
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        toggleRead = (ImageView) findViewById(R.id.toggle);
        TextView mTitle = (TextView) findViewById(R.id.title);
        tv_getCode = (TextView) findViewById(R.id.tv_getCode);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_name = (EditText) findViewById(R.id.et_name);
        et_code = (EditText) findViewById(R.id.et_code);
        mTitle.setText("实名认证");

        et_card = (EditText) findViewById(R.id.et_card);
        mShowPswd = (ImageView) findViewById(R.id.img_showpswd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fininsh:
                bpresenter.bindCard(this);
                showFinishDialog();
                break;
            case R.id.back_layout:
                startActivity(new Intent(this,
                        PersonalSettingActivity.class));    // 启动到SingTask模式
                this.finish();
                break;
            case R.id.toggle:
                readProtocol();
                break;
            case R.id.img_showpswd:
                showPassWord();
                break;
            case R.id.tv_getCode:
                if (ValifyUtil.valifyPhoneNum(et_phone.getText().toString())) {
                    bpresenter.sendCode(TrueNameActivity3.this);
                    timepresenter.loadHandlerTimer(Constants.INTERVAL, Constants.TIME);
                } else {
                    ToastUtil.customAlert(TrueNameActivity3.this, "手机号格式不对");
                }
                break;
        }
    }

    /***
     * 阅读协议
     */
    boolean isRead = true;

    private void readProtocol() {
        if (isRead) {
            toggleRead.setImageResource(R.drawable.img_no_read);
            isRead = false;
        } else {
            toggleRead.setImageResource(R.drawable.img_read);
            isRead = true;
        }
    }

    // show password
    private boolean isShowed = false;

    private void showPassWord() {
        if (isShowed) { // 隐藏
            mShowPswd.setImageResource(R.drawable.img_eye_close);
            et_card.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isShowed = false;
        } else {        //显示
            mShowPswd.setImageResource(R.drawable.img_eye_open);
            et_card.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isShowed = true;
        }
    }

    /****
     * 认证通过现实的PopupWindow
     */
    private PopupWindow mWindows;
    private View showView;

    private void showFinishDialog() {

        showView = LayoutInflater.from(this).inflate(R.layout.true_namefinish_popwindow, null);
        TextView kown = (TextView) showView.findViewById(R.id.kown);

        kown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mWindows != null && mWindows.isShowing()) {
                    mWindows.dismiss();
                    mWindows = null;
                }
            }
        });

        mWindows = new PopupWindow(showView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        mWindows.setTouchable(true);
        mWindows.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mWindows.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mWindows.showAtLocation(showView, Gravity.BOTTOM, 0, 0);

    }


}

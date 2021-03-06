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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carfriend.mistCF.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Mvp_presenter.BankCard_Presenter;
import com.laxiong.Mvp_view.IViewBindCard;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;

public class TrueNameActivity3 extends BaseActivity implements OnClickListener, IViewBindCard {
    /****
     * 实名认证第三步
     */
    private TextView mFinish, tv_bank;
    private RelativeLayout rl_bank;
    private FrameLayout mBack;
    private ImageView toggleRead, mShowPswd;
    private EditText et_card, et_phone, et_name;
    private BankCard_Presenter bpresenter;
    private static final int REQUEST_CODE = 1;
    private boolean bselected = false;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_name3);
        initView();
        initData();
    }

    @Override
    public String getName() {
        return et_name.getText().toString();
    }

    @Override
    public String getCardNum() {
        return et_card.getText().toString();
    }

    @Override
    public String getCardId() {
        return id;
    }

    @Override
    public String getPhoneNum() {
        return et_phone.getText().toString();
    }

    @Override
    public void reqbackSuc(String tag) {
        if (BankCard_Presenter.TYPE_CARD.equals(tag)) {
            CommonReq.reqUserMsg(this);
            YiTouApplication.getInstance().getUser().setBankcount(1);
            ToastUtil.customAlert(this, "绑定银行卡成功!");
            showFinishDialog();
        }
    }

    @Override
    public void reqbackFail(String msg, String tag) {
        ToastUtil.customAlert(this, msg);
    }


    private void initData() {
        bpresenter = new BankCard_Presenter(this);
        mFinish.setOnClickListener(this);
        mBack.setOnClickListener(this);
        toggleRead.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
        rl_bank.setOnClickListener(this);
        ValifyUtil.setEnabled(mFinish, false);
        TextWatcher tw = new BasicWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                valify();
            }
        };
        et_card.addTextChangedListener(tw);
        et_name.addTextChangedListener(tw);
        et_phone.addTextChangedListener(tw);
    }
    private void valify(){
        if (ValifyUtil.valifySpecial(et_name.getText().toString().trim())&&!StringUtils.testBlankAll(et_name.getText().toString(), et_card.getText().toString())
                && ValifyUtil.valifyPhoneNum(et_phone.getText().toString()) && bselected && !StringUtils.isBlank(id)&&isRead) {
            ValifyUtil.setEnabled(mFinish, true);
        } else {
            ValifyUtil.setEnabled(mFinish, false);
        }
    }
    private void initView() {
        mFinish = (TextView) findViewById(R.id.fininsh);
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        toggleRead = (ImageView) findViewById(R.id.toggle);
        TextView mTitle = (TextView) findViewById(R.id.title);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_name = (EditText) findViewById(R.id.et_name);
        rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
        tv_bank = (TextView) findViewById(R.id.tv_bank);
        mTitle.setText("绑定银行卡");

        et_card = (EditText) findViewById(R.id.et_card);
        mShowPswd = (ImageView) findViewById(R.id.img_showpswd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fininsh:
                bpresenter.bindCard(this);
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
            case R.id.rl_bank:
                startActivityForResult(new Intent(this, BankListActivity.class), REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            String name = data.getStringExtra("bankname");
            if (!StringUtils.isBlank(name)) {
                tv_bank.setText(name);
                bselected = true;
                id = data.getStringExtra("id");
            }
        }
    }

    /***
     * 阅读协议
     */
    boolean isRead = false;

    private void readProtocol() {
        if (!isRead) {
            toggleRead.setImageResource(R.drawable.img_read);
            isRead = true;
        } else {
            toggleRead.setImageResource(R.drawable.img_no_read);
            isRead = false;
        }
        valify();
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
        TextView invest = (TextView) showView.findViewById(R.id.tv_go);
        kown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mWindows != null && mWindows.isShowing()) {
                    mWindows.dismiss();
                    mWindows = null;
                    startActivity(new Intent(TrueNameActivity3.this,CountSettingActivity.class));
                    finish();
                }
            }
        });
        invest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindows.dismiss();
                mWindows = null;
                startActivity(new Intent(TrueNameActivity3.this, MainActivity.class).putExtra("jumptoinvest", true));
                finish();
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

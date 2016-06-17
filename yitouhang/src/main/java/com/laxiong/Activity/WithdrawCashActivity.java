package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Common.Settings;
import com.laxiong.InputMethod.LPTextField;
import com.laxiong.Mvp_model.BankCard;
import com.laxiong.Mvp_presenter.BankCard_Presenter;
import com.laxiong.Mvp_presenter.Login_Presenter;
import com.laxiong.Mvp_view.IViewWithdraw;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.laxiong.entity.User;

import java.text.DecimalFormat;

public class WithdrawCashActivity extends BaseActivity implements OnClickListener, IViewWithdraw {
    /***
     * 提现
     */
    private FrameLayout mBack;
    private TextView mNextBtn;
    private TextView mNotice;
    private ImageView mToggleBtn;
    private TextView money, tv_edu, tv_bank, tv_factor, tv_daozhang;
    private EditText et_input;
    private BankCard_Presenter presenter;
    private double fee;
    private int id;
    private double edu;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawcash);
        initView();
        initData();
        presenter.loadBankCard(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.loadBankCard(this);
    }

    @Override
    public void reqbackSuc(String tag) {
        ToastUtil.customAlert(this, "提现成功");
        CommonReq.reqUserMsg(getApplicationContext());
        Intent intent = new Intent(WithdrawCashActivity.this,
                WithdrawCashDetailsActivity.class);
        intent.putExtra("cash", et_input.getText().toString());
        intent.putExtra("fee", tv_factor.getText().toString());
        intent.putExtra("bankname", tv_bank.getText().toString());
        startActivity(intent);
        this.finish();
        return;
    }

    @Override
    public void reqbackFail(String msg, String tag) {
        ToastUtil.customAlert(this, msg);
    }

    @Override
    public void loadCardData(BankCard card) {
        User user = YiTouApplication.getInstance().getUser();
        if (card == null || user == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            edu=user.getAvailable_amount() - user.getFee();
            money.setText(keepDecimal2(user.getAvailable_amount()) + "");
            tv_edu.setText(keepDecimal2(edu) + "");
            tv_bank.setText(card.getName() + "(" + card.getSnumber() + ")");
            fee = user.getFee();
            id = card.getId();
            if (user.getAvailable_amount() <= 100) {
                et_input.setText(keepDecimal2(user.getAvailable_amount()) + "");
                et_input.setEnabled(false);
            }
        }

    }

    private String keepDecimal2(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    private String keepDecimal2(double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    @Override
    public void loadCardFailure(String msg) {
        ToastUtil.customAlert(this, msg);
        this.finish();
    }

    private void initData() {
        presenter = new BankCard_Presenter(this);
        mBack.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mNotice.setOnClickListener(this);
        mToggleBtn.setOnClickListener(this);
    }

    private void initView() {
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        mNextBtn = (TextView) findViewById(R.id.nextbtn);
        mNotice = (TextView) findViewById(R.id.notice);
        mToggleBtn = (ImageView) findViewById(R.id.img_toggle);
        money = (TextView) findViewById(R.id.money);
        tv_edu = (TextView) findViewById(R.id.tv_edu);
        tv_bank = (TextView) findViewById(R.id.tv_bank);
        tv_factor = (TextView) findViewById(R.id.tv_factor);
        et_input = (EditText) findViewById(R.id.et_input);
        tv_daozhang = (TextView) findViewById(R.id.tv_daozhang);
        ValifyUtil.setEnabled(mNextBtn, false);
        et_input.addTextChangedListener(new BasicWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                valify();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                String input = et_input.getText().toString();
                double num = Double.valueOf(StringUtils.isBlank(input) ? "0" : input);
                double kouchu = Double.valueOf(StringUtils.getFactor(num, edu));
                tv_factor.setText(keepDecimal2(kouchu) + "");
                tv_daozhang.setText(keepDecimal2(num - kouchu) + "");
            }
        });
    }
    private void valify(){
        String s=et_input.getText().toString();
        if ("".equals(s) ||s.startsWith("00"))
            et_input.setText("0");
        if (StringUtils.isBlank(s.toString()) || "0".equals(s)||isRead) {
            ValifyUtil.setEnabled(mNextBtn, false);
        } else {
            ValifyUtil.setEnabled(mNextBtn, true);
        }
    }
    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
            case R.id.nextbtn:
                payPswdMethod();
                break;
            case R.id.notice:
                startActivity(new Intent(this,WebViewActivity.class).putExtra("title","提现说明").putExtra("url","https://licai.gongshidai.com/wap/public/cylc/tixian.html"));
                break;
            case R.id.img_toggle:
                readProcotol();
                break;
        }
    }

    // 阅读协议
    private boolean isRead = false;

    private void readProcotol() {
        if (isRead) { // 是阅读的
            mToggleBtn.setImageResource(R.drawable.img_read);
            isRead = false;
        } else {  // 没有阅读
            mToggleBtn.setImageResource(R.drawable.img_no_read);
            isRead = true;
        }
        valify();
    }

    /***
     * 提现的PopupWindow
     */
    private PopupWindow mPayPswd;
    private View mPayView;

    private void payPswdMethod() {
        double cash = Double.valueOf(money.getText().toString());
        double tixian = Double.valueOf(et_input.getText().toString());
        if (cash > 100 && tixian < 100) {
            ToastUtil.customAlert(WithdrawCashActivity.this, "提现不能少于100");
            return;
        }
        mPayView = LayoutInflater.from(WithdrawCashActivity.this).inflate(R.layout.paypassword_popupwindow, null);
        TextView mSureBtn = (TextView) mPayView.findViewById(R.id.surebtm);
        TextView mConcelBtn = (TextView) mPayView.findViewById(R.id.concelbtn);
        ImageView concelImgs = (ImageView) mPayView.findViewById(R.id.imgs_concel);
        TextView tv_money = (TextView) mPayView.findViewById(R.id.tv_money);
        tv_money.setText(et_input.getText().toString() + "元");

        // TODO
        final LinearLayout mInputLayout = (LinearLayout) mPayView.findViewById(R.id.input_layout);
        final LPTextField mInputPswdEd = (LPTextField) mPayView.findViewById(R.id.inputpassward);


        mSureBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //TODO 先支付
                presenter.widthdrawCash(WithdrawCashActivity.this, id, Double.valueOf(et_input.getText().toString()), mInputPswdEd.getContentText());
                if (mPayPswd != null && mPayPswd.isShowing()) {
                    mPayPswd.dismiss();
                    mPayPswd = null;
                }
            }
        });

        concelImgs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mPayPswd != null && mPayPswd.isShowing()) {
                    mPayPswd.dismiss();
                    mPayPswd = null;
                }
            }
        });

        mConcelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mPayPswd != null && mPayPswd.isShowing()) {
                    mPayPswd.dismiss();
                    mPayPswd = null;
                }
            }
        });

        mPayPswd = new PopupWindow(mPayView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

        mPayPswd.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mPayPswd.setTouchable(true);
        mPayPswd.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mPayPswd.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mPayPswd.showAtLocation(mPayView, Gravity.BOTTOM, 0, 0);

        //TODO Bug
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mInputLayout.getWidth(), mInputLayout.getHeight());
        final int marginLeftRight = Settings.DISPLAY_WIDTH - mInputLayout.getWidth() / 2;
        final int margincenter = Settings.DISPLAY_HEIGHT - mInputLayout.getHeight() / 2;
//		params.setMargins(marginLeftRight,margincenter,marginLeftRight,0);
//		mInputLayout.setLayoutParams(params);
        mInputPswdEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {    //获取焦点 键盘弹出
//					params.setMargins(marginLeftRight, 100, marginLeftRight, 0);
//					mInputLayout.setLayoutParams(params);
                } else {  //没有获取焦点

//					params.setMargins(marginLeftRight, margincenter, marginLeftRight, 0);
//					mInputLayout.setLayoutParams(params);
                }
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

package com.laxiong.Activity;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allinpay.appayassistex.APPayAssistEx;
import com.carfriend.mistCF.R;
import com.laxiong.Adapter.RedPaper;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.Buy_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.LogUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.entity.User;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;
import com.squareup.okhttp.FormEncodingBuilder;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;

public class BuyingActivity extends BaseActivity implements OnClickListener, IViewCommonBack {
    /***
     * 立即购买页面
     */
    private static final int REQUEST_CODE = 1;
    private LinearLayout mChangeBankTpye, ll_redpaper;
    private FrameLayout mBack;
    private TextView mShowBankName, mMoneyLimit, mProjectName, mAmountMoney, mTrueAmount, mProfitAmout;
    private TextView mBuyBtn, tv_paper;
    private ImageView mToggleBtn;
    private boolean buytype = false;
    private String mProjectStr;
    private String mAmountStr;
    private int productId;
    private int limitDay;
    private double mBuyPrecent;  //总年化收益率
    private LinearLayout mMostMoney;

    private EditText mBuyAmount; //购买的份额
    private List<RedPaper> listpaper;
    private User user;
    private Buy_Presenter presenter;
    private int special ;
    private boolean isVip ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);
        presenter = new Buy_Presenter(this);
        mProjectStr = getIntent().getStringExtra("projectStr");
        mAmountStr = getIntent().getStringExtra("amountStr");
        productId = getIntent().getIntExtra("id", -1);
        mBuyPrecent = getIntent().getDoubleExtra("mBuyPrecent", -1);
        limitDay = getIntent().getIntExtra("limitday", -1);
        special = getIntent().getIntExtra("viplimitmoney",-1);
        isVip = getIntent().getBooleanExtra("isVip",false);


        initView();
        initData();
        readProcotol();
        getBankInfo();
    }

    @Override
    public void reqbackSuc(String tag) {

    }

    @Override
    public void reqbackFail(String msg, String tag) {
        disMisInputPay();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setValue();
    }

    private void setValue() {
        user = YiTouApplication.getInstance().getUser();
        if (user != null && mMoneyLimit != null) {
            mMoneyLimit.setText("" + user.getQuota());
        }
        HttpUtil2.get(InterfaceInfo.PRODUCT_URL + "?id=" + productId, new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if (response == null)
                    return;
                try {
                    mAmountStr = String.valueOf(response.getString("amount"));
                    mAmountMoney.setText(mAmountStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void initData() {
        mChangeBankTpye.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mBuyBtn.setOnClickListener(this);
        mToggleBtn.setOnClickListener(this);
        ll_redpaper.setOnClickListener(this);

        mProjectName.setText(mProjectStr);
        mAmountMoney.setText(mAmountStr);

        if (special!=-1){
            if (mBuyAmount!=null)
                mBuyAmount.setHint("最低可购买"+special/10000+"万元");
        }
    }

    private void initView() {
        ll_redpaper = (LinearLayout) findViewById(R.id.ll_redpaper);
        mChangeBankTpye = (LinearLayout) findViewById(R.id.changebank);
        mBack = (FrameLayout) findViewById(R.id.backlayout_);
        mBuyBtn = (TextView) findViewById(R.id.buyingbtn);
        mToggleBtn = (ImageView) findViewById(R.id.img_toggle);
        tv_paper = (TextView) findViewById(R.id.tv_paper);
        mTrueAmount = (TextView) findViewById(R.id.trueAmount);
        mProfitAmout = (TextView) findViewById(R.id.profitAmount);

        mShowBankName = (TextView) findViewById(R.id.showBankname);
        mMoneyLimit = (TextView) findViewById(R.id.one_limit);
        mAmountMoney = (TextView) findViewById(R.id.project_amount);
        mProjectName = (TextView) findViewById(R.id.project_name);
        mBuyAmount = (EditText) findViewById(R.id.buyamount);
        mMostMoney = (LinearLayout) findViewById(R.id.most_money);

        mBuyAmount.addTextChangedListener(watcher);
        setValue();
    }

    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.changebank:
                payMenthodType();
                break;
            case R.id.backlayout_:
                this.finish();
                break;
            case R.id.buyingbtn:
                if (Common.inputContentNotNull(mBuyAmount.getText().toString().trim())) {
                    if (minBugAmount()) {
                        selectPayMethod();
                    }else {
                        ToastUtil.customAlert(BuyingActivity.this,"最低可购买"+special/10000+"万元");
                    }
                } else {
                    ToastUtil.customAlert(BuyingActivity.this,"请输入购买金额");
                }

                break;
            case R.id.ll_redpaper:
                Intent intent = new Intent(BuyingActivity.this, WelCenterActivity.class);
                String money = mBuyAmount.getText().toString();
                if (StringUtils.isBlank(money)) {
                    ToastUtil.customAlert(this, "购买金额不能为空");
                } else {
                    intent.putExtra("money", Double.valueOf(money));
                    intent.putExtra("isBuying", true);
                    intent.putExtra("isAll", false);
                    intent.putExtra("limit", limitDay);
                    BuyingActivity.this.startActivityForResult(intent, REQUEST_CODE);
                }
                break;

            case R.id.img_toggle:
                readProcotol();
                break;

        }
    }

    private int total = 0;
    private String redBaoId = "";
    private int decAmount; // 最终提交的钱

    public void showAppayRes(String res) {
        new AlertDialog.Builder(this)
                .setMessage(res)
                .setPositiveButton("确定", null)
                .show();
    }

    //处理红包选择回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        total = 0;
        String redbao = "";
        if (data != null) {
            if (buytype) {
                if (APPayAssistEx.REQUESTCODE == requestCode) {
                    if (null != data) {
                        String payRes = null;
                        String payAmount = null;
                        String payTime = null;
                        String payOrderId = null;
                        try {
                            JSONObject resultJson = new JSONObject(data.getExtras().getString("result"));
                            payRes = resultJson.getString(APPayAssistEx.KEY_PAY_RES);
                            payAmount = resultJson.getString("payAmount");
                            payTime = resultJson.getString("payTime");
                            payOrderId = resultJson.getString("payOrderId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS)) {
                            showAppayRes("支付成功！");
                            startActivity(new Intent(BuyingActivity.this,
                                    BuyingResultActivity.class).
                                    putExtra("Money", String.valueOf(decAmount)).
                                    putExtra("ProductName", mProjectStr));  // 跳转到购买结果的页面
                            finish();
                        } else {
                            showAppayRes("支付失败！");
                        }
                        disMisInputPay();
                        LogUtils.d("payResult", "payRes: " + payRes + "  payAmount: " + payAmount + "  payTime: " + payTime + "  payOrderId: " + payOrderId);
                    }
                }
                super.onActivityResult(requestCode, resultCode, data);
                buytype = false;
                return;
            }
            if (resultCode == RESULT_OK) {
                listpaper = data.getParcelableArrayListExtra("data");
                if (listpaper != null && listpaper.size() > 0) {
                    for (RedPaper paper : listpaper) {
                        total += paper.getAmount();
                        redbao = paper.getId() + "," + redbao;
                    }
                    redBaoId = redbao.substring(0, redbao.length() - 1);
                    tv_paper.setText(total + "	元");
                    decAmount = Integer.valueOf(mBuyAmount.getText().toString().trim()) - total;
                    mTrueAmount.setText("" + decAmount);
                }
            }
            buytype = false;
        }
    }

    // 阅读协议
    private boolean isRead = true;

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

    // 余额不足的情况
    private boolean isEnough() {
        if (user != null) {
            if (user.getAvailable_amount() < Integer.valueOf(mBuyAmount.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    // 选择不同的支付方式，银行卡支付和余额支付的
    private String mYuMoneyStr = "";

    private void selectPayMethod() {
        if (mShowBankName != null) {
            if (selectPay.equals(mYuMoneyStr)) {  // 余额的
                if (isEnough()) {
                    Toast.makeText(BuyingActivity.this, "余额不足", Toast.LENGTH_LONG).show();
                } else {
                    inputOverToPswd();
                }
            } else {
                inputOverToPswd();
            }
        }
    }

    //显示支付方式和PopWindow里选中的支付方式的一致型
    private int choiceImg = 1;

    private void choicePayImg(int index) {
        initNoSelect();
        switch (index) {
            case 1:            //招商卡
                constranceBank_img.setImageResource(R.drawable.img_read);
                break;
            case 2:            //余额
                lateMoney_img.setImageResource(R.drawable.img_read);
                break;
        }
    }

    // pay Menthod
    private PopupWindow mPayMathodWindow;
    private View PayView;
    private ImageView lateMoney_img, constranceBank_img;
    private ImageView BankIcon, LateMoneyIcon;
    private TextView mBankName, mMyYuE;

    private void payMenthodType() {

        PayView = LayoutInflater.from(BuyingActivity.this).inflate(R.layout.pay_mathod_popwindow, null);

        RelativeLayout lateMoney = (RelativeLayout) PayView.findViewById(R.id.latemoney);
        RelativeLayout constranceBank = (RelativeLayout) PayView.findViewById(R.id.concreatebank);
        TextView mConcel = (TextView) PayView.findViewById(R.id.concel);

        lateMoney_img = (ImageView) PayView.findViewById(R.id.change_img_latemoney);
        constranceBank_img = (ImageView) PayView.findViewById(R.id.change_img_concreatebank);

        lateMoney.setOnClickListener(listenner);
        constranceBank.setOnClickListener(listenner);
        mConcel.setOnClickListener(listenner);

        //银行卡等信息
        mBankName = (TextView) PayView.findViewById(R.id.bankname);
        mMyYuE = (TextView) PayView.findViewById(R.id.myyue);
        //Item卡的图片
        BankIcon = (ImageView) PayView.findViewById(R.id.icon1);
        LateMoneyIcon = (ImageView) PayView.findViewById(R.id.icon2);

        if (bankname != null) {
            mBankName.setText(bankname + "(尾号" + bankLastNum + ")");
            choicePayImg(choiceImg);
        }
        if (logokey != null)
            BankIcon.setImageResource(getResources().getIdentifier("logo_" + logokey, "drawable", getPackageName()));

        if (user != null) {
            mMyYuE.setText("从余额(" + user.getAvailable_amount() + ")元");
            mYuMoneyStr = "余额(" + user.getAvailable_amount() + ")元";
        }

        mPayMathodWindow = new PopupWindow(PayView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        mPayMathodWindow.setTouchable(true);
        mPayMathodWindow.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mPayMathodWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mPayMathodWindow.showAtLocation(PayView, Gravity.BOTTOM, 0, 0);
    }

    // 取消选择支付方式的
    private void dissPayMethod() {
        if (mPayMathodWindow != null && mPayMathodWindow.isShowing()) {
            mPayMathodWindow.dismiss();
            mPayMathodWindow = null;
        }
    }

    OnClickListener listenner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            initNoSelect();
            switch (v.getId()) {
                case R.id.latemoney:  //  余额
                    choicePayImg(2);
                    choiceImg = 2;
                    if (user != null) {
                        mShowBankName.setText("从余额(" + user.getAvailable_amount() + ")元");
                        selectPay = "余额(" + user.getAvailable_amount() + ")元";
                        mMostMoney.setVisibility(View.INVISIBLE);
                    }
                    dissPayMethod();

                    break;
                case R.id.concreatebank:  // 建设银行
                    choicePayImg(1);
                    choiceImg = 1;
                    mShowBankName.setText(bankname + "(尾号" + bankLastNum + ")");
                    selectPay = bankname + "(尾号" + bankLastNum + ")";
                    mMostMoney.setVisibility(View.VISIBLE);
                    dissPayMethod();

                    break;
                case R.id.concel:
                    dissPayMethod();

                    break;

                case R.id.imgs_concel: // 支付取消Img
                    disMisInputPay();

                    break;

                case R.id.concel_btn:// 支付取消Btn
                    disMisInputPay();

                    break;

                case R.id.sure_btn:    // 支付确定按钮
                    if (selectPay.equals(mYuMoneyStr)) {  // 余额的
                        payBuyProduct();
                    } else {
                        payBuyLLProduct();
                    }

                    break;

                case R.id.forget_pswd:    // 支付忘记密码
                    startActivity(new Intent(BuyingActivity.this,
                            FoundPswdActivity.class));
                    break;
            }
        }
    };

    private void initNoSelect() {
        if (lateMoney_img != null && constranceBank_img != null) {
            lateMoney_img.setImageResource(R.drawable.img_no_read);
            constranceBank_img.setImageResource(R.drawable.img_no_read);
        }
    }

    private String logokey;
    private String bankname;
    private int bankLastNum;
    private String selectPay; // 支付密码处的显示支付方式
    // 转入按钮  输入密码
    private PopupWindow mInputPswdWindow;
    private View mInputView;
    private EditText mInputPswdEd;
    private TextView mNoticeTopayMoney, mTranInMoney;
    private LinearLayout mLlPswdBg;

    private void inputOverToPswd() {

        mInputView = LayoutInflater.from(BuyingActivity.this).inflate(R.layout.overtopswd_popwindow, null);

        ImageView comcelImags = (ImageView) mInputView.findViewById(R.id.imgs_concel);
        TextView mForgetPswd = (TextView) mInputView.findViewById(R.id.forget_pswd);
        TextView concelBtn = (TextView) mInputView.findViewById(R.id.concel_btn);
        TextView sureBtn = (TextView) mInputView.findViewById(R.id.sure_btn);
        mInputPswdEd = (EditText) mInputView.findViewById(R.id.inputpswd);
        mNoticeTopayMoney = (TextView) mInputView.findViewById(R.id.topaymoney);
        mTranInMoney = (TextView) mInputView.findViewById(R.id.zhuang_money);
        mLlPswdBg = (LinearLayout) mInputView.findViewById(R.id.ll_pswd_bg);

        mNoticeTopayMoney.setText("从" + selectPay + "-转入-" + mProjectStr);
//		int payMoney = Integer.valueOf(mBuyAmount.getText().toString().trim());
        mTranInMoney.setText(decAmount + "元");


        comcelImags.setOnClickListener(listenner);
        concelBtn.setOnClickListener(listenner);
        sureBtn.setOnClickListener(listenner);
        mForgetPswd.setOnClickListener(listenner);

        mInputPswdWindow = new PopupWindow(mInputView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        mInputPswdWindow.setTouchable(true);
        mInputPswdWindow.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mInputPswdWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mInputPswdWindow.showAtLocation(mInputView, Gravity.BOTTOM, 0, 0);
    }

    //消去支付密码PopWindow
    private void disMisInputPay() {
        if (mInputPswdWindow != null && mInputPswdWindow.isShowing()) {
            mInputPswdWindow.dismiss();
            mInputPswdWindow = null;
        }
    }

    //获取银行卡信息
    private void getBankInfo() {
        HttpUtil2.get(InterfaceInfo.BASE_URL + "/bank", new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            bankname = response.getString("name");
                            mShowBankName.setText(bankname + "(尾号" + response.getInt("snumber") + ")");
                            selectPay = bankname + "(尾号" + response.getInt("snumber") + ")";
//							mMoneyLimit.setText(response.getString("one_limit"));  // 银行卡限额
                            logokey = response.getString("logoKey");
                            bankLastNum = response.getInt("snumber");
                            bankId = response.getInt("id");
                            banknumber = response.getInt("number");
                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(BuyingActivity.this);
                                return;
                            }
                            ToastUtil.customAlert(BuyingActivity.this,response.getString("msg"));
                        }
                    } catch (Exception E) {
                    }
                }
            }
            @Override
            public void onFailure(String msg) {
                ToastUtil.customAlert(BuyingActivity.this, "获取数据失败");
            }
        },Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
                .getUserLogin().getToken()));
    }

    //购买产品  余额购买
    private void payBuyProduct() {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        int money = Integer.valueOf(mBuyAmount.getText().toString().trim());
        builder.add("amount", money+"");
        builder.add("product", productId+"");
        builder.add("pay_pwd", mInputPswdEd.getText().toString().trim());
        //我的红包
        builder.add("pamount", total+"");
        builder.add("pids", redBaoId);
        HttpUtil2.post(InterfaceInfo.BASE_URL + "/appBuy", builder, new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            sendBroadcast(new Intent("com.action.updatedata").putExtra("id", productId));
                            startActivity(new Intent(BuyingActivity.this,
                                    BuyingResultActivity.class).
                                    putExtra("Money", String.valueOf(decAmount)).
                                    putExtra("ProductName", mProjectStr));
                            disMisInputPay();
                            finish();
                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(BuyingActivity.this);
                                return;
                            }
                            //交易密码是否正确的判断
                            if (mInputPswdEd != null && mLlPswdBg != null) {
                                mLlPswdBg.setBackgroundResource(R.drawable.red_border);
                                mInputPswdEd.setText("");
                                mInputPswdEd.setHintTextColor(Color.parseColor("#EE4E42"));
                                mInputPswdEd.setHint(response.getString("msg"));
                            }
                        }
                    } catch (Exception E) {
                    }
                }
            }
            @Override
            public void onFailure(String msg) {
                ToastUtil.customAlert(BuyingActivity.this,"获取数据失败");
            }
        },Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
                YiTouApplication.getInstance().getUserLogin().getToken()));
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String amountMoney = mBuyAmount.getText().toString().trim();
            if (!amountMoney.equals(""))
                decAmount = Integer.valueOf(amountMoney) - total;
            valify();
            //最多可输入可购买的份额以内的数值
            if (!amountMoney.equals("") && amountMoney.length() != 0) {
                if (Integer.valueOf(amountMoney) > Integer.valueOf(mAmountStr)) {
                    mBuyAmount.setText(mAmountStr);
                    mBuyAmount.setSelection(mAmountMoney.length()); // 设置光标的位置
                }
            }
        }
    };

    //设置计算后的数据
    private void setMathData(int amount) {
        if (mBuyPrecent != -1 && limitDay != -1) {
            NumberFormat mFormat = NumberFormat.getNumberInstance();
            mFormat.setMaximumFractionDigits(3);
            double profit_money = backComfix(amount, limitDay, mBuyPrecent);
            String result = mFormat.format(profit_money);
            mProfitAmout.setText("" + result);
        } else {
            mProfitAmout.setText("0.0");
        }

        amount = amount - total;
        mTrueAmount.setText("" + amount);

    }

    /**
     * money:本金
     * day：日期
     * lu：利率
     */
    private double backComfix(float money, double day, double lu) {
        double backMoney = money * (lu / 100) * (day / 365) + money;
        return backMoney;
    }

    private int bankId;
    private int banknumber;

    //购买的卡支付
    private void payBuyLLProduct() {
        buytype = true;
        int money = Integer.valueOf(mBuyAmount.getText().toString().trim());
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("amount", money + "");
        builder.add("bank_id", bankId + "");
        builder.add("product", productId + "");
        builder.add("pay_pwd", mInputPswdEd.getText().toString().trim());
        builder.add("number", banknumber + "");
        builder.add("recharge", decAmount + "");
        builder.add("pamount", total + "");
        builder.add("pids", redBaoId);
        builder.add("vip",isVip+"");
        presenter.buyByCard(this, builder);
    }

    private void valify() {
        if (!TextUtils.isEmpty(mBuyAmount.getText().toString()) && !isRead) {
            setMathData(Integer.valueOf(mBuyAmount.getText().toString().trim()));
            mBuyBtn.setClickable(true);
            mBuyBtn.setBackgroundResource(R.drawable.button_red_corner_border);
        } else {
            mBuyBtn.setClickable(false);
            mBuyBtn.setBackgroundResource(R.drawable.button_grey_corner_border);
        }
    }

    // 最低可购买的份额
    private boolean minBugAmount(){
        if (isVip) {
            String amountMoney = mBuyAmount.getText().toString().trim();
            if (!amountMoney.equals("") && amountMoney.length() != 0) {
                if (Integer.valueOf(amountMoney) >= special) {
                    return true;
                }
                return false;
            }
        }
        return true ;
    }

}

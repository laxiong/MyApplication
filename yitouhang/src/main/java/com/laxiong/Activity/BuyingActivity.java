package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import com.gongshidai.mistGSD.R;
import com.google.gson.GsonBuilder;
import com.laxiong.Adapter.RedPaper;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.BaseHelper;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.Constants;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.Md5Algorithm;
import com.laxiong.Utils.MobileSecurePayer;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.entity.EnvConstants;
import com.laxiong.entity.LlOrderInfo;
import com.laxiong.entity.PayOrder;
import com.laxiong.entity.PaySignParam;
import com.laxiong.entity.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;

public class BuyingActivity extends BaseActivity implements OnClickListener {
    /***
     * 立即购买页面
     */
    private static final int REQUEST_CODE = 1;
    private LinearLayout mChangeBankTpye, ll_redpaper;
    private FrameLayout mBack;
    private TextView mShowBankName, mMoneyLimit, mProjectName, mAmountMoney, mTrueAmount, mProfitAmout;
    private TextView mBuyBtn, tv_paper;
    private ImageView mToggleBtn;

    private String mProjectStr;
    private String mAmountStr;
    private int productId;
    private int limitDay;
    private double mBuyPrecent;  //总年化收益率
    private LinearLayout mMostMoney;

    private EditText mBuyAmount; //购买的份额
    private List<RedPaper> listpaper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);

        mProjectStr = getIntent().getStringExtra("projectStr");
        mAmountStr = getIntent().getStringExtra("amountStr");
        productId = getIntent().getIntExtra("id", -1);
        mBuyPrecent = getIntent().getDoubleExtra("mBuyPrecent", -1);
        limitDay = getIntent().getIntExtra("limitday", -1);

        initView();
        initData();
        readProcotol();
        getBankInfo();
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
                if(response==null)
                    return;
                try {
                    mAmountStr=String.valueOf(response.getString("amount"));
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
                    selectPayMethod();
                } else {
                    Toast.makeText(BuyingActivity.this, "请输入购买金额", Toast.LENGTH_LONG).show();
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

    //处理红包选择回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        total=0;
        String redbao = "";
        if (data != null && resultCode == RESULT_OK) {
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
        HttpUtil.get(InterfaceInfo.BASE_URL + "/bank", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

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
                            Toast.makeText(BuyingActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception E) {
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(BuyingActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        }, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
                .getUserLogin().getToken()));
    }

    //购买产品  余额购买
    private void payBuyProduct() {
        RequestParams params = new RequestParams();

        int money = Integer.valueOf(mBuyAmount.getText().toString().trim());
        params.put("amount", money);
        params.put("product", productId);
        params.put("pay_pwd", mInputPswdEd.getText().toString().trim());
        // 抵扣的红包
        params.put("pamount", total);
        params.put("pids", redBaoId);
//		Log.i("WK", "余额购买的红包数据：" + "pamount红包的金额= "+total+" ; "+"pids红包的id= "+redBaoId);
        HttpUtil.post(InterfaceInfo.BASE_URL + "/appBuy", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            sendBroadcast(new Intent("com.action.updatedata").putExtra("id",productId));
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(BuyingActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        }, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
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

    //设置计算后的数据	mTrueAmount ,mProfitAmout  实际支付  预期收益
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

        RequestParams params = new RequestParams();

        int money = Integer.valueOf(mBuyAmount.getText().toString().trim());
        params.put("amount", money);
        params.put("bank_id", bankId);
        params.put("product", productId);
        params.put("pay_pwd", mInputPswdEd.getText().toString().trim());
        params.put("number", banknumber);
        params.put("recharge", decAmount);
        // 抵扣的红包
        params.put("pamount", total);
        params.put("pids", redBaoId);

        HttpUtil.post(InterfaceInfo.BASE_URL + "/appBuy", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {

                            LlOrderInfo mInfo = new GsonBuilder().create().fromJson(response.toString(),
                                    LlOrderInfo.class);
                            PayOrder mPayOrder = constructPreCardPayOrder(mInfo);
                            String content4Pay = BaseHelper.toJSONString(mPayOrder);
                            // 关键 content4Pay
                            // 用于提交到支付SDK的订单支付串，如遇到签名错误的情况，请将该信息帖给我们的技术支持
                            MobileSecurePayer msp = new MobileSecurePayer();
                            boolean bRet = msp.pay(content4Pay, mHandler, Constants.RQF_PAY,
                                    BuyingActivity.this, false);
                            if (bRet)
                                disMisInputPay();

                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(BuyingActivity.this);
                                return;
                            }
                            if (!TextUtils.isEmpty(response.getString("msg"))) {
                                Toast.makeText(BuyingActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BuyingActivity.this, "获取订单信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception E) {
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(BuyingActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        }, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
                YiTouApplication.getInstance().getUserLogin().getToken()));

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

    //构造订单类
    private PayOrder constructPreCardPayOrder(LlOrderInfo mInfo) {

        PayOrder order = new PayOrder();
        PaySignParam signParam = new PaySignParam();
        signParam.setBusi_partner(mInfo.getBusi_partner());
        signParam.setNo_order(mInfo.getNo_order());
        signParam.setDt_order(mInfo.getDt_order());
        signParam.setName_goods(mInfo.getName_goods());
        signParam.setNotify_url(mInfo.getNotify_url());
        signParam.setSign_type(PayOrder.SIGN_TYPE_MD5);
        signParam.setValid_order(mInfo.getValid_order());
        signParam.setInfo_order(mInfo.getInfo_order());
        signParam.setMoney_order(mInfo.getMoney_order());
        // 银行卡历次支付时填写，可以查询得到，协议号匹配会进入SDK，
        // order.setNo_agree();
        // 风险控制参数
        signParam.setRisk_item(mInfo.getRisk_item());
        signParam.setOid_partner(EnvConstants.PARTNER);
        String sign = "";
        String content = BaseHelper.sortParam(signParam);
        // MD5 签名方式
        sign = Md5Algorithm.getInstance().sign(content, EnvConstants.MD5_KEY);

        order.setSign(sign);

        order.setBusi_partner(mInfo.getBusi_partner());
        order.setNo_order(mInfo.getNo_order());
        Log.i("WK", "连连支付的订单号：" + mInfo.getNo_order());
        order.setDt_order(mInfo.getDt_order());
        order.setName_goods(mInfo.getName_goods());
        order.setNotify_url(mInfo.getNotify_url());
        order.setSign_type(PayOrder.SIGN_TYPE_MD5);
        order.setValid_order(mInfo.getValid_order());

        order.setUser_id(mInfo.getUser_id());
        order.setId_no(mInfo.getId_no());
        order.setInfo_order(mInfo.getInfo_order());

        order.setAcct_name(mInfo.getAcct_name());
        order.setMoney_order(mInfo.getMoney_order());
        order.setNo_goods(mInfo.getNo_goods());

        // 银行卡卡号，该卡首次支付时必填
        order.setCard_no(mInfo.getCard_no());
        // 银行卡历次支付时填写，可以查询得到，协议号匹配会进入SDK，
        // order.setNo_agree();

        // int id = ((RadioGroup)
        // findViewById(R.id.flag_modify_group)).getCheckedRadioButtonId();
        // if (id == R.id.flag_modify_0) {
        // order.setFlag_modify("0");
        // } else if (id == R.id.flag_modify_1) {
        // order.setFlag_modify("1");
        // }
        // 风险控制参数
        order.setRisk_item(mInfo.getRisk_item());
        order.setOid_partner(EnvConstants.PARTNER);

        return order;
    }

    private Handler mHandler = createHandler();

    @SuppressLint("HandlerLeak")
    private Handler createHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {
                String strRet = (String) msg.obj;
                switch (msg.what) {
                    case Constants.RQF_PAY: {
                        JSONObject objContent = BaseHelper.string2JSON(strRet);
                        String retCode = objContent.optString("ret_code");
                        String retMsg = objContent.optString("ret_msg");
                        // 成功
                        if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
                            // TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
//							BaseHelper.showDialog(BuyingActivity.this, "提示", "支付成功",
//									android.R.drawable.ic_dialog_alert);
                            startActivity(new Intent(BuyingActivity.this,
                                    BuyingResultActivity.class).
                                    putExtra("Money", String.valueOf(decAmount)).
                                    putExtra("ProductName", mProjectStr));  // 跳转到购买结果的页面
                            finish();
//							NofifyUserinfoChanged nofifyUserinfoChanged = new NofifyUserinfoChanged() {
//								@Override
//								public void onNotifyUserinfoChange() {
//									CommonUtils.setmNofifyUserinfoChanged(null);
//
//								}
//							};
//							CommonUtils.setmNofifyUserinfoChanged(nofifyUserinfoChanged);
//							CommonUtils.getUserInfo(YiTouApplication.getInstance().getUserLogin().getToken_id(),
//									YiTouApplication.getInstance().getUserLogin().getToken(),BuyingActivity.this);

                        } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                            // 处理中，掉单的情形
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay)) {
                                BaseHelper.showDialog(BuyingActivity.this, "提示",
                                        objContent.optString("ret_msg"),
                                        android.R.drawable.ic_dialog_alert);
                            }

                        } else {
                            // 失败
                            BaseHelper.showDialog(BuyingActivity.this, "提示", retMsg,
                                    android.R.drawable.ic_dialog_alert);
                        }
                    }
                    break;
                }
                super.handleMessage(msg);
            }
        };
    }

}

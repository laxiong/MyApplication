package com.laxiong.Activity;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.gongshidai.mistGSD.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.Buy_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.LogUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.entity.User;
import com.squareup.okhttp.FormEncodingBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferInActivity extends BaseActivity implements OnClickListener,IViewCommonBack{
	/****
	 * 转入的
	 */
	private FrameLayout mBack ;
	private TextView mBankCan ; // 银行限制
	private LinearLayout mPayMethod ;
	private TextView mTransferinBtn ,mAmountLimit ,mTransferInProduct,mShowBankName,mProjectAmount , mDate;
	private ImageView mToggleBtn ;
	private EditText mBuyAmount ;
	private int productId ;
	private User user ;
	private Buy_Presenter presenter;
	// 可购买金额
	private String mAmountMoney ;
	private String dates ;
	private LinearLayout mMostMoney ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transferin);
		initView();
		initData();
		getBankInfo();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		setValue();
	}

	@Override
	public void reqbackSuc(String tag) {

	}

	@Override
	public void reqbackFail(String msg, String tag) {

	}

	private void initData() {
		presenter=new Buy_Presenter(this);
		mBack.setOnClickListener(this);
		mBankCan.setOnClickListener(this);
		mPayMethod.setOnClickListener(this);
		mTransferinBtn.setOnClickListener(this);
		mToggleBtn.setOnClickListener(this);
		setValue();
	}
	private void setValue(){
		user = YiTouApplication.getInstance().getUser();
		if (user!=null&&mAmountLimit!=null){
			int lateAmount = user.getQuota()-user.getCurrent();
			mAmountMoney = lateAmount+"";
			mProjectAmount.setText(""+lateAmount); // 用户可购买金额，普通用户20000元，vip用户500000元
		}
	}
	private void initView() {
		mBack = (FrameLayout)findViewById(R.id.in_backlayout);
		mBankCan = (TextView)findViewById(R.id.bankcan);
		mPayMethod = (LinearLayout)findViewById(R.id.selectpaymethod);
		mTransferinBtn = (TextView)findViewById(R.id.transferinnow);
		mToggleBtn = (ImageView)findViewById(R.id.img_toggle);
		mProjectAmount = (TextView)findViewById(R.id.project_amount);
		mDate = (TextView)findViewById(R.id.date);

		mAmountLimit =(TextView)findViewById(R.id.transferin_limit);
		mTransferInProduct=(TextView)findViewById(R.id.transferin_product);
		mShowBankName =(TextView)findViewById(R.id.showbankname);
		mBuyAmount =(EditText)findViewById(R.id.input_money);
		mBuyAmount.addTextChangedListener(watcher);

		mMostMoney = (LinearLayout)findViewById(R.id.mMost_limit);


//		mAmountMoney = getIntent().getStringExtra("mAmountMoney");
		dates = getIntent().getStringExtra("date");
		mDate.setText(dates);

		SharedPreferences sxtId = getSharedPreferences("SXT_ID", Context.MODE_PRIVATE);
		if (sxtId!=null){
			productId = sxtId.getInt("sxt_id",0);
		}else {
			productId = 0 ;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.in_backlayout:
				this.finish();
				break;
			case R.id.selectpaymethod:
				payMenthodType();
				break;
			case R.id.transferinnow:
				if (Common.inputContentNotNull(mBuyAmount.getText().toString().trim())){
					selectPayMethod();
				}else {
					Toast.makeText(TransferInActivity.this, "请输入购买金额",Toast.LENGTH_LONG).show();
				}
				break;

			case R.id.bankcan:
				Toast.makeText(this, "银行限额", Toast.LENGTH_SHORT).show();
				break;
			case R.id.img_toggle:
				readProcotol();
				break;
		}
	}

	// 阅读协议
	private boolean isRead = false ;
	private void readProcotol(){
		if(isRead){ // 是阅读的
			mToggleBtn.setImageResource(R.drawable.img_read);
			isRead = false ;
		}else{  // 没有阅读
			mToggleBtn.setImageResource(R.drawable.img_no_read);
			isRead = true ;
		}
		valify();
	}

	// 余额不足的情况
	private boolean isEnough(){
		if (user!=null){
			if (user.getAvailable_amount() < Integer.valueOf(mBuyAmount.getText().toString().trim())) {
				return true ;
			}
		}
		return false ;
	}

	// 选择不同的支付方式，银行卡支付和余额支付的
	private String mYuMoneyStr = "";
	private void selectPayMethod(){
		if (mShowBankName!=null){
			if (selectPay.equals(mYuMoneyStr)){  // 余额的
				if (isEnough()) {
					Toast.makeText(TransferInActivity.this, "余额不足", Toast.LENGTH_LONG).show();
				}else {
					inputOverToPswd();
				}
			}else {
				inputOverToPswd();
			}
		}
	}

	// pay Menthod
	private PopupWindow mPayMathodWindow ;
	private View PayView ;
	private ImageView lateMoney_img,constranceBank_img ;
	private ImageView BankIcon,LateMoneyIcon ;
	private TextView mBankName,mMyYuE;
	private void payMenthodType(){

		PayView = LayoutInflater.from(TransferInActivity.this).inflate(R.layout.pay_mathod_popwindow, null);

		RelativeLayout lateMoney = (RelativeLayout)PayView.findViewById(R.id.latemoney);
		RelativeLayout constranceBank = (RelativeLayout)PayView.findViewById(R.id.concreatebank);
		TextView mConcel = (TextView)PayView.findViewById(R.id.concel);

		lateMoney_img = (ImageView)PayView.findViewById(R.id.change_img_latemoney);
		constranceBank_img = (ImageView)PayView.findViewById(R.id.change_img_concreatebank);

		lateMoney.setOnClickListener(listenner);
		constranceBank.setOnClickListener(listenner);
		mConcel.setOnClickListener(listenner);

		//银行卡等信息
		mBankName =(TextView)PayView.findViewById(R.id.bankname);
		mMyYuE =(TextView)PayView.findViewById(R.id.myyue);
		//Item卡的图片
		BankIcon =(ImageView)PayView.findViewById(R.id.icon1);
		LateMoneyIcon =(ImageView)PayView.findViewById(R.id.icon2);

		if (bankname!=null) {
			mBankName.setText(bankname+"(尾号"+bankLastNum+")");
			choicePayImg(choiceImg);
		}
		if (logokey!=null)
			BankIcon.setImageResource(getResources().getIdentifier("logo_" + logokey, "drawable", getPackageName()));

		if (user!=null){
			mMyYuE.setText("从余额("+user.getAvailable_amount() + ")元");
			mYuMoneyStr = "余额("+user.getAvailable_amount() + ")元"; // 余额的String
		}

		mPayMathodWindow = new PopupWindow(PayView,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
		mPayMathodWindow.setTouchable(true);
		mPayMathodWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mPayMathodWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mPayMathodWindow.showAtLocation(PayView, Gravity.BOTTOM, 0, 0);
	}

	//显示支付方式和PopWindow里选中的支付方式的一致型
	private int choiceImg = 1;
	private void choicePayImg(int index){
		initNoSelect();
		switch (index){
			case 1:			//招商卡
				constranceBank_img.setImageResource(R.drawable.img_read);
				break;
			case 2:			//余额
				lateMoney_img.setImageResource(R.drawable.img_read);
				break;
		}
	}

	// 取消选择支付方式的
	private void dissPayMethod(){
		if(mPayMathodWindow!=null&&mPayMathodWindow.isShowing()){
			mPayMathodWindow.dismiss();
			mPayMathodWindow = null ;
		}
	}

	OnClickListener listenner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			initNoSelect();
			switch(v.getId()){
				case R.id.latemoney:  //  余额
					choicePayImg(2);
					choiceImg = 2 ;
					if (user!=null) {
						mShowBankName.setText("从余额(" + user.getAvailable_amount() + ")元");
						selectPay = "余额(" + user.getAvailable_amount() + ")元";
						mMostMoney.setVisibility(View.INVISIBLE);
					}
					dissPayMethod();

					break;

				case R.id.concreatebank:  // 建设银行
					choicePayImg(1);
					choiceImg = 1 ;
					mShowBankName.setText(bankname + "(尾号" + bankLastNum + ")");
					selectPay = bankname + "(尾号" + bankLastNum + ")" ;
					mMostMoney.setVisibility(View.VISIBLE);
					dissPayMethod();

					break;

				case R.id.concel:
					dissPayMethod();

					break;

				case R.id.concel_btn:  // 输入的Text取消
					disMisInputPay();

					break;

				case R.id.imgs_concel:  // 输入的Img取消
					disMisInputPay();

					break;

				case R.id.sure_btn:	// 输入的Text确定

					if (selectPay.equals(mYuMoneyStr)) {  // 余额的
						payBuyProduct();
					}else {					// 银行卡的
						payBuyLLProduct();
					}

					break;

				case R.id.forget_pswd: //输入的忘记密码
					startActivity(new Intent(TransferInActivity.this,
							FoundPswdActivity.class));

					break;
			}
		}
	};

	private void initNoSelect(){
		if(constranceBank_img!=null&&lateMoney_img!=null) {
			lateMoney_img.setImageResource(R.drawable.img_no_read);
			constranceBank_img.setImageResource(R.drawable.img_no_read);
		}
	}

	private String logokey ;
	private int bankLastNum ;
	private String bankname ;
	private String selectPay ; // 支付密码处的显示支付方式
	// 转入按钮  输入密码
	private PopupWindow mInputPswdWindow ;
	private View mInputView ;
	private EditText mInputPswdEd ;
	private TextView mNoticeTopayMoney ,mTranInMoney;
	private void inputOverToPswd(){

		mInputView = LayoutInflater.from(TransferInActivity.this).inflate(R.layout.overtopswd_popwindow, null);

		ImageView comcelImags = (ImageView)mInputView.findViewById(R.id.imgs_concel);
		TextView  mForgetPswd = (TextView)mInputView.findViewById(R.id.forget_pswd);
		TextView concelBtn = (TextView)mInputView.findViewById(R.id.concel_btn);
		TextView sureBtn = (TextView)mInputView.findViewById(R.id.sure_btn);
		mInputPswdEd =(EditText)mInputView.findViewById(R.id.inputpswd);
		mNoticeTopayMoney =(TextView)mInputView.findViewById(R.id.topaymoney);
		mTranInMoney =(TextView)mInputView.findViewById(R.id.zhuang_money);

		mNoticeTopayMoney.setText("从"+selectPay+"-转入-"+"时息通");
		mTranInMoney.setText(mBuyAmount.getText().toString().trim()+"元");

		comcelImags.setOnClickListener(listenner);
		concelBtn.setOnClickListener(listenner);
		sureBtn.setOnClickListener(listenner);
		mForgetPswd.setOnClickListener(listenner);


		mInputPswdWindow = new PopupWindow(mInputView,  LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
		mInputPswdWindow.setTouchable(true);
		mInputPswdWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mInputPswdWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mInputPswdWindow.showAtLocation(mInputView, Gravity.BOTTOM, 0, 0);
	}

	//消去支付密码PopWindow
	private void disMisInputPay(){
		if(mInputPswdWindow!=null&&mInputPswdWindow.isShowing()){
			mInputPswdWindow.dismiss();
			mInputPswdWindow = null ;
		}
	}

	// 获取银行卡的信息
	private void getBankInfo(){
		HttpUtil2.get(InterfaceInfo.BASE_URL + "/bank", new Callback() {
			@Override
			public void onResponse2(JSONObject response) {
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							bankname = response.getString("name");
							mShowBankName.setText(bankname+"(尾号"+response.getInt("snumber")+")");
							selectPay = bankname+"(尾号"+response.getInt("snumber")+")";
							mAmountLimit.setText(response.getString("one_limit")); 					 // 这是银行卡限额
							logokey = response.getString("logoKey");
							bankLastNum = response.getInt("snumber");
							bankId = response.getInt("id");
							banknumber = response.getInt("number");
						} else {
							if (response.getInt("code") == 401) {
								CommonReq.showReLoginDialog(TransferInActivity.this);
								return;
							}
							Toast.makeText(TransferInActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
						}
					} catch (Exception E) {
					}
				}
			}
			@Override
			public void onFailure(String msg) {
				ToastUtil.customAlert(TransferInActivity.this,"获取数据失败");
			}
		},Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
				.getUserLogin().getToken()));
	}


	public void showAppayRes(String res) {
		new AlertDialog.Builder(this)
				.setMessage(res)
				.setPositiveButton("确定", null)
				.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
					showAppayRes("转入成功！");
					CommonReq.reqUserMsg(getApplicationContext());
					startActivity(new Intent(TransferInActivity.this,
									TransferInResultActivity.class).
									putExtra("Money", mBuyAmount.getText().toString().trim())
					);  // 跳转到购买结果的页面
					finish();
				} else {
					showAppayRes("转入失败！");
				}
				disMisInputPay();
				LogUtils.d("payResult", "payRes: " + payRes + "  payAmount: " + payAmount + "  payTime: " + payTime + "  payOrderId: " + payOrderId);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	//购买产品 余额支付
	private void payBuyProduct(){
		FormEncodingBuilder builder = new FormEncodingBuilder();
		builder.add("amount", mBuyAmount.getText().toString().trim());
		builder.add("product", productId+"");
		builder.add("pay_pwd", mInputPswdEd.getText().toString().trim());
		HttpUtil2.post(InterfaceInfo.BASE_URL + "/appBuy", builder, new Callback() {
			@Override
			public void onResponse2(JSONObject response) {
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							LogUtils.i("WK", "余额支付" + response);
							CommonReq.reqUserMsg(getApplicationContext());
							startActivity(new Intent(TransferInActivity.this,
									TransferInResultActivity.class).
									putExtra("money", mBuyAmount.getText().toString().trim()));
							disMisInputPay();
							finish();
						} else {
							ToastUtil.customAlert(TransferInActivity.this, response.getString("msg"));
						}
					} catch (Exception E) {
					}
				}
			}
			@Override
			public void onFailure(String msg) {
				ToastUtil.customAlert(TransferInActivity.this,"获取数据失败");
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
			String str = mBuyAmount.getText().toString().trim();
			if (str == null || str.equals("") || str.length() == 0) {

			}
		}
		@Override
		public void afterTextChanged(Editable s) {
			String amountMoney = mBuyAmount.getText().toString().trim();
			valify();
			//最多可输入可购买的份额以内的数值
			if (!amountMoney.equals("")&&amountMoney.length()!=0){
				if (Integer.valueOf(amountMoney)>Integer.valueOf(mAmountMoney)){
					mBuyAmount.setText(mAmountMoney);
					mBuyAmount.setSelection(mAmountMoney.length()); // 设置光标的位置
				}
			}
		}
	};
	private void valify(){
		if (!TextUtils.isEmpty(mBuyAmount.getText().toString().trim())&&!isRead){
			mTransferinBtn.setClickable(true);
			mTransferinBtn.setBackgroundResource(R.drawable.button_red_corner_border);
		}else {
			mTransferinBtn.setClickable(false);
			mTransferinBtn.setBackgroundResource(R.drawable.button_grey_corner_border);
		}
	}


	private int bankId ;
	private int banknumber ;
	//转入的卡支付
	private void payBuyLLProduct(){
		FormEncodingBuilder builder=new FormEncodingBuilder();
		builder.add("amount",mBuyAmount.getText().toString().trim());
		builder.add("pamount",mBuyAmount.getText().toString().trim());
		builder.add("bank_id", bankId+"");
		builder.add("product",productId+"");
		builder.add("pay_pwd", mInputPswdEd.getText().toString().trim());
		builder.add("number",banknumber+"");
		builder.add("recharge",mBuyAmount.getText().toString().trim());
		presenter.buyByCard(this,builder);
	}

}

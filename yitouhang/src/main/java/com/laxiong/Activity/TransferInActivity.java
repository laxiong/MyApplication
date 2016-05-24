package com.laxiong.Activity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
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

import com.gongshidai.mistGSD.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.entity.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class TransferInActivity extends BaseActivity implements OnClickListener{
	/****
	 * 转入的
	 */
	private FrameLayout mBack ;
	private TextView mBankCan ; // 银行限制
	private LinearLayout mPayMethod ;
	private TextView mTransferinBtn ,mAmountLimit ,mTransferInProduct,mShowBankName;
	private ImageView mToggleBtn ;
	private EditText mBuyAmount ;
	private int productId ;
	private User user ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transferin);
		initView();
		initData();
		getBankInfo();
	}

	private void initData() {
		mBack.setOnClickListener(this);
		mBankCan.setOnClickListener(this);
		mPayMethod.setOnClickListener(this);
		mTransferinBtn.setOnClickListener(this);
		mToggleBtn.setOnClickListener(this);
		user = YiTouApplication.getInstance().getUser();
		if (user!=null&&mAmountLimit!=null){
			mAmountLimit.setText(""+user.getQuota());
		}
	}

	private void initView() {
		mBack = (FrameLayout)findViewById(R.id.in_backlayout);
		mBankCan = (TextView)findViewById(R.id.bankcan);
		mPayMethod = (LinearLayout)findViewById(R.id.selectpaymethod);
		mTransferinBtn = (TextView)findViewById(R.id.transferinnow);
		mToggleBtn = (ImageView)findViewById(R.id.img_toggle);

		mAmountLimit =(TextView)findViewById(R.id.transferin_limit);
		mTransferInProduct=(TextView)findViewById(R.id.transferin_product);
		mShowBankName =(TextView)findViewById(R.id.showbankname);
		mBuyAmount =(EditText)findViewById(R.id.input_money);
		mBuyAmount.addTextChangedListener(watcher);

		productId =getIntent().getIntExtra("id",-1);
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
					if (isEnough()) {
						inputOverToPswd();
					}else {
						Toast.makeText(TransferInActivity.this, "余额不足",Toast.LENGTH_LONG).show();
					}
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
		}

		mPayMathodWindow = new PopupWindow(PayView,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
		mPayMathodWindow.setTouchable(true);
		mPayMathodWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mPayMathodWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mPayMathodWindow.showAtLocation(PayView, Gravity.BOTTOM, 0, 0);
	}

	// 余额不足的情况
	private boolean isEnough(){
		if (user!=null){
			if (user.getAvailable_amount() < Integer.valueOf(mBuyAmount.getText().toString().trim())) {
				return false ;
			}
		}
		return true ;
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
					}
					break;

				case R.id.concreatebank:  // 建设银行
					choicePayImg(1);
					choiceImg = 1 ;
					mShowBankName.setText(bankname + "(尾号" + bankLastNum + ")");
					selectPay = bankname + "(尾号" + bankLastNum + ")" ;
					break;

				case R.id.concel:
					if(mPayMathodWindow!=null&&mPayMathodWindow.isShowing()){
						mPayMathodWindow.dismiss();
						mPayMathodWindow = null ;
					}
					break;

				case R.id.concel_btn:  // 输入的Text取消
					disMisInputPay();

					break;

				case R.id.imgs_concel:  // 输入的Img取消
					disMisInputPay();

					break;

				case R.id.sure_btn:	// 输入的Text确定
					payBuyProduct();

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
		HttpUtil.get(InterfaceInfo.BASE_URL + "/bank", new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							bankname = response.getString("name");
							mShowBankName.setText(bankname+"(尾号"+response.getInt("snumber")+")");
							selectPay = bankname+"(尾号"+response.getInt("snumber")+")";
//							mAmountLimit.setText(response.getString("one_limit")); 					 // 这是银行卡限额
							logokey = response.getString("logoKey");
							bankLastNum = response.getInt("snumber");
						} else {
							Toast.makeText(TransferInActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
						}
					} catch (Exception E) {
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(TransferInActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
			}
		}, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
				.getUserLogin().getToken()));
	}

	//购买产品
	private void payBuyProduct(){
		RequestParams params = new RequestParams();
		params.put("amount", mBuyAmount.getText().toString().trim());
		params.put("product", productId);
		params.put("pay_pwd", mInputPswdEd.getText().toString().trim());

		HttpUtil.post(InterfaceInfo.BASE_URL + "/appBuy", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							startActivity(new Intent(TransferInActivity.this,
									TransferInResultActivity.class));
							disMisInputPay();
						} else {
							Toast.makeText(TransferInActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
						}
					} catch (Exception E) {
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(TransferInActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
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
			if (!TextUtils.isEmpty(mBuyAmount.getText().toString().trim())){
				mTransferinBtn.setClickable(true);
				mTransferinBtn.setBackgroundResource(R.drawable.button_red_corner_border);
			}else {
				mTransferinBtn.setClickable(false);
				mTransferinBtn.setBackgroundResource(R.drawable.button_grey_corner_border);
			}
		}
	};



}

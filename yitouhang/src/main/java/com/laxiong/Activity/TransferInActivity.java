package com.laxiong.Activity;

import android.annotation.SuppressLint;
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

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.yitouhang.R;
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
	private String logokey ;
	private String bankname ;
	private EditText mBuyAmount ;
	private int productId ;

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
				inputOverToPswd();
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
	private ImageView newCard_img,lateMoney_img,constranceBank_img ;
	private ImageView BankIcon,LateMoneyIcon ,NewCardIcon;
	private TextView mConcel ,mBankName,mMyYuE,mNewCard;
	private void payMenthodType(){

		PayView = LayoutInflater.from(TransferInActivity.this).inflate(R.layout.pay_mathod_popwindow, null);

		RelativeLayout newCard = (RelativeLayout)PayView.findViewById(R.id.addnewcard);
		RelativeLayout lateMoney = (RelativeLayout)PayView.findViewById(R.id.latemoney);
		RelativeLayout constranceBank = (RelativeLayout)PayView.findViewById(R.id.concreatebank);
		TextView mConcel = (TextView)PayView.findViewById(R.id.concel);

		newCard_img = (ImageView)PayView.findViewById(R.id.change_img_addnewcard);
		lateMoney_img = (ImageView)PayView.findViewById(R.id.change_img_latemoney);
		constranceBank_img = (ImageView)PayView.findViewById(R.id.change_img_concreatebank);

		newCard.setOnClickListener(listenner);
		lateMoney.setOnClickListener(listenner);
		constranceBank.setOnClickListener(listenner);
		mConcel.setOnClickListener(listenner);

		//银行卡等信息
		mBankName =(TextView)PayView.findViewById(R.id.bankname);
		mMyYuE =(TextView)PayView.findViewById(R.id.myyue);
		mNewCard =(TextView)PayView.findViewById(R.id.newcard);
		//Item卡的图片
		BankIcon =(ImageView)PayView.findViewById(R.id.icon1);
		LateMoneyIcon =(ImageView)PayView.findViewById(R.id.icon2);
		NewCardIcon =(ImageView)PayView.findViewById(R.id.icon3);

		if (bankname!=null) {
			mBankName.setText(bankname);
			constranceBank_img.setImageResource(R.drawable.img_read);
		}
		if (logokey!=null)
			BankIcon.setImageResource(getResources().getIdentifier("logo_" + logokey, "drawable", getPackageName()));

		mPayMathodWindow = new PopupWindow(PayView,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
		mPayMathodWindow.setTouchable(true);
		mPayMathodWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mPayMathodWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mPayMathodWindow.showAtLocation(PayView, Gravity.BOTTOM, 0, 0);
	}


	OnClickListener listenner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			initNoSelect();
			switch(v.getId()){
				case R.id.addnewcard:  // 加新卡
					newCard_img.setImageResource(R.drawable.img_read);
					mShowBankName.setText("从新卡里取");
					break;
				case R.id.latemoney:  //  余额
					lateMoney_img.setImageResource(R.drawable.img_read);
					mShowBankName.setText("账户余额");
					break;
				case R.id.concreatebank:  // 建设银行
					constranceBank_img.setImageResource(R.drawable.img_read);
					mShowBankName.setText(bankname);
					break;
				case R.id.concel:
					if(mPayMathodWindow!=null&&mPayMathodWindow.isShowing()){
						mPayMathodWindow.dismiss();
						mPayMathodWindow = null ;
					}
					break;
			}
		}
	};

	private void initNoSelect(){
		newCard_img.setImageResource(R.drawable.img_no_read);
		lateMoney_img.setImageResource(R.drawable.img_no_read);
		constranceBank_img.setImageResource(R.drawable.img_no_read);
	}

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

		mNoticeTopayMoney.setText("从"+bankname+"-转入-"+"时息通");
		mTranInMoney.setText(mBuyAmount.getText().toString().trim()+"元");

		comcelImags.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mInputPswdWindow!=null&&mInputPswdWindow.isShowing()){
					mInputPswdWindow.dismiss();
					mInputPswdWindow = null ;
				}
			}
		});

		concelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mInputPswdWindow!=null&&mInputPswdWindow.isShowing()){
					mInputPswdWindow.dismiss();
					mInputPswdWindow = null ;
				}
			}
		});

		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				payBuyProduct();

				if(mInputPswdWindow!=null&&mInputPswdWindow.isShowing()){
					mInputPswdWindow.dismiss();
					mInputPswdWindow = null ;
				}

			}
		});

		mForgetPswd.setOnClickListener(new OnClickListener() {
			@SuppressLint("InlinedApi") @Override
			public void onClick(View arg0) {
				Toast.makeText(TransferInActivity.this, "忘记密码的操作",Toast.LENGTH_LONG).show();
			}
		});

		mInputPswdWindow = new PopupWindow(mInputView,  LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
		mInputPswdWindow.setTouchable(true);
		mInputPswdWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mInputPswdWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mInputPswdWindow.showAtLocation(mInputView, Gravity.BOTTOM, 0, 0);
	}

	private void getBankInfo(){

		HttpUtil.get(InterfaceInfo.BASE_URL + "/bank", new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							bankname = response.getString("name");
							mShowBankName.setText(bankname);
							mAmountLimit.setText(response.getString("one_limit"));
							logokey = response.getString("logoKey");
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

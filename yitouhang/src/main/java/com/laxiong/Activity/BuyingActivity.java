package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
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
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Adapter.RedPaper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

public class BuyingActivity extends BaseActivity implements OnClickListener{
	/***
	 * 立即购买页面
	 */
	private static final int REQUEST_CODE=1;
	private LinearLayout mChangeBankTpye,ll_redpaper;
	private FrameLayout mBack ;
	private TextView mShowBankName,mMoneyLimit ,mProjectName,mAmountMoney;
	private TextView mBuyBtn,tv_paper;
	private ImageView mToggleBtn;

	private String mProjectStr ;
	private String mAmountStr ;
	private int productId ;
	private EditText mBuyAmount ; //购买的份额
	private List<RedPaper> listpaper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buying);

		mProjectStr = getIntent().getStringExtra("projectStr");
		mAmountStr = getIntent().getStringExtra("amountStr");
		productId = getIntent().getIntExtra("id",-1);

		initView();
		initData();
		getBankInfo();
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
		ll_redpaper= (LinearLayout) findViewById(R.id.ll_redpaper);
		mChangeBankTpye = (LinearLayout)findViewById(R.id.changebank);
		mBack = (FrameLayout)findViewById(R.id.backlayout_);
		mBuyBtn = (TextView)findViewById(R.id.buyingbtn);
		mToggleBtn = (ImageView)findViewById(R.id.img_toggle);
		tv_paper= (TextView) findViewById(R.id.tv_paper);

		mShowBankName =(TextView)findViewById(R.id.showBankname);
		mMoneyLimit =(TextView)findViewById(R.id.one_limit);
		mAmountMoney =(TextView)findViewById(R.id.project_amount);
		mProjectName =(TextView)findViewById(R.id.project_name);
		mBuyAmount =(EditText)findViewById(R.id.buyamount);

		mBuyAmount.addTextChangedListener(watcher);
	}

	@Override
	public void onClick(View V) {
		switch(V.getId()){
			case R.id.changebank:
				payMenthodType();
				break;
			case R.id.backlayout_:
				this.finish();
				break;
			case R.id.buyingbtn:
				if (Common.inputContentNotNull(mBuyAmount.getText().toString().trim())){
					inputOverToPswd();
				}else {
					Toast.makeText(BuyingActivity.this, "请输入购买金额",Toast.LENGTH_LONG).show();
				}

				break;
			case R.id.ll_redpaper:
				Intent intent=new Intent(BuyingActivity.this,WelCenterActivity.class);
				intent.putExtra("isBuying", true);
				BuyingActivity.this.startActivityForResult(intent,REQUEST_CODE);
			break;
			case R.id.img_toggle:
				readProcotol();
				break;
		}
	}
	//处理红包选择回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null&&resultCode==RESULT_OK){
			listpaper=data.getParcelableArrayListExtra("data");
			if(listpaper!=null&&listpaper.size()>0){
				int total=0;
				for(RedPaper paper:listpaper){
					total+=paper.getAmount();
				}
				tv_paper.setText(total+"");
			}
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

		PayView = LayoutInflater.from(BuyingActivity.this).inflate(R.layout.pay_mathod_popwindow, null);

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
			mBankName.setText(bankname+"(尾号"+bankLastNum+")");
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

	private String logokey ;
	private String bankname ;
	private  int bankLastNum ;
//	private void payMenthodType(){
//		PayMethodSelectPop mPay = new PayMethodSelectPop(this);
//		mPay.setBanklogokey(logokey);
//		mPay.setmBankStr(bankname);
//		mPay.showPopLocation();
//	}

	// 转入按钮  输入密码
	private PopupWindow mInputPswdWindow ;
	private View mInputView ;
	private EditText mInputPswdEd ;
	private TextView mNoticeTopayMoney ,mTranInMoney;
	private void inputOverToPswd(){

		mInputView = LayoutInflater.from(BuyingActivity.this).inflate(R.layout.overtopswd_popwindow, null);

		ImageView comcelImags = (ImageView)mInputView.findViewById(R.id.imgs_concel);
		TextView  mForgetPswd = (TextView)mInputView.findViewById(R.id.forget_pswd);
		TextView concelBtn = (TextView)mInputView.findViewById(R.id.concel_btn);
		TextView sureBtn = (TextView)mInputView.findViewById(R.id.sure_btn);
		mInputPswdEd =(EditText)mInputView.findViewById(R.id.inputpswd);
		mNoticeTopayMoney =(TextView)mInputView.findViewById(R.id.topaymoney);
		mTranInMoney =(TextView)mInputView.findViewById(R.id.zhuang_money);

		mNoticeTopayMoney.setText("从"+bankname+"-转入-"+mProjectStr);
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
				Toast.makeText(BuyingActivity.this, "忘记密码的操作",Toast.LENGTH_LONG).show();
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
						Log.i("WKKKKKK", "购买页面：" + response);
						if (response.getInt("code") == 0) {
							bankname = response.getString("name");
							mShowBankName.setText(bankname+"(尾号"+response.getInt("snumber")+")");
							mMoneyLimit.setText(response.getString("one_limit"));
							logokey = response.getString("logoKey");
							bankLastNum = response.getInt("snumber");
						} else {
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

	//购买产品
	private void payBuyProduct(){
		RequestParams params = new RequestParams();
		params.put("amount",mBuyAmount.getText().toString().trim());
		params.put("product", productId);
		params.put("pay_pwd",mInputPswdEd.getText().toString().trim());
		// 抵扣的红包尚未加上

		HttpUtil.post(InterfaceInfo.BASE_URL+"/appBuy",params,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response!=null){
					try {
						Log.i("lalalalalalalal", "Buy" + response);
						if (response.getInt("code") == 0) {

							startActivity(new Intent(BuyingActivity.this,
									BuyingResultActivity.class));
						}else {
							Toast.makeText(BuyingActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
						}
					}catch (Exception E){
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(BuyingActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
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
			if (!TextUtils.isEmpty(mBuyAmount.getText().toString().trim())){
				mBuyBtn.setClickable(true);
				mBuyBtn.setBackgroundResource(R.drawable.button_red_corner_border);
			}else {
				mBuyBtn.setClickable(false);
				mBuyBtn.setBackgroundResource(R.drawable.button_grey_corner_border);
			}
		}
	};

}

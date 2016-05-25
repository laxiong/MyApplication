package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.entity.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.gongshidai.mistGSD.R;
import org.apache.http.Header;
import org.json.JSONObject;

public class TransferOutActivity extends BaseActivity implements View.OnClickListener{
	/***
	 * 转出
	 */
	private TextView mSureTransferOut ,mTransferOutMoney;
	private EditText mBuyAmount;
	private String mAmountMoney ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transferout);
		initView();
	}

	private void initView() {
		TextView mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText("转出");
		FrameLayout mBackLayout = (FrameLayout)findViewById(R.id.back_layout);
		mSureTransferOut =(TextView)findViewById(R.id.suretranferout);
		mBuyAmount =(EditText)findViewById(R.id.input_transferout);

		mTransferOutMoney =(TextView)findViewById(R.id.transferout_money);
		mBackLayout.setOnClickListener(this);
		mSureTransferOut.setOnClickListener(this);
		mBuyAmount.addTextChangedListener(watcher);


		User mUser =YiTouApplication.getInstance().getUser();
		if (mUser!=null) {
			int CurMoney = mUser.getCurrent();
			mAmountMoney = String.valueOf(CurMoney);
		}else {
			mAmountMoney = "0.0";
		}
		mTransferOutMoney.setText(mAmountMoney);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back_layout:
				TransferOutActivity.this.finish();
				break;
			case R.id.suretranferout:
				inputOverToPswd();
				break;
		}
	}

	// 转入按钮  输入密码
	private PopupWindow mInputPswdWindow ;
	private View mInputView ;
	private EditText mInputPswdEd ;
	private TextView mNoticeTopayMoney ,mTranOutMoney;
	private void inputOverToPswd(){

		mInputView = LayoutInflater.from(TransferOutActivity.this).inflate(R.layout.overtopswd_popwindow, null);

		ImageView comcelImags = (ImageView)mInputView.findViewById(R.id.imgs_concel);
		TextView  mForgetPswd = (TextView)mInputView.findViewById(R.id.forget_pswd);
		TextView concelBtn = (TextView)mInputView.findViewById(R.id.concel_btn);
		TextView sureBtn = (TextView)mInputView.findViewById(R.id.sure_btn);
		mInputPswdEd =(EditText)mInputView.findViewById(R.id.inputpswd);
		mNoticeTopayMoney =(TextView)mInputView.findViewById(R.id.topaymoney);
		mTranOutMoney =(TextView)mInputView.findViewById(R.id.zhuang_money);

		mNoticeTopayMoney.setText("从时息通"+"-转出-"+"我的余额");
		mTranOutMoney.setText(mBuyAmount.getText().toString().trim()+"元");

		comcelImags.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismissWindows();
			}
		});
		concelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismissWindows();
			}
		});
		sureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				payBuyProduct();
			}
		});

		mForgetPswd.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi") @Override
			public void onClick(View arg0) {
				Toast.makeText(TransferOutActivity.this, "忘记密码的操作", Toast.LENGTH_LONG).show();
			}
		});

		mInputPswdWindow = new PopupWindow(mInputView,  ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT,true);
		mInputPswdWindow.setTouchable(true);
		mInputPswdWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mInputPswdWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mInputPswdWindow.showAtLocation(mInputView, Gravity.BOTTOM, 0, 0);
	}

	private void dismissWindows(){
		if(mInputPswdWindow!=null&&mInputPswdWindow.isShowing()){
			mInputPswdWindow.dismiss();
			mInputPswdWindow = null ;
		}
	}

	//购买产品
	private void payBuyProduct(){
		RequestParams params = new RequestParams();
		params.put("amount", mBuyAmount.getText().toString().trim());
		params.put("pay_pwd", mInputPswdEd.getText().toString().trim());

		HttpUtil.post(InterfaceInfo.BASE_URL + "/appBuy", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							startActivity(new Intent(TransferOutActivity.this,
									TransferOutResultActivity.class).
									putExtra("money", mBuyAmount.getText().toString().trim()));
							dismissWindows();
						} else {
							Toast.makeText(TransferOutActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
						}
					} catch (Exception E) {
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(TransferOutActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
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
			if (!TextUtils.isEmpty(amountMoney)){
				mSureTransferOut.setClickable(true);
				mSureTransferOut.setBackgroundResource(R.drawable.button_red_corner_border);
			}else {
				mSureTransferOut.setClickable(false);
				mSureTransferOut.setBackgroundResource(R.drawable.button_grey_corner_border);
			}

			//最多可转出的数值
			if (!amountMoney.equals("")&&amountMoney.length()!=0){
				if (Integer.valueOf(amountMoney)>Integer.valueOf(mAmountMoney)){
					mBuyAmount.setText(mAmountMoney);
					mBuyAmount.setSelection(mAmountMoney.length()); // 设置光标的位置
				}
			}


		}
	};


}

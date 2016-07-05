package com.laxiong.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

public class RechargeActivity extends BaseActivity implements View.OnClickListener,IViewCommonBack{
	/***
	 * 充值页面
	 */
	private FrameLayout mBack;
	private TextView mRechargeBtn, mPayBank;
	private ImageView mToggleBtn;
	private EditText mInputRecharge; //输入的支付金额
	private Buy_Presenter presenter;
	/**
	 */
	private int bankId ;
	private String bankName ;
	private String bankCardNum ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		initView();
		getBankInfo();
	}

	@Override
	public void reqbackSuc(String tag) {

	}

	@Override
	public void reqbackFail(String msg, String tag) {
		ToastUtil.customAlert(this, msg);
	}

	private void initView() {
		presenter=new Buy_Presenter(this);
		mBack = (FrameLayout) findViewById(R.id.back_layout);
		mRechargeBtn = (TextView) findViewById(R.id.rechargeBtn);
		mToggleBtn = (ImageView) findViewById(R.id.toggle_img);
		mPayBank = (TextView) findViewById(R.id.pay_bank); // 选择的银行卡显示的内容
		mInputRecharge = (EditText) findViewById(R.id.input_recharge);

		mBack.setOnClickListener(this);
		mRechargeBtn.setOnClickListener(this);
		mToggleBtn.setOnClickListener(this);
		mInputRecharge.addTextChangedListener(watcher);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.back_layout:
				this.finish();
				break;
			case R.id.rechargeBtn:
				getOrderInfo();
				break;
			case R.id.toggle_img:
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

	TextWatcher watcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		@Override
		public void afterTextChanged(Editable s) {
			valify();
		}
	};
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
					showAppayRes("充值成功！");
					CommonReq.reqUserMsg(getApplicationContext());
					startActivity(new Intent(RechargeActivity.this,
							RechargeResultActivity.class).
							putExtra("money", mInputRecharge.getText().toString().trim()).
							putExtra("bankNum", bankInfo));
					finish();
				} else {
					showAppayRes("充值失败！");
				}
				LogUtils.d("payResult", "payRes: " + payRes + "  payAmount: " + payAmount + "  payTime: " + payTime + "  payOrderId: " + payOrderId);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void valify(){
		if (!TextUtils.isEmpty(mInputRecharge.getText().toString().trim())&&!isRead){
			mRechargeBtn.setEnabled(true);
			mRechargeBtn.setBackgroundResource(R.drawable.button_change_bg_border);
		}else {
			mRechargeBtn.setEnabled(false);
			mRechargeBtn.setBackgroundResource(R.drawable.button_grey_corner_border);
		}
	}
	// 获取订单信息
	private void getOrderInfo() {
		presenter.recharge(this,mInputRecharge.getText().toString().trim(),bankId+"");
	}

	private String bankInfo ;
	// 获取银行卡信息
	private void getBankInfo() {
		HttpUtil2.get(InterfaceInfo.BASE_URL + "/bank", new Callback() {
			@Override
			public void onResponse2(JSONObject response) {
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							bankId = response.getInt("id");
							bankName = response.getString("name");
							bankCardNum = response.getString("number");
							bankInfo = response.getString("name")+"(尾号"+response.getInt("snumber")+")" ;
							mPayBank.setText(bankInfo);
						} else {
							ToastUtil.customAlert(RechargeActivity.this, response.getString("msg"));
						}
					} catch (Exception E) {
					}
				}
			}
			@Override
			public void onFailure(String msg) {
				ToastUtil.customAlert(RechargeActivity.this,"获取数据失败");
			}
		}, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
				.getUserLogin().getToken()));
	}

}

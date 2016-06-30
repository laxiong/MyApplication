package com.laxiong.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongshidai.mistGSD.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TransferInResultActivity extends BaseActivity implements OnClickListener{
	/***
	 * 转入结果界面
	 */
	private FrameLayout mBack ;
	private TextView mBankName,mTransferInMoney,mTransferInDate,tv_ok;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tranferin_result);
		initView();
		initData();
		getBankInfo();
	}

	private void initData() {
		mBack.setOnClickListener(this);
	}

	private void initView() {
		TextView mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText("转入");

		mBankName = (TextView)findViewById(R.id.bankname);
		mTransferInMoney = (TextView)findViewById(R.id.tranferin_money);
		mBack = (FrameLayout)findViewById(R.id.back_layout);
		mTransferInDate =(TextView)findViewById(R.id.tranferin_date);
		tv_ok= (TextView) findViewById(R.id.tv_ok);
		String money = getIntent().getStringExtra("money");
		mTransferInMoney.setText(money);
		SharedPreferences sxtff = getSharedPreferences("SXT_DATE", Context.MODE_PRIVATE);
		if (sxtff!=null){
			String dates = sxtff.getString("sxt_date", "2015/5/4");
			mTransferInDate.setText(dates);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.back_layout:
				this.finish();
				break;
			case R.id.tv_ok:
				this.finish();
				break;
		}
	}

	// 获取银行卡信息
	private void getBankInfo(){
		HttpUtil.get(InterfaceInfo.BASE_URL + "/bank", new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							mBankName.setText(response.getString("name") + "(尾号" + response.getInt("snumber") + ")");
						} else {
							Toast.makeText(TransferInResultActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
						}
					} catch (Exception E) {
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(TransferInResultActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
			}
		}, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
				.getUserLogin().getToken()));



	}

}

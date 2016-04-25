package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.yitouhang.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;

public class TimeXiTongActivity extends BaseActivity implements OnClickListener{
	 /***
	  * 时息通
	  */
	private FrameLayout mBack ;
	private TextView mJiGetMoney , mShareBtn ,mScrollIn , mScrollOut;
	private EditText mJiMoney , mJiDay ;
	private TextView mYesDayProfit,mAmountProfit,mGetCashProfit,mPrecent,mRemark1,mRemark2,mLastCash,SxtTitle;
	private int mId ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_shixitong_layout);
		initView();
		initData();
		getNetWork();
	}

	private void initData() {
		
		mBack.setOnClickListener(this);
		mShareBtn.setOnClickListener(this);
		mScrollIn.setOnClickListener(this);
		mScrollOut.setOnClickListener(this);
		
		mJiDay.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String str = mJiDay.getText().toString().trim();
				if (str == null || str.equals("") || str.length() == 0) {
					Toast.makeText(TimeXiTongActivity.this, "输入整数", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
										  int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				String day = mJiDay.getText().toString().trim();

				//TODO  TextView 的计算结果显示
				if (mJiMoney != null && mJiMoney.getText().toString().trim().length() != 0 && !mJiMoney.getText().toString().trim().equals("")) {
					if (day != null && day.length() != 0 && !day.equals("")) {

						int tD = Integer.parseInt(day);
						int tM = Integer.parseInt(mJiMoney.getText().toString().trim());
						double lu = 0.072;

						// 保留小数点三位
						NumberFormat mFormat = NumberFormat.getNumberInstance();
						mFormat.setMaximumFractionDigits(3);
						String comfixNum = mFormat.format(backComfix(tM, tD, lu));

						mJiGetMoney.setText(comfixNum);

					}
				}
			}
		});
		
		mJiMoney.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String str = mJiMoney.getText().toString().trim();
				if (str == null || str.equals("") || str.length() == 0) {
					Toast.makeText(TimeXiTongActivity.this, "输入整数", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
										  int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				String money = mJiMoney.getText().toString().trim();

				//TODO  TextView 的计算结果显示
				if (mJiDay != null && mJiDay.getText().toString().trim().length() != 0 && !mJiDay.getText().toString().trim().equals("")) {
					if (money != null && money.length() != 0 && !money.equals("")) {

						int tM = Integer.parseInt(money);
						int tD = Integer.parseInt(mJiDay.getText().toString().trim());
						double lu = 0.72;

						// 保留小数点三位
						NumberFormat mFormat = NumberFormat.getNumberInstance();
						mFormat.setMaximumFractionDigits(3);
						String comfixNum = mFormat.format(backComfix(tM, tD, lu));

						mJiGetMoney.setText(comfixNum);
					}
				}
			}
		});
		
	}

	private void initView() {
		mJiDay = (EditText)findViewById(R.id.payday);
		mJiMoney = (EditText)findViewById(R.id.paymoney);
		mJiGetMoney = (TextView)findViewById(R.id.jigetmoney);
		mBack = (FrameLayout)findViewById(R.id.backlayout);
		mShareBtn = (TextView)findViewById(R.id.share);
		mScrollIn = (TextView)findViewById(R.id.scroll_in);
		mScrollOut = (TextView)findViewById(R.id.scroll_out);

		mRemark1 =(TextView)findViewById(R.id.remark1);
		mRemark2 =(TextView)findViewById(R.id.remark2);
		mPrecent =(TextView)findViewById(R.id.tv2);
		mYesDayProfit =(TextView)findViewById(R.id.yesdayprofit);
		mAmountProfit =(TextView)findViewById(R.id.amountprofit);
		mGetCashProfit =(TextView)findViewById(R.id.getcashprofit);
		mLastCash=(TextView)findViewById(R.id.text2);
		SxtTitle =(TextView)findViewById(R.id.sxt_title);
		mId = getIntent().getIntExtra("id",-1);
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.scroll_in:  // 转入
				startActivity(new Intent(TimeXiTongActivity.this,
						TransferInActivity.class));
				break;
			case R.id.backlayout:
				this.finish();
				break;
			case R.id.share:
				Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.scroll_out:  // 转出
				startActivity(new Intent(TimeXiTongActivity.this,
						TransferOutActivity.class));
				break;
		}
	}
	/**
	 * 计算器的算法
	 * money:本金
	 * day：日期
	 * lu：利率  7.2%
	 */
	private double backComfix(float money,float day, double lu){
		double backMoney = money*lu*(day/365)+money;
		
		return backMoney ;
	}

	private void getNetWork(){
		RequestParams params = new RequestParams();
		if (mId!=-1)
			params.put("id",mId);
		HttpUtil.get(InterfaceInfo.PRODUCT_URL,params,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response!=null){
					try {
						if (response.getInt("code")==0){

							updataUi(response);

						}else {
							Toast.makeText(TimeXiTongActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
						}
					}catch (Exception E){
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(TimeXiTongActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void updataUi(JSONObject response){
		if (response!=null){
			try{
				mPrecent.setText(String.valueOf(response.getDouble("apr")));
				mLastCash.setText(String.valueOf(response.getInt("members")));
				JSONArray ARRA = response.getJSONArray("details");
				if (ARRA.length()>0){
					mRemark1.setText(ARRA.getString(0));
					mRemark2.setText(ARRA.getString(1));
				}
				SxtTitle.setText(response.getString("title"));
			}catch (Exception E){
			}
		}
	}


	
}

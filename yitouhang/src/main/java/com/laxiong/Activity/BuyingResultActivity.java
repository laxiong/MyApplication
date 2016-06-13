package com.laxiong.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
import com.laxiong.Utils.CommonReq;

import java.text.SimpleDateFormat;


public class BuyingResultActivity extends BaseActivity implements View.OnClickListener{
	/***
	 * 购买结果页面
	 */
	private String Money ;
	private String ProductName ;
	private String LastDate ;
	private TextView mProject,mBuyDate,mBuyMoney,mLastDate ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buying_result);
		initView();
		initData();
		setDatas();
	}

	private void initView() {
		TextView mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText("购买结果");
		FrameLayout mBack = (FrameLayout)findViewById(R.id.back_layout);
		mBack.setOnClickListener(this);

		mProject = (TextView)findViewById(R.id.product_name);
		mBuyMoney = (TextView)findViewById(R.id.pay_money);
		mBuyDate =(TextView)findViewById(R.id.tv_buy_time);
		mLastDate = (TextView)findViewById(R.id.tv_last_time);
	}

	private void initData(){
		CommonReq.reqUserMsg(getApplicationContext());
		ProductName = getIntent().getStringExtra("ProductName");
		Money = getIntent().getStringExtra("Money");
		SharedPreferences GXBssf = getSharedPreferences("GXB_DATE", Context.MODE_PRIVATE);
		if (GXBssf!=null){
			LastDate = GXBssf.getString("gxb_date","2015/5/4");
		}
	}

	private void setDatas(){
		mProject.setText(ProductName);
		mBuyMoney.setText(Money);
		mLastDate.setText(LastDate);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String date=sdf.format(new java.util.Date());
		mBuyDate.setText(date);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back_layout:
				this.finish();
				break;
		}
	}
}

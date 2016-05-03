package com.laxiong.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laxiong.yitouhang.R;

public class BuyingResultActivity extends BaseActivity implements View.OnClickListener{
	/***
	 * 购买结果页面
	 */
	private FrameLayout mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buying_result);
		initView();
		initData();
	}

	private void initView() {
		TextView mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText("购买结果");
		mBack =(FrameLayout)findViewById(R.id.back_layout);
	}

	private void initData(){
		mBack.setOnClickListener(this);
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

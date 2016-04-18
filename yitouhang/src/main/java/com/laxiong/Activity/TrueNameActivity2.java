package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.laxiong.yitouhang.R;

public class TrueNameActivity2 extends BaseActivity implements OnClickListener{
	/***
	 * 实名认证第二步
	 */
	private TextView mNextPage ;
	private FrameLayout mBack ;
	private ImageView mShowPswd ;
	private EditText mPswdEd ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_true_name2);
		initView();
		initData();
	}
	private void initData() {
		mNextPage.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mShowPswd.setOnClickListener(this);
	}
	private void initView() {
		mNextPage = (TextView)findViewById(R.id.nextpager);
		mBack = (FrameLayout)findViewById(R.id.back_layout);
		TextView mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText("实名认证");

		mShowPswd =(ImageView)findViewById(R.id.img_showpswd);
		mPswdEd = (EditText)findViewById(R.id.pswd);
		
	}
	@Override
	public void onClick(View V) {
		switch(V.getId()){
			case R.id.nextpager:
				startActivity(new Intent(TrueNameActivity2.this,
						TrueNameActivity3.class));
				break;
			case R.id.back_layout:
				this.finish();
				break;
			case R.id.img_showpswd:
				showPassWord();
				break;
		}
	}

	// show password
	private boolean isShowed = false ;
	private void showPassWord(){
		if(isShowed){ // 隐藏
			mShowPswd.setImageResource(R.drawable.img_eye_close);
			mPswdEd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			isShowed = false;
		}else{		//显示
			mShowPswd.setImageResource(R.drawable.img_eye_open);
			mPswdEd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			isShowed = true;
		}
	}

}

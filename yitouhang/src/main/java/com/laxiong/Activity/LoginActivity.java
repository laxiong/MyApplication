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
import android.widget.Toast;

import com.laxiong.Common.Common;
import com.laxiong.yitouhang.R;

public class LoginActivity extends BaseActivity implements OnClickListener{
	/***
	 * 登录
	 */
	private TextView mRegistBtn , mLoginBtn ,mFindPswd,mChangeCount;
	private FrameLayout mBack ;
	private ImageView mShowPswd ;
	private EditText mPswd ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_layout);
		initView();
		initData();
	}
	private void initData() {
		mRegistBtn.setOnClickListener(this);
		mLoginBtn.setOnClickListener(this);
		mFindPswd.setOnClickListener(this);
		mChangeCount.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mShowPswd.setOnClickListener(this);
	}
	private void initView() {
		mRegistBtn = (TextView)findViewById(R.id.registBtn);
		mLoginBtn = (TextView)findViewById(R.id.loginBtn);
		mChangeCount = (TextView)findViewById(R.id.changecount);
		mFindPswd = (TextView)findViewById(R.id.findpswd);
		mBack = (FrameLayout)findViewById(R.id.backlayout);
		mShowPswd = (ImageView)findViewById(R.id.img_showpswd);
		mPswd = (EditText)findViewById(R.id.password);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.registBtn:
				startActivity(new Intent(LoginActivity.this,
						RegistActivity.class));
				break;
			case R.id.loginBtn:
				Toast.makeText(this, "登录", 2).show();
				break;
			case R.id.changecount:
				startActivity(new Intent(LoginActivity.this,
						ChangeCountActivity.class));			
				break;
			case R.id.findpswd:
				startActivity(new Intent(LoginActivity.this,
						FoundPswdActivity.class));
				break;
			case R.id.backlayout:
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
			mPswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			isShowed = false;
		}else{		//显示
			mShowPswd.setImageResource(R.drawable.img_eye_open);
			mPswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			isShowed = true;
		}
	}



}

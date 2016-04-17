package com.laxiong.Activity;

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

import com.laxiong.yitouhang.R;

public class ChangeLoginPswdActivity extends BaseActivity implements OnClickListener{
	/***
	 * 修改登录密码
	 */
	private FrameLayout mBack ;
	private TextView mComplete ;
	private EditText mPswd ;
	private ImageView mShowPswd ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_login_pswd);
		initView();
		initData();
	}

	private void initData() {
		mBack.setOnClickListener(this);
		mComplete.setOnClickListener(this);
		mShowPswd.setOnClickListener(this);
	}

	private void initView() {
		mBack = (FrameLayout)findViewById(R.id.back_layout);
		mComplete = (TextView)findViewById(R.id.complete);
		
		TextView mText = (TextView)findViewById(R.id.title);
		mText.setText("修改登录密码");

		mPswd = (EditText)findViewById(R.id.pswd);
		mShowPswd = (ImageView)findViewById(R.id.img_showpswd);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.back_layout:
				this.finish();
				break;
			case R.id.complete:
				Toast.makeText(this, "完成", Toast.LENGTH_SHORT).show();
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

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

public class FoundPswdActivity extends BaseActivity implements OnClickListener{
	/****
	 * 找回密码
	 */
	private TextView mCompletBtn ;
	private FrameLayout mBack ;
	private EditText mPswd ;
	private ImageView mShowPswd ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foundpswd_layout);
		initView();
		initData();
	}

	private void initData() {
		mCompletBtn.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mShowPswd.setOnClickListener(this);
	}

	private void initView() {
		mCompletBtn = (TextView)findViewById(R.id.complete_Btn);
		mBack = (FrameLayout)findViewById(R.id.back);
		mShowPswd = (ImageView)findViewById(R.id.img_showpswd);
		mPswd = (EditText)findViewById(R.id.pswd);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.complete_Btn:
				Toast.makeText(this, "完成", Toast.LENGTH_SHORT).show();
				break;
			case R.id.back:
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

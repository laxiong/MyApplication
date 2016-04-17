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

public class ChangePayPswdActivity2 extends BaseActivity implements OnClickListener{
	/****
	 * 修改支付密码第二层
	 */
	private TextView mComplete ;
	private FrameLayout mBack ;
	private ImageView mShowPswd;
	private EditText mPswdEd ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changepay_pswd2);
		initView();
		initData();
	}

	private void initData() {
		mComplete.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mShowPswd.setOnClickListener(this);
	}

	private void initView() {
		mComplete = (TextView)findViewById(R.id.complete);
		mBack = (FrameLayout)findViewById(R.id.back_layout);
		
		TextView mText =(TextView)findViewById(R.id.title);
		mText.setText("修改支付密码");

		mShowPswd = (ImageView)findViewById(R.id.img_showpswd);
		mPswdEd = (EditText)findViewById(R.id.pswd);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.complete:
				Toast.makeText(this, "完成", Toast.LENGTH_SHORT).show();
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

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

import com.laxiong.yitouhang.R;

public class ChangeCountActivity extends BaseActivity implements OnClickListener{
	/***
	 * 切换账号
	 */
	private TextView mRegistBtn,mFindPswd ,mComplete;
	private FrameLayout mBack;
	private ImageView mShowPswd ;
	private EditText mPswd ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changecount_layout);
		initView();
		initData();
	}
	private void initData() {
		mFindPswd.setOnClickListener(this);
		mRegistBtn.setOnClickListener(this);
		mComplete.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mShowPswd.setOnClickListener(this);
	}
	private void initView() {
		mRegistBtn = (TextView)findViewById(R.id.registBtn);
		mFindPswd = (TextView)findViewById(R.id.findpswd);
		mComplete = (TextView)findViewById(R.id.completeBtn);
		mBack = (FrameLayout)findViewById(R.id.backlayout);

		mPswd =(EditText)findViewById(R.id.pswd);
		mShowPswd = (ImageView)findViewById(R.id.img_showpswd);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.registBtn:
				startActivity(new Intent(ChangeCountActivity.this,
						RegistActivity.class));
				break;
			case R.id.findpswd:
				startActivity(new Intent(ChangeCountActivity.this,
						FoundPswdActivity.class));
				break;
			case R.id.backlayout:
				ChangeCountActivity.this.finish();
				break;
			case R.id.completeBtn:
				Toast.makeText(ChangeCountActivity.this, "完成", Toast.LENGTH_SHORT).show();
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

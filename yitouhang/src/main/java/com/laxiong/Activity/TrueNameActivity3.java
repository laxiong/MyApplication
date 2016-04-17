package com.laxiong.Activity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.laxiong.yitouhang.R;

public class TrueNameActivity3 extends BaseActivity implements OnClickListener{
	/****
	 * 实名认证第三步
	 */
	private TextView mFinish ;
	private FrameLayout mBack ;
	private ImageView toggleRead , mShowPswd;
	private EditText mPswdEd ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_true_name3);
		initView();
		initData();
	}
	private void initData() {
		mFinish.setOnClickListener(this);
		mBack.setOnClickListener(this);
		toggleRead.setOnClickListener(this);
		mShowPswd.setOnClickListener(this);
	}
	private void initView() {
		mFinish = (TextView)findViewById(R.id.fininsh);
		mBack = (FrameLayout)findViewById(R.id.back_layout);
		toggleRead = (ImageView)findViewById(R.id.toggle);
		TextView mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText("实名认证");

		mPswdEd = (EditText)findViewById(R.id.pswd);
		mShowPswd =(ImageView)findViewById(R.id.img_showpswd);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.fininsh:
				showFinishDialog();
				break;
			case R.id.back_layout:
				startActivity(new Intent(this,
						PersonalSettingActivity.class)); 	// 启动到SingTask模式
				this.finish();
				break;
			case R.id.toggle:
				readProtocol();
				break ;
			case R.id.img_showpswd:
				showPassWord();
				break;
		}
	}
	
	/***
	 * 阅读协议
	 */
	boolean isRead = true ;
	private void readProtocol() {
		if(isRead){
			toggleRead.setImageResource(R.drawable.img_no_read);
			isRead = false ;
		}else{
			toggleRead.setImageResource(R.drawable.img_read);
			isRead = true ;
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

	/****
	 * 认证通过现实的PopupWindow
	 */
	private PopupWindow mWindows ;
	private View showView ;
	private void showFinishDialog(){
		
		showView = LayoutInflater.from(this).inflate(R.layout.true_namefinish_popwindow, null);
		TextView kown = (TextView)showView.findViewById(R.id.kown);
		
		kown.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mWindows!=null&&mWindows.isShowing()){
					mWindows.dismiss();
					mWindows = null ;
				}
			}
		});
		
		mWindows = new PopupWindow(showView,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
		mWindows.setTouchable(true);
		mWindows.setOutsideTouchable(true);
		 // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
	    // 我觉得这里是API的一个bug
		mWindows.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mWindows.showAtLocation(showView, Gravity.BOTTOM, 0, 0);
		
	}
	
	
	
	
}

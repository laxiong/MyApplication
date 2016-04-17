package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Common.Settings;
import com.laxiong.InputMethod.LPTextField;
import com.laxiong.yitouhang.R;

public class WithdrawCashActivity extends BaseActivity implements OnClickListener{
	/***
	 * 提现
	 */
	private FrameLayout mBack ;
	private TextView mNextBtn ;
	private TextView mNotice ;
	private ImageView mToggleBtn ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdrawcash);
		initView();
		initData();
	}

	private void initData() {
		mBack.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
		mNotice.setOnClickListener(this);
		mToggleBtn.setOnClickListener(this);
	}

	private void initView() {
		mBack = (FrameLayout)findViewById(R.id.back_layout);
		mNextBtn = (TextView)findViewById(R.id.nextbtn);
		mNotice = (TextView)findViewById(R.id.notice);
		mToggleBtn = (ImageView)findViewById(R.id.img_toggle);
	}

	@Override
	public void onClick(View V) {
		switch(V.getId()){
			case R.id.back_layout:
				this.finish();				
				break;
			case R.id.nextbtn:
				payPswdMethod();
				break;
			case R.id.notice:
				startActivity(new Intent(WithdrawCashActivity.this,
						WithdrawCashNoticeActivity.class));
				break;
			case R.id.img_toggle:
				readProcotol();
				break;
		}
	}

	// 阅读协议
	private boolean isRead = false ;
	private void readProcotol(){
		if(isRead){ // 是阅读的
			mToggleBtn.setImageResource(R.drawable.img_read);
			isRead = false ;
		}else{  // 没有阅读
			mToggleBtn.setImageResource(R.drawable.img_no_read);
			isRead = true ;
		}
	}
	/***
	 * 提现的PopupWindow
	 */
	private PopupWindow mPayPswd;
	private View mPayView ;
	private void payPswdMethod(){
		
		mPayView = LayoutInflater.from(WithdrawCashActivity.this).inflate(R.layout.paypassword_popupwindow, null);
		TextView mSureBtn = (TextView)mPayView.findViewById(R.id.surebtm);
		TextView mConcelBtn = (TextView)mPayView.findViewById(R.id.concelbtn);
		ImageView concelImgs = (ImageView)mPayView.findViewById(R.id.imgs_concel);

		// TODO
		final LinearLayout mInputLayout = (LinearLayout)mPayView.findViewById(R.id.input_layout);
		LPTextField mInputPswdEd = (LPTextField)mPayView.findViewById(R.id.inputpassward);





		mSureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO 先支付
				Toast.makeText(WithdrawCashActivity.this, "提现申请已经成功提交", Toast.LENGTH_SHORT).show();

				startActivity(new Intent(WithdrawCashActivity.this,
						WithdrawCashDetailsActivity.class));

				if (mPayPswd != null && mPayPswd.isShowing()) {
					mPayPswd.dismiss();
					mPayPswd = null;
				}
			}
		});
		
		concelImgs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mPayPswd!=null&&mPayPswd.isShowing()){
					mPayPswd.dismiss();
					mPayPswd = null ;
				}
			}
		});
		
		mConcelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mPayPswd!=null&&mPayPswd.isShowing()){
					mPayPswd.dismiss();
					mPayPswd = null ;
				}
			}
		});
		
		mPayPswd = new PopupWindow(mPayView,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);

		mPayPswd.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		mPayPswd.setTouchable(true);
		mPayPswd.setOutsideTouchable(true);
		 // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
	    // 我觉得这里是API的一个bug
		mPayPswd.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mPayPswd.showAtLocation(mPayView, Gravity.BOTTOM, 0, 0);

		//TODO Bug
		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mInputLayout.getWidth(),mInputLayout.getHeight());
		final int marginLeftRight = Settings.DISPLAY_WIDTH-mInputLayout.getWidth()/2;
		final int margincenter = Settings.DISPLAY_HEIGHT-mInputLayout.getHeight()/2;
//		params.setMargins(marginLeftRight,margincenter,marginLeftRight,0);
//		mInputLayout.setLayoutParams(params);
		mInputPswdEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				if (b) {    //获取焦点 键盘弹出
//					params.setMargins(marginLeftRight, 100, marginLeftRight, 0);
//					mInputLayout.setLayoutParams(params);
				} else {  //没有获取焦点

//					params.setMargins(marginLeftRight, margincenter, marginLeftRight, 0);
//					mInputLayout.setLayoutParams(params);
				}
			}
		});


	}
	
	
}

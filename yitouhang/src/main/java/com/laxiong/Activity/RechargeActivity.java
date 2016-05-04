package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.yitouhang.R;

public class RechargeActivity extends BaseActivity implements View.OnClickListener{
	/***
	 * 充值页面
	 */
	private LinearLayout mPayMethod ;
	private FrameLayout mBack ;
	private TextView mRechargeBtn ;
	private ImageView mToggleBtn ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		initView();
	}

	private void initView(){
		mPayMethod = (LinearLayout)findViewById(R.id.change_pay_method);
		mBack= (FrameLayout)findViewById(R.id.back_layout);
		mRechargeBtn = (TextView)findViewById(R.id.rechargeBtn);
		mToggleBtn =(ImageView)findViewById(R.id.toggle_img);

		mBack.setOnClickListener(this);
		mPayMethod.setOnClickListener(this);
		mRechargeBtn.setOnClickListener(this);
		mToggleBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.change_pay_method:
				payMenthodType();
				break;
			case R.id.back_layout:
				this.finish();
				break;
			case R.id.rechargeBtn:
				inputOverToPswd();
				break;
			case R.id.toggle_img:
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

	// pay Menthod

	private PopupWindow mPayMathodWindow ;
	private View PayView ;
	private ImageView newCard_img,lateMoney_img,constranceBank_img ;
	private void payMenthodType(){

		PayView = LayoutInflater.from(RechargeActivity.this).inflate(R.layout.pay_mathod_popwindow, null);

		RelativeLayout newCard = (RelativeLayout)PayView.findViewById(R.id.addnewcard);
		RelativeLayout lateMoney = (RelativeLayout)PayView.findViewById(R.id.latemoney);
		RelativeLayout constranceBank = (RelativeLayout)PayView.findViewById(R.id.concreatebank);
		TextView mConcel = (TextView)PayView.findViewById(R.id.concel);

		newCard_img = (ImageView)PayView.findViewById(R.id.change_img_addnewcard);
		lateMoney_img = (ImageView)PayView.findViewById(R.id.change_img_latemoney);
		constranceBank_img = (ImageView)PayView.findViewById(R.id.change_img_concreatebank);

		newCard.setOnClickListener(listenner);
		lateMoney.setOnClickListener(listenner);
		constranceBank.setOnClickListener(listenner);
		mConcel.setOnClickListener(listenner);

		mPayMathodWindow = new PopupWindow(PayView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT,true);
		mPayMathodWindow.setTouchable(true);
		mPayMathodWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mPayMathodWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mPayMathodWindow.showAtLocation(PayView, Gravity.BOTTOM, 0, 0);
	}

	View.OnClickListener listenner = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.addnewcard:  // 加新卡
					newCard_img.setImageResource(R.drawable.img_read);
					lateMoney_img.setImageResource(R.drawable.img_no_read);
					constranceBank_img.setImageResource(R.drawable.img_no_read);
					break;
				case R.id.latemoney:  //  余额
					newCard_img.setImageResource(R.drawable.img_no_read);
					lateMoney_img.setImageResource(R.drawable.img_read);
					constranceBank_img.setImageResource(R.drawable.img_no_read);
					break;
				case R.id.concreatebank:  // 建设银行
					newCard_img.setImageResource(R.drawable.img_no_read);
					lateMoney_img.setImageResource(R.drawable.img_no_read);
					constranceBank_img.setImageResource(R.drawable.img_read);
					break;
				case R.id.concel:
					if(mPayMathodWindow!=null&&mPayMathodWindow.isShowing()){
						mPayMathodWindow.dismiss();
						mPayMathodWindow = null ;
					}
					break;
			}
		}
	};


	// 转入按钮  输入密码
	private PopupWindow mInputPswdWindow ;
	private View mInputView ;
	private void inputOverToPswd(){

		mInputView = LayoutInflater.from(RechargeActivity.this).inflate(R.layout.overtopswd_popwindow, null);

		ImageView comcelImags = (ImageView)mInputView.findViewById(R.id.imgs_concel);
		TextView  mForgetPswd = (TextView)mInputView.findViewById(R.id.forget_pswd);
		TextView concelBtn = (TextView)mInputView.findViewById(R.id.concel_btn);
		TextView sureBtn = (TextView)mInputView.findViewById(R.id.sure_btn);

		comcelImags.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mInputPswdWindow!=null&&mInputPswdWindow.isShowing()){
					mInputPswdWindow.dismiss();
					mInputPswdWindow = null ;
				}
			}
		});

		concelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mInputPswdWindow!=null&&mInputPswdWindow.isShowing()){
					mInputPswdWindow.dismiss();
					mInputPswdWindow = null ;
				}
			}
		});

		sureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(RechargeActivity.this,
						RechargeResultActivity.class));

				if(mInputPswdWindow!=null&&mInputPswdWindow.isShowing()){
					mInputPswdWindow.dismiss();
					mInputPswdWindow = null ;
				}

			}
		});

		mForgetPswd.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi") @Override
			public void onClick(View arg0) {
				Toast.makeText(RechargeActivity.this, "忘记密码的操作", Toast.LENGTH_SHORT).show();
			}
		});

		mInputPswdWindow = new PopupWindow(mInputView,  ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT,true);
		mInputPswdWindow.setTouchable(true);
		mInputPswdWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mInputPswdWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mInputPswdWindow.showAtLocation(mInputView, Gravity.BOTTOM, 0, 0);
	}


}

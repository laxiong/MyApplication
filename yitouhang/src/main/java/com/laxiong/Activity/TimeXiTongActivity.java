package com.laxiong.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carfriend.mistCF.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.Share_Presenter;
import com.laxiong.Mvp_view.IViewBasicObj;
import com.laxiong.Utils.DialogUtils;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.OpenAccount;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.entity.Profit;
import com.laxiong.entity.ShareInfo;
import com.laxiong.entity.User;
import com.laxiong.entity.Yesterday;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;

public class TimeXiTongActivity extends BaseActivity implements OnClickListener,IViewBasicObj<ShareInfo>
{
	 /***
	  * 时息通
	  */

	 private Share_Presenter presenter;
	private FrameLayout mBack ;
	private TextView mJiGetMoney , mShareBtn ,mScrollIn , mScrollOut;
	private EditText mJiMoney , mJiDay ;
	private TextView mYesDayProfit,mAmountProfit,mGetCashProfit,mPrecent,mRemark1,mRemark2,mLastCash,SxtTitle;
	private RelativeLayout mSafeProtect ,mMoreDetails;
	private int mId ;
	private Double lu = 0.0; // 计算器的计算利率
	private LinearLayout ll_wrap;
	private  User mUser ;
	private ImageView mSxtBack ;
	private boolean isVip ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_shixitong_layout);
		initView();
		initData();
		getNetWork();
		setAmountData();
		mUser = YiTouApplication.getInstance().getUser();
		isVip = getIntent().getBooleanExtra("isVip",false);
		if (isVip){
			setVipColor();
		}else {
			setColors();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getNetWork();
	}

	private void initData() {
		mBack.setOnClickListener(this);
		mShareBtn.setOnClickListener(this);
		mScrollIn.setOnClickListener(this);
		mScrollOut.setOnClickListener(this);
		mSafeProtect.setOnClickListener(this);
		mMoreDetails.setOnClickListener(this);

		presenter = new Share_Presenter(this);
		
		mJiDay.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String str = mJiDay.getText().toString().trim();
				if (str == null || str.equals("") || str.length() == 0) {
					Toast.makeText(TimeXiTongActivity.this, "输入整数", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
										  int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				String day = mJiDay.getText().toString().trim();
				//TODO  TextView 的计算结果显示
				if (mJiMoney != null && mJiMoney.getText().toString().trim().length() != 0 && !mJiMoney.getText().toString().trim().equals("")) {
					if (day != null && day.length() != 0 && !day.equals("")) {
						int tD = Integer.parseInt(day);
						int tM = Integer.parseInt(mJiMoney.getText().toString().trim());
						double lu = 0.072;
						// 保留小数点三位
						NumberFormat mFormat = NumberFormat.getNumberInstance();
						mFormat.setMaximumFractionDigits(3);
						String comfixNum = mFormat.format(backComfix(tM, tD, lu));
						mJiGetMoney.setText(comfixNum);
					}
				}
			}
		});
		
		mJiMoney.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String str = mJiMoney.getText().toString().trim();
				if (str == null || str.equals("") || str.length() == 0) {
					Toast.makeText(TimeXiTongActivity.this, "输入整数", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
										  int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				String money = mJiMoney.getText().toString().trim();
				//TODO  TextView 的计算结果显示
				if (mJiDay != null && mJiDay.getText().toString().trim().length() != 0 && !mJiDay.getText().toString().trim().equals("")) {
					if (money != null && money.length() != 0 && !money.equals("")) {
						int tM = Integer.parseInt(money);
						int tD = Integer.parseInt(mJiDay.getText().toString().trim());
						double lu = 0.72;
						// 保留小数点三位
						NumberFormat mFormat = NumberFormat.getNumberInstance();
						mFormat.setMaximumFractionDigits(3);
						String comfixNum = mFormat.format(backComfix(tM, tD, lu));
						mJiGetMoney.setText(comfixNum);
					}
				}
			}
		});
	}

	// setColor的 mPrecent	mYesDayProfit	mGetCashProfit	mAmountProfit
	private TextView mPrecentTitle,mYesDayTitle,mAmountTitle,mGetCashTitle,mMathPrecent;
	private View mline1,mline2 ;
	private void initView() {
		ll_wrap = (LinearLayout) findViewById(R.id.ll_wrap);
		mJiDay = (EditText)findViewById(R.id.payday);
		mJiMoney = (EditText)findViewById(R.id.paymoney);
		mJiGetMoney = (TextView)findViewById(R.id.jigetmoney);
		mBack = (FrameLayout)findViewById(R.id.backlayout);
		mShareBtn = (TextView)findViewById(R.id.share);
		mScrollIn = (TextView)findViewById(R.id.scroll_in);
		mScrollOut = (TextView)findViewById(R.id.scroll_out);

		mSafeProtect =(RelativeLayout)findViewById(R.id.safeprotect);//安全保障
		mMoreDetails = (RelativeLayout)findViewById(R.id.more_details); // 更多详情

		mRemark1 =(TextView)findViewById(R.id.remark1);
		mRemark2 =(TextView)findViewById(R.id.remark2);
		mPrecent =(TextView)findViewById(R.id.tv2);
		mYesDayProfit =(TextView)findViewById(R.id.yesdayprofit);
		mAmountProfit =(TextView)findViewById(R.id.amountprofit);
		mGetCashProfit =(TextView)findViewById(R.id.getcashprofit);
		mLastCash=(TextView)findViewById(R.id.text2);
		SxtTitle =(TextView)findViewById(R.id.sxt_title);
		mId = getIntent().getIntExtra("id",-1);

		mMathPrecent = (TextView)findViewById(R.id.math_precent);
		mYesDayTitle = (TextView)findViewById(R.id.tv3);
		mAmountTitle = (TextView)findViewById(R.id.tv4);
		mGetCashTitle = (TextView)findViewById(R.id.tv0);
		mPrecentTitle = (TextView)findViewById(R.id.tv1);
		mline1 = findViewById(R.id.line_1);
		mline2 = findViewById(R.id.line_2);
		mSxtBack = (ImageView)findViewById(R.id.sxt_back);

	}

	// 可购买金额
	private String mAmountMoney ;
	private String dates ;
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.scroll_in:  // 转入
				if (mUser!=null){
					if (mUser.getBankcount() >=1){
						startActivity(new Intent(TimeXiTongActivity.this,
								TransferInActivity.class).
								putExtra("mAmountMoney", mAmountMoney).
								putExtra("date", dates).
								putExtra("isVip",isVip));
					}else {
						OpenAccount.getInstance().goToCreateCountNum(TimeXiTongActivity.this);
					}
				}else {
					startActivity(new Intent(TimeXiTongActivity.this,LoginActivity.class));
				}

				break;
			case R.id.backlayout:
				this.finish();
				break;
			case R.id.share:
				presenter.loadShareData(this);

				break;
				
			case R.id.scroll_out:  // 转出
				if (mUser!=null){
					if (mUser.getBankcount() >=1){
						startActivity(new Intent(TimeXiTongActivity.this,
								TransferOutActivity.class));
					}else {
						OpenAccount.getInstance().goToCreateCountNum(TimeXiTongActivity.this);
					}
				}else {
					startActivity(new Intent(TimeXiTongActivity.this, LoginActivity.class));
				}

				break;
			case R.id.safeprotect :
				startActivity(new Intent(TimeXiTongActivity.this,WebViewActivity.class).
						putExtra("url", "https://licai.gongshidai.com/wap/public/ytbank/yt.safe.html").
						putExtra("title","安全保障"));
				break;
			case R.id.more_details:
				startActivity(new Intent(TimeXiTongActivity.this,WebViewActivity.class).
						putExtra("url", "https://licai.gongshidai.com/wap/public/ytbank/yt.debt.html").
						putExtra("title","更多详情"));
				break;
		}
	}
	/**
	 * 计算器的算法
	 * money:本金
	 * day：日期
	 * lu：利率  7.2%
	 */
	private double backComfix(float money,float day, double lu){
		double backMoney = money*lu*(day/365)+money;
		return backMoney ;
	}

	private void getNetWork(){
		String url=InterfaceInfo.PRODUCT_URL;
		if(mId!=-1)
			url=url+"?id="+mId;
		HttpUtil2.get(url, new Callback() {
			@Override
			public void onResponse2(JSONObject response) {
				if (response!=null){
					try {
						if (response.getInt("code")==0){
							updataUi(response);
						}else {
							Toast.makeText(TimeXiTongActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
						}
					}catch (Exception E){
					}
				}
			}
			@Override
			public void onFailure(String msg) {
				Toast.makeText(TimeXiTongActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void updataUi(JSONObject response){
		if (response!=null){
			try{
				Double apr = response.getDouble("apr");
				Double data = apr / 100;
				lu = data ;
				mPrecent.setText(String.valueOf(apr));
				mAmountMoney = String.valueOf(response.getInt("amount")) ;
				dates = response.getString("date");
				//保存日期的时间
				SharedPreferences gxb_date = getSharedPreferences("SXT_DATE", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = gxb_date.edit();
				editor.putString("sxt_date",response.getString("date"));
				editor.commit();

				mLastCash.setText(mAmountMoney);
				JSONArray ARRA = response.getJSONArray("details");
				if (ARRA.length()>0){
					mRemark1.setText(ARRA.getString(0));
					mRemark2.setText(ARRA.getString(1));
				}
				SxtTitle.setText(response.getString("title"));
			}catch (Exception E){
			}
		}
	}

	// 更新 昨日收益，累计收益，持有金额的
	private void setAmountData(){
		User AppUser = YiTouApplication.getInstance().getUser();
		if (mGetCashProfit!=null&&mAmountProfit!=null&&mYesDayProfit!=null) {
			if (AppUser != null) {
				// 活期份额
				int mCur = AppUser.getCurrent();
				mGetCashProfit.setText(String.valueOf(mCur));
				// 累计收益
				Profit mProfit = AppUser.getProfit_list();
				double mSxtProfit = mProfit.getSxt();
				mAmountProfit.setText(String.valueOf(mSxtProfit));
				// 昨日收益
				Yesterday mYesterday = AppUser.getYesterday();
				double mSxtYester = mYesterday.getSxt();
				mYesDayProfit.setText(String.valueOf(mSxtYester));
			} else {
				mGetCashProfit.setText("0.0");
				mAmountProfit.setText("0.0");
				mYesDayProfit.setText("0.0");
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void loadObjSuc(ShareInfo obj) {
		if (obj == null) {
			ToastUtil.customAlert(this, "未获取到分享数据");
		} else {
			DialogUtils.getInstance(TimeXiTongActivity.this).alertShareDialog(obj, ll_wrap);
		}
	}

	@Override
	public void loadObjFail(String msg) {
		ToastUtil.customAlert(this, msg);
	}

	//setColor的 mPrecent	mYesDayProfit	mGetCashProfit	mAmountProfit
	//mPrecentTitle,mYesDayTitle,mAmountTitle,mGetCashTitle,mMathPrecent;mline1,mline2

	// 设置Vip的颜色
	private void setVipColor(){
		mPrecent.setTextColor(Color.parseColor("#FFFFDFAA"));
		mYesDayProfit.setTextColor(Color.parseColor("#FFFFDFAA"));
		mGetCashProfit.setTextColor(Color.parseColor("#FFFFDFAA"));
		mAmountProfit.setTextColor(Color.parseColor("#FFFFDFAA"));
		mPrecentTitle.setTextColor(Color.parseColor("#FFFFDFAA"));
		mYesDayTitle.setTextColor(Color.parseColor("#FFFFDFAA"));
		mAmountTitle.setTextColor(Color.parseColor("#FFFFDFAA"));
		mGetCashTitle.setTextColor(Color.parseColor("#FFFFDFAA"));
		mMathPrecent.setTextColor(Color.parseColor("#FFFFDFAA"));
		mline1.setBackgroundColor(Color.parseColor("#FFFFDFAA"));
		mline2.setBackgroundColor(Color.parseColor("#FFFFDFAA"));
		mShareBtn.setTextColor(Color.parseColor("#FFFFDFAA"));
		SxtTitle.setTextColor(Color.parseColor("#FFFFDFAA"));
		mSxtBack.setImageResource(R.drawable.img_vip_back);
	}
	//设置一般的白色
	private void setColors(){
		mPrecent.setTextColor(Color.parseColor("#FFFFFFFF"));
		mYesDayProfit.setTextColor(Color.parseColor("#FFFFFFFF"));
		mGetCashProfit.setTextColor(Color.parseColor("#FFFFFFFF"));
		mAmountProfit.setTextColor(Color.parseColor("#FFFFFFFF"));
		mPrecentTitle.setTextColor(Color.parseColor("#FFFFFFFF"));
		mYesDayTitle.setTextColor(Color.parseColor("#FFFFFFFF"));
		mAmountTitle.setTextColor(Color.parseColor("#FFFFFFFF"));
		mGetCashTitle.setTextColor(Color.parseColor("#FFFFFFFF"));
		mMathPrecent.setTextColor(Color.parseColor("#FFFFFFFF"));
		mline1.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
		mline2.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
		mShareBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
		SxtTitle.setTextColor(Color.parseColor("#FFFFFFFF"));
		mSxtBack.setImageResource(R.drawable.img_back);
	}


}

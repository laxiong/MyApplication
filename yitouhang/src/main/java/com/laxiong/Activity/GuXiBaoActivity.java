package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.View.VerticalNumberProgressBar;
import com.laxiong.yitouhang.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;

public class GuXiBaoActivity extends BaseActivity implements OnClickListener{
	/****
	 * 固息宝
	 */
	private RelativeLayout mLayout_progressbar ;
	private TextView mProgressNum ,mShareBtn , mBuyBtn;
	private VerticalNumberProgressBar mProgressBar ;
	private FrameLayout mBack ;
	private ImageView mJiSuanQi ;
	private int mId;
	// 百分比 等加载的内容
	private TextView mPrecent ,mAddPrecent,mRemark1,mRemark2,mLastEran,mAddOther,mGxbTitle,mYdProfit,mGetCash;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_guxibao);
		initView();
		initData();
		getNetWork();
	}
	private void initData() {
		mShareBtn.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mJiSuanQi.setOnClickListener(this);
		mBuyBtn.setOnClickListener(this);

		mId = getIntent().getIntExtra("id",-1);
		Log.i("GXB","获取的Id："+mId);


	}
	private void initView() {
		mLayout_progressbar = (RelativeLayout)findViewById(R.id.progressbar_layout);
		mProgressNum = (TextView)findViewById(R.id.numText);
		mProgressBar =(VerticalNumberProgressBar)findViewById(R.id.numberbar);
		mJiSuanQi = (ImageView)findViewById(R.id.jisuanqi);
		mBuyBtn = (TextView)findViewById(R.id.buying);
		
		mBack = (FrameLayout)findViewById(R.id.backlayout);
		mShareBtn = (TextView)findViewById(R.id.share);


		mPrecent =(TextView)findViewById(R.id.tv2);
		mAddPrecent =(TextView)findViewById(R.id.addprecent);
		mGetCash =(TextView)findViewById(R.id.getcash);
		mYdProfit =(TextView)findViewById(R.id.yesterdayprofit);
		mRemark1 =(TextView)findViewById(R.id.remark1);
		mRemark2 =(TextView)findViewById(R.id.remark2);
		mLastEran =(TextView)findViewById(R.id.text1);
		mAddOther =(TextView)findViewById(R.id.add_profit);
		mGxbTitle =(TextView)findViewById(R.id.gxb_title);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.share:
				Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
				break;
			case R.id.backlayout:
				this.finish();
				break;
			case R.id.jisuanqi:
				showJiSuanQi();
				break;
			case R.id.buying:
				startActivity(new Intent(GuXiBaoActivity.this,
						BuyingActivity.class));
				break;
		}
	}
	
	// set progress textview height
	private void setProgressNumHeight(float f){
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		
        mLayout_progressbar.measure(widthMeasureSpec, heightMeasureSpec);
        mProgressNum.measure(widthMeasureSpec, heightMeasureSpec);
        
        mProgressBar.setProgress(f);
        int layoutHeight = mLayout_progressbar.getMeasuredHeight();
        int progressTextHeight = mProgressNum.getMeasuredHeight();
        
        // 设置进度的运行高度大于  TextView本身默认的高度时
        if(((mProgressBar.getProgress() / 100 )*layoutHeight) > progressTextHeight){
        	 LayoutParams tvlp =  mProgressNum.getLayoutParams();
        	 tvlp.height = (int)((mProgressBar.getProgress() / 100 )*layoutHeight);
        	 mProgressNum.setLayoutParams(tvlp);
        }
        mProgressNum.setText(mProgressBar.getProgress()+"%");
	}
	
	/**显示计算器的PopupWindow*/
	private PopupWindow mPopWindJi ;
	private View mJiSuanView ;
	private void showJiSuanQi(){
		mJiSuanView = LayoutInflater.from(this).inflate(R.layout.jisuanqi_popwindow, null);
		final EditText mMoney = (EditText)mJiSuanView.findViewById(R.id.toumoney);
		final EditText mDays = (EditText)mJiSuanView.findViewById(R.id.touday);
		final TextView mComfix = (TextView)mJiSuanView.findViewById(R.id.comfit);
		ImageView imgConcel = (ImageView)mJiSuanView.findViewById(R.id.imgs_concel);
		// 投资金额
		mMoney.addTextChangedListener(new TextWatcher(){   
			@Override
			public void afterTextChanged(Editable arg0) {
				//TODO 假设是20000
				String str = mMoney.getText().toString().trim();
					//TODO  TextView 的计算结果显示
					if(mDays!=null&&!mDays.equals("")&&mDays.length()!=0){
						int tM = Integer.parseInt(str);
						int tD = Integer.parseInt(mDays.getText().toString().trim());
						double lu = 0.72 ;
						// 保留小数点三位
						NumberFormat mFormat = NumberFormat.getNumberInstance(); 
						mFormat.setMaximumFractionDigits(3);
						String comfixNum = mFormat.format(backComfix(tM,tD,lu));
						
						mComfix.setText(comfixNum);
					}
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				String str = mMoney.getText().toString().trim();
				try{
					if(str!= null)
					Integer.parseInt(str);
				}catch(Exception e){
					Toast.makeText(GuXiBaoActivity.this, "输入整数", Toast.LENGTH_SHORT).show();
				}
			}
		});
		// 投资天数
		mDays.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String str = mDays.getText().toString().trim();
				try{
					if(str!= null)
					Integer.parseInt(str);
				}catch(Exception e){
					Toast.makeText(GuXiBaoActivity.this, "输入整数", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				String str = mDays.getText().toString().trim();
					//TODO  TextView 的计算结果显示
					if(mMoney!=null&&!mMoney.equals("")&&mMoney.length()!=0){
						int tD = Integer.parseInt(str);
						int tM = Integer.parseInt(mMoney.getText().toString().trim());
						double lu = 0.072 ;
						// 保留小数点三位
						NumberFormat mFormat = NumberFormat.getNumberInstance(); 
						mFormat.setMaximumFractionDigits(3);
						String comfixNum = mFormat.format(backComfix(tM,tD,lu));
						
						mComfix.setText(comfixNum);
					}
			}
		});
		// 取消按钮
		imgConcel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mPopWindJi!=null&&mPopWindJi.isShowing()){
					mPopWindJi.dismiss();
					mPopWindJi = null ;
				}
			}
		});
		mPopWindJi = new PopupWindow(mJiSuanView,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
		mPopWindJi.setTouchable(true);
		mPopWindJi.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
	    // 我觉得这里是API的一个bug
		mPopWindJi.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mPopWindJi.showAtLocation(mJiSuanView, Gravity.BOTTOM, 0, 0);
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

	// 设置数据
	private void getNetWork(){
		RequestParams params = new RequestParams();
		if (mId!=-1)
			params.put("id",mId);
		HttpUtil.get(InterfaceInfo.PRODUCT_URL,params,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response!=null){
					try {
						if (response.getInt("code")==0){
							Log.i("WK","所有的对象："+response);
							Log.i("WK","Title的值是："+response.getString("title"));
							updataUi(response);
							double percent = response.getDouble("percent");
							setProgressNumHeight((float)percent);
						}else {
							Toast.makeText(GuXiBaoActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
						}
					}catch (Exception E){
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(GuXiBaoActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void updataUi(JSONObject response){
		if (response!=null){
			try{
				Log.i("WK","APR的值是："+response.getDouble("apr"));
				mPrecent.setText(String.valueOf(response.getDouble("apr")));
				if (response.getInt("bird")==0) { // 不是新手
					if(response.getDouble("vip")==0.0){ //不是vip
						mAddPrecent.setText("+"+String.valueOf(response.getDouble("present"))+"%");
					}else {
						mAddPrecent.setText("+"+String.valueOf(response.getDouble("accum"))+"%");
						mAddOther.setText("+"+String.valueOf(response.getDouble("vip"))+"%");
					}
				}else {
					mAddPrecent.setText("+"+String.valueOf(response.getDouble("birdapr"))+"%");
					mAddOther.setText("+"+String.valueOf(response.getDouble("birdapr"))+"%");
				}
				mLastEran.setText(String.valueOf(response.getInt("members")));


				JSONArray details = response.getJSONArray("details");
				if (details.length()>0){
					mRemark1.setText(details.getString(0));
					mRemark2.setText(details.getString(1));
				}
				mGxbTitle.setText(response.getString("title"));

			}catch (Exception E){
			}
		}
	}



	
}

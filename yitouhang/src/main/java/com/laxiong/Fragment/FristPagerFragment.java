package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Activity.GuXiBaoActivity;
import com.laxiong.Activity.TimeXiTongActivity;
import com.laxiong.Activity.WebViewActivity;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.ScollPagerUtils;
import com.laxiong.View.ChildViewPager;
import com.laxiong.entity.Branner;
import com.laxiong.yitouhang.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi") 
public class FristPagerFragment extends Fragment implements OnClickListener{
	/****
	 * 首页的碎片
	 */
	private View FristView = null ;
	private ChildViewPager mChildViewPager = null ;
	private LinearLayout mLinearDot = null ;
	private Context mContext = null ;
	
	private RelativeLayout mNewBiao , mGuXiBao , mTimeXiTong ;  // 新手标  固息宝  时息通
	private TextView mNew ,mNew_tv ,mSxt,mSxt_tv,mGxb,mGxb_tv,mAmount,mSolid,mTouTiao,mCental;
	private String mCentalUrl,mAdTitle;
	private int mGxbId,mSxtId,mNewbId ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		FristView = inflater.inflate(R.layout.fristpager_layout, null);
		mContext = getActivity() ;
		initView();
		initData();
		setListen();
		return FristView;
	}

	private void initData() {

		getBanner();
		getFristPagerData();
	}

	private void initView() {
		
		mChildViewPager = (ChildViewPager) FristView.findViewById(R.id.childviewpager);
		mLinearDot = (LinearLayout)FristView.findViewById(R.id.layout_dot);
		mNewBiao = (RelativeLayout)FristView.findViewById(R.id.new_biao);
		mGuXiBao = (RelativeLayout)FristView.findViewById(R.id.gu_xibao);
		mTimeXiTong = (RelativeLayout)FristView.findViewById(R.id.time_xitong);
		
		// set Height 
//		mChildViewPager.getLayoutParams().height = Settings.DISPLAY_WIDTH * 350 / 750 ;
		mNew =(TextView)FristView.findViewById(R.id.tv1);
		mNew_tv =(TextView)FristView.findViewById(R.id.new_biao_tv);
		mSxt =(TextView)FristView.findViewById(R.id.tv2);
		mSxt_tv =(TextView)FristView.findViewById(R.id.sxt_tv);
		mGxb =(TextView)FristView.findViewById(R.id.tv3);
		mGxb_tv =(TextView)FristView.findViewById(R.id.gxb_tv);
		mAmount = (TextView)FristView.findViewById(R.id.amount);
		mSolid =(TextView)FristView.findViewById(R.id.solid);
		mTouTiao = (TextView)FristView.findViewById(R.id.toutiao);
		mCental =(TextView)FristView.findViewById(R.id.cental);

	}

	private void setListen(){
		mNewBiao.setOnClickListener(this);
		mGuXiBao.setOnClickListener(this);
		mTimeXiTong.setOnClickListener(this);
		mCental.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.new_biao:	//
				startActivity(new Intent(getActivity(),
						GuXiBaoActivity.class).putExtra("id",mNewbId));

				break;

			case R.id.gu_xibao:
					startActivity(new Intent(getActivity(),
							GuXiBaoActivity.class).putExtra("id",mGxbId));
				break;

			case R.id.time_xitong:
					startActivity(new Intent(getActivity(),
							TimeXiTongActivity.class).putExtra("id",mSxtId));
				break;

			case R.id.cental:
				if(mCentalUrl!=null&&mAdTitle!=null){
					startActivity(new Intent(getActivity(),
							WebViewActivity.class).putExtra("url",mCentalUrl).putExtra("title",mAdTitle));
				}
				break;
		}
	}
	// Banner轮播图
	private void getBanner(){
		HttpUtil.get(InterfaceInfo.BASE_URL+"/banner",new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response!=null){
					try {
						if (response.getInt("code")==0){
							JSONArray arra = response.getJSONArray("list");
							ArrayList<Branner> banArra = getBannerData(arra);

							//TODO 开始适配器的操作
							ScollPagerUtils mScollPagerUtils = new ScollPagerUtils(banArra,mContext,mChildViewPager,mLinearDot);
							mScollPagerUtils.startPlayPic();

						}else {
							Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
						}
					}catch (Exception E){
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(getActivity(),"获取Banner页失败",Toast.LENGTH_SHORT).show();
			}
		});
	}

	private ArrayList<Branner> getBannerData(JSONArray array){
		try{
			ArrayList<Branner> banArray = new ArrayList<Branner>();
			if (array!=null){
				for (int i=0;i<array.length();i++){
					JSONObject obj = array.getJSONObject(i);
					Branner ban = new Branner();
					ban.setImageurl(obj.getString("imageurl"));
					ban.setHref(obj.getString("href"));
					ban.setTitle(obj.getString("title"));
					banArray.add(ban);
				}
				return  banArray;
			}
		}catch (Exception E){
		}
		return  null;
	}

	//首页的数据
	private void getFristPagerData(){
		HttpUtil.get(InterfaceInfo.BASE_URL+"/rental",new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response!=null) {
					try {
						if (response.getInt("code")==0){
							JSONArray ARRA = response.getJSONArray("list");
							setPagerData(ARRA);
							int solid = response.getInt("solid");
							double amount = response.getDouble("amount");
							mAmount.setText(String.valueOf(amount));
							mSolid.setText("已经有" + String.valueOf(solid) + "位聪明伙伴在壹投行理财(元)");
							JSONObject AD = response.getJSONObject("ad");
							mAdTitle = AD.getString("title");
							mTouTiao.setText(mAdTitle);
							mCentalUrl = AD.getString("url");

						}else {
							Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
						}
					} catch (Exception E) {
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(getActivity(),"首页数据获取失败",Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setPagerData(JSONArray ARRA){
		try{
			if (ARRA!=null){
				if (ARRA.length()>0) {
					JSONObject xinobj = ARRA.getJSONObject(0);
					mNew.setText(xinobj.getString("name"));
					mNew_tv.setText(xinobj.getString("title"));
					mNewbId = xinobj.getInt("product");

					JSONObject sxtobj = ARRA.getJSONObject(1);
					mSxt.setText(sxtobj.getString("name"));
					mSxt_tv.setText(sxtobj.getString("title"));
					mSxtId = sxtobj.getInt("product");

					JSONObject gxbobj = ARRA.getJSONObject(2);
					mGxb.setText(gxbobj.getString("name"));
					mGxb_tv.setText(gxbobj.getString("title"));
					mGxbId = gxbobj.getInt("product");
				}
			}
		}catch (Exception E){
		}
	}


}

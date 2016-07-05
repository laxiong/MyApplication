package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongshidai.mistGSD.R;
import com.laxiong.Activity.GuXiBaoActivity;
import com.laxiong.Activity.TimeXiTongActivity;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Json.FinanceJsonBean;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.LoadUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CircleProgressBar;
import com.laxiong.View.FinancingListView;
import com.laxiong.View.PrecentCricleBar;
import com.laxiong.View.WaitPgView;
import com.laxiong.entity.FinanceInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi") 
public class VipFinancingFragment extends Fragment implements View.OnClickListener{
	/****
	 * VIP理财的碎片
	 */
	private View mVipView ;
	private FinanceJsonBean  mFinanBean = null;
	private ImageView mConcel_img ;
	private LinearLayout mFinancelMessage ; // 提示消息

	private FinancingListView mListView ;
	private  List<FinanceInfo>  mList = new ArrayList<FinanceInfo>() ;// 全是固息宝的
	private int listNum ;  // 刷新加载更多所有的个数
	private WaitPgView wp;
	private static final String NO_SALE="#FFD6D6D6";

	private Dialog dialog ;

	private Handler handler = new Handler() {
	      @Override
	      public void handleMessage(Message msg) {
	           super.handleMessage(msg);
	           switch (msg.what){
				   case 1:
					   mListView.completeRefresh();
					   break;
				   case 2:
					   mListView.completeRefresh();
					   break;
	           }
	       }
	  };
	public void showLoadView(boolean flag) {
		wp = (WaitPgView)mVipView.findViewById(R.id.wp_load);
		wp.setVisibility(flag ? View.VISIBLE : View.GONE);
//		if (flag){
//			dialog.show();
//		}else {
//			if (dialog!=null&&dialog.isShowing()){
//				dialog.dismiss();
//				dialog = null ;
//			}
//		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mVipView = inflater.inflate(R.layout.financing_layout, null);
		initView();
		return  mVipView;
	}
	private void initView() {
		dialog = LoadUtils.createbuildDialog(getActivity(), "正在加载...");

		mConcel_img = (ImageView)mVipView.findViewById(R.id.concel_img);
		mFinancelMessage = (LinearLayout)mVipView.findViewById(R.id.finance_message);
		mConcel_img.setOnClickListener(this);
		mFinancelMessage.setVisibility(View.GONE);

		mListView = (FinancingListView)mVipView.findViewById(R.id.Listview);
		showLoadView(true);

		getVipProductInfo();
		mListView.setOnRefreshListener(mRefresh);
	}

	FinancingListView.OnRefreshListener mRefresh = new FinancingListView.OnRefreshListener(){
		// 刷新
		@Override
		public void onPullRefresh() {
			requestDataFromServer(true);
			pager = 1;
			if (mList!=null&&mFinanBean!=null) {
				mList.removeAll(mFinanBean.getGxb());
				mList.remove(mFinanBean.getSxt());
			}
			getVipProductInfo();
		}
		//加载更多
		@Override
		public void onLoadingMore() {
			requestDataFromServer(false);
			if (listNum >= mList.size()+1) {
				pager = pager + 1;
				getVipProductInfo();
			}else {
				Toast.makeText(getActivity(),"全部加载完毕",Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void requestDataFromServer(final boolean isLoading){
		new Thread(){
			public void run() {
				SystemClock.sleep(3000);
				Message msg = Message.obtain();
				if(isLoading){
					msg.what = 1;
				}else{
					msg.what = 2;
				}
				//更新UI
				handler.sendMessage(msg);
			};
		}.start();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.concel_img:
				if(mFinancelMessage!=null)
					mFinancelMessage.setVisibility(View.GONE);
				break;
		}
	}


	class VipAdapter extends BaseAdapter{
		private int currentType;  //当前item类型
		private static final int TYPE_COUNT = 2;//item类型的总数
		private static final int TYPE_Title = 0;// 标题
		private static final int TYPE_Content = 1;//内容
		@Override
		public int getCount() {
			if(mFinanBean!=null){
				int count = 3+mList.size();
				return count ;
			}
			return 0;
		}
		@Override
		public Object getItem(int i) {
			return null;
		}
		@Override
		public long getItemId(int i) {
			return 0;
		}
		@Override
		public View getView(final int i, View view, ViewGroup viewGroup) {
			ViewHonder mViewHonder = null ;
			currentType =getItemViewType(i);
			if(view == null){
				mViewHonder = new ViewHonder();
				mViewHonder.iv = new ImageView(getActivity());
				switch (currentType){
					case TYPE_Title:
						view = LayoutInflater.from(getActivity()).inflate(R.layout.item_section,null);
						mViewHonder.mText_section = (TextView)view.findViewById(R.id.section);
						mViewHonder.line=view.findViewById(R.id.line);

						break;
					case  TYPE_Content:
						view = LayoutInflater.from(getActivity()).inflate(R.layout.item_items,null);
						mViewHonder.baifenbi = (TextView)view.findViewById(R.id.baifenbi);
						mViewHonder.mInterge=(TextView)view.findViewById(R.id.tv1);
						mViewHonder.mCircleProgressView = (CircleProgressBar)view.findViewById(R.id.cricleprogress);
						mViewHonder.mPoint = (TextView)view.findViewById(R.id.xiaoshu);
						mViewHonder.vip_addbf = (TextView)view.findViewById(R.id.vip_baifenbi);

						mViewHonder.profit_tv = (TextView)view.findViewById(R.id.profit_tv);
						mViewHonder.year_hua = (TextView)view.findViewById(R.id.year_hua);
						mViewHonder.mProject = (TextView)view.findViewById(R.id.rel_tv1);
						mViewHonder.showRel = (RelativeLayout)view.findViewById(R.id.rel_tv2);

						mViewHonder.mNewPerson = (RelativeLayout)view.findViewById(R.id.new_person);
						mViewHonder.rl_days =(RelativeLayout)view.findViewById(R.id.days);
						mViewHonder.mLimitDay = (TextView)view.findViewById(R.id.limit_day);

						mViewHonder.mEnought=(PrecentCricleBar)view.findViewById(R.id.precent_enough);
						mViewHonder.Tv_limitMoney = (TextView)view.findViewById(R.id.tv_viplimit_money);
						break;
				}
				view.setTag(mViewHonder);
			}else {
				mViewHonder = (ViewHonder)view.getTag();
			}

			if(currentType==TYPE_Title){
				if(i==0){
					if(mViewHonder.mText_section!=null) {
						mViewHonder.mText_section.setText("时息通");
						mViewHonder.mText_section.setTextColor(Color.parseColor("#FF785603"));
					}
				}else if(i==2){
					if(mViewHonder.mText_section!=null) {
						mViewHonder.mText_section.setText("固息宝");
						mViewHonder.mText_section.setTextColor(Color.parseColor("#FF785603"));
					}
				}
				if(mViewHonder.line!=null)
					mViewHonder.line.setBackgroundColor(Color.parseColor("#FFE2A42A"));
			}
			if(currentType == TYPE_Content){
				if(i==1){  // 时息通
					final FinanceInfo sxt = mFinanBean.getSxt();
					if(mViewHonder.mInterge!=null&&mViewHonder.mPoint!=null&&sxt!=null) {
						mViewHonder.mInterge.setTextColor(Color.parseColor("#FF785603"));
						mViewHonder.mPoint.setTextColor(Color.parseColor("#FF785603"));
						double apr = sxt.getApr();
						if(isInterge(apr)){
							String aprStr = String.valueOf(apr);
							String[] arr = aprStr.split("[.]");
							String zhengshu = arr[0];
							mViewHonder.mInterge.setText(zhengshu);
							mViewHonder.mPoint.setVisibility(View.GONE);
						}else{
							mViewHonder.mPoint.setVisibility(View.VISIBLE);
							String aprStr = String.valueOf(apr);
							String[] arr = aprStr.split("[.]");
							String zhengshu = arr[0];
							mViewHonder.mInterge.setText(zhengshu);
							String xiaoshu = arr[1];
							mViewHonder.mPoint.setText("."+xiaoshu);
						}
					}
					if(mViewHonder.mCircleProgressView!=null&&mViewHonder.mEnought!=null&&sxt!=null){
							if (sxt.getPercent()==100.0){
								mViewHonder.mCircleProgressView.setVisibility(View.INVISIBLE);
								mViewHonder.mEnought.setVisibility(View.VISIBLE);
							}else {
								mViewHonder.mCircleProgressView.setVisibility(View.VISIBLE);
								mViewHonder.mEnought.setVisibility(View.INVISIBLE);
							}
						mViewHonder.mEnought.setPaintColor(NO_SALE);
						mViewHonder.mCircleProgressView.setPaintColor("#FFE2A42A");
						mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(sxt.getPercent())),
								mViewHonder.iv);
					}

					if(mViewHonder.vip_addbf!=null&&sxt!=null){
						if (sxt.getVip()==0.0){
							mViewHonder.vip_addbf.setVisibility(View.INVISIBLE);
						}else {
							mViewHonder.vip_addbf.setVisibility(View.VISIBLE);
							mViewHonder.vip_addbf.setText("+"+String.valueOf(sxt.getVip())+"%");
							mViewHonder.vip_addbf.setTextColor(Color.parseColor("#FF785603"));
						}
					}

					// 新手标 //日期
					if (mViewHonder.rl_days!=null)
						mViewHonder.rl_days.setVisibility(View.GONE);

					final int special =sxt.getSpecials();
					if (mViewHonder.mNewPerson!=null&&mViewHonder.Tv_limitMoney!=null) {
						if (special!=0){
							mViewHonder.mNewPerson.setVisibility(View.VISIBLE);
							mViewHonder.Tv_limitMoney.setText("最高持有金额"+special/10000+"万");
						}else {
							mViewHonder.mNewPerson.setVisibility(View.GONE);
						}
					}

					if(mViewHonder.profit_tv!=null&&sxt!=null&&mViewHonder.baifenbi!=null&&mViewHonder.mProject!=null&&
							mViewHonder.year_hua!=null) {
						mViewHonder.profit_tv.setTextColor(Color.parseColor("#FFE2A42A"));
						mViewHonder.profit_tv.setText(sxt.getPaytype());
						mViewHonder.baifenbi.setTextColor(Color.parseColor("#FF785603"));
						mViewHonder.mProject.setTextColor(Color.parseColor("#FF785603"));
						mViewHonder.mProject.setText(sxt.getTitle());
						mViewHonder.year_hua.setTextColor(Color.parseColor("#FFE2A42A"));
					}

					if(view!=null&&sxt!=null)
						view.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {

								SharedPreferences sfsxt = getActivity().getSharedPreferences("SXT_ID", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = sfsxt.edit();
								editor.putInt("sxt_id",sxt.getId());
								editor.commit();

								getActivity().startActivity(new Intent(getActivity(),
										TimeXiTongActivity.class).
										putExtra("id", sxt.getId()).
										putExtra("isVip", true));
							}
						});


				}else if (i>=3){ // 固息宝
					final FinanceInfo gxb = mList.get(i - 3);
					gxbOperate(mViewHonder, gxb, view);
				}
			}
			return view;
		}
		@Override
		public int getItemViewType(final int position) {
			// 时息通
			if(position==0||position==2){
				return TYPE_Title ;
			}else{
				return TYPE_Content ;
			}
		}
		@Override
		public int getViewTypeCount() {
			return TYPE_COUNT;
		}
	}

	// 固息宝的
	private void gxbOperate(ViewHonder mViewHonder,final FinanceInfo gxb,View view){

		if(mViewHonder.mInterge!=null&&mViewHonder.mPoint!=null) {
			mViewHonder.mInterge.setTextColor(Color.parseColor("#FF785603"));
			mViewHonder.mPoint.setTextColor(Color.parseColor("#FF785603"));
			double apr = gxb.getApr();
			if(isInterge(apr)){
				String aprStr = String.valueOf(apr);
				String[] arr = aprStr.split("[.]");
				String zhengshu = arr[0];
				mViewHonder.mInterge.setText(zhengshu);
				mViewHonder.mPoint.setVisibility(View.GONE);
			}else{
				mViewHonder.mPoint.setVisibility(View.VISIBLE);
				String aprStr = String.valueOf(apr);
				String[] arr = aprStr.split("[.]");
				String zhengshu = arr[0];
				mViewHonder.mInterge.setText(zhengshu);
				String xiaoshu = arr[1];
				mViewHonder.mPoint.setText("."+xiaoshu);
			}
		}
		if(mViewHonder.mCircleProgressView!=null&&mViewHonder.mEnought !=null){
			if (gxb.getPercent()==100.0){
				mViewHonder.mCircleProgressView.setVisibility(View.INVISIBLE);
				mViewHonder.mEnought.setVisibility(View.VISIBLE);
			}else {
				mViewHonder.mCircleProgressView.setVisibility(View.VISIBLE);
				mViewHonder.mEnought.setVisibility(View.INVISIBLE);
			}
			mViewHonder.mEnought.setPaintColor(NO_SALE);
			mViewHonder.mCircleProgressView.setPaintColor("#FFE2A42A");
			mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(gxb.getPercent())),
					mViewHonder.iv);
		}
//
		if(mViewHonder.vip_addbf!=null){
			if (gxb.getVip()==0.0){
				mViewHonder.vip_addbf.setVisibility(View.INVISIBLE);
			}else {
				mViewHonder.vip_addbf.setVisibility(View.VISIBLE);
				mViewHonder.vip_addbf.setText("+"+String.valueOf(gxb.getAccum())+"%");
				mViewHonder.vip_addbf.setTextColor(Color.parseColor("#FF785603"));
			}
		}

		//最低可投金额
		final int special = gxb.getSpecials();
		if(mViewHonder.mNewPerson!=null&&mViewHonder.Tv_limitMoney!=null){
			if (special!=0){
				mViewHonder.mNewPerson.setVisibility(View.VISIBLE);
				mViewHonder.Tv_limitMoney.setText(special/10000+"万");
			}else {
				mViewHonder.mNewPerson.setVisibility(View.INVISIBLE);
			}
		}
		//日期
		final int limit = gxb.getLimit();
		if(mViewHonder.mLimitDay!=null){
			String limit_day = String.valueOf(limit);
			String[] day = limit_day.split("[.]");
			mViewHonder.rl_days.setVisibility(View.VISIBLE);
			mViewHonder.mLimitDay.setText(day[0]+"天");
		}else {
			mViewHonder.rl_days.setVisibility(View.GONE);
		}

		if(mViewHonder.profit_tv!=null)
			mViewHonder.profit_tv.setTextColor(Color.parseColor("#FFE2A42A"));
		mViewHonder.profit_tv.setText(gxb.getPaytype());
		if(mViewHonder.baifenbi!=null)
			mViewHonder.baifenbi.setTextColor(Color.parseColor("#FF785603"));
		if(mViewHonder.mProject!=null)
			mViewHonder.mProject.setTextColor(Color.parseColor("#FF785603"));
		mViewHonder.mProject.setText(gxb.getTitle());
		if(mViewHonder.year_hua!=null)
			mViewHonder.year_hua.setTextColor(Color.parseColor("#FFE2A42A"));

		if(view!=null)
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (gxb.getPercent()!=100.0) {  // 如果是售罄就无法点击进入到详情页面
						getActivity().startActivity(new Intent(getActivity(),
								GuXiBaoActivity.class).
								putExtra("id", gxb.getId()).
								putExtra("ttnum", listNum).
								putExtra("limitday", limit).
								putExtra("isVip", true).
								putExtra("bird",-1).
								putExtra("viplimitmoney",special));
					}
				}
			});
	}
	/**
	 * ViewHonder的类
	 */
	class ViewHonder {
		TextView mText_section;
		CircleProgressBar mCircleProgressView;
		PrecentCricleBar mEnought ;  // 100%售完的显示图片
		TextView baifenbi;
		TextView mInterge;
		TextView mPoint;
		ImageView iv; //放cricleprogressbar的节点
		View line ;
		TextView vip_addbf ;

		TextView mProject ;
		TextView year_hua ;
		TextView profit_tv ;

		RelativeLayout showRel ;
		RelativeLayout rl_days ;
		RelativeLayout mNewPerson ; //新手标
		TextView mLimitDay ; // 日期
		TextView Tv_limitMoney;
	}

	//判断是否为整数
	private boolean isInterge(double num){
		if(num%1==0){
			return  true;
		}else{
			return false;
		}
	}

	// 获取VIP产品的内容
	private int pager = 1;
	private void getVipProductInfo(){
		HttpUtil2.get(InterfaceInfo.PRODUCT_URL + "?p=" + pager + "&limit=10"+"&type=vip", new Callback() {
			@Override
			public void onResponse2(JSONObject response) {
				showLoadView(false);
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							// 时息通
							if (pager == 1) {
								mFinanBean = new FinanceJsonBean();
								JSONObject sxt = response.getJSONObject("sxt");
								FinanceInfo sxtInfo = setProductInfo(sxt);
								mFinanBean.setSxt(sxtInfo);
							}
							// 固息宝
							JSONArray gxb =  response.getJSONArray("gxb");
							if(gxb.length()>0){
								for (int i=0;i<gxb.length();i++){
									JSONObject gxb_obj = gxb.getJSONObject(i);
									FinanceInfo gxbInfo = setProductInfo(gxb_obj);
									double brid = gxbInfo.getBird();
									if (brid!=1){
										mList.add(gxbInfo);
									}
								}
								mFinanBean.setGxb(mList);
							}
							mFinanBean.setMsg(response.getString("msg"));
							mFinanBean.setTime(response.getString("time"));
							listNum = response.getInt("ttnum");

							if(pager == 1){
								mListView.setAdapter(new VipAdapter());
							}else {
								mListView.invalidate();
							}

						} else {
							ToastUtil.customAlert(getActivity(), response.getString("msg"));
						}
					} catch (Exception e) {
					}
				}
			}
			@Override
			public void onFailure(String msg) {
				showLoadView(false);
				ToastUtil.customAlert(getActivity(), "网络访问失败");
			}
		});
	}

	// 设置产品的信息
	private FinanceInfo  setProductInfo(JSONObject obj){
		if(obj!=null){
			try {
				FinanceInfo info = new FinanceInfo();
				info.setId(obj.getInt("id"));
				info.setAmount(obj.getInt("amount"));
				info.setApr(obj.getDouble("apr"));
				info.setBird(obj.getInt("bird"));
				info.setBirdapr(obj.getDouble("birdapr"));
				info.setContent(obj.getString("content"));
				info.setDate(obj.getString("date"));
				info.setKey(obj.getString("key"));
				info.setLimit(obj.getInt("limit"));
				info.setMax(obj.getInt("max"));
				info.setTitle(obj.getString("title"));
				info.setType(obj.getInt("type"));
				info.setMembers(obj.getInt("members"));
				info.setPaytype(obj.getString("paytype"));
				info.setPercent(obj.getDouble("percent"));
				info.setPresent(obj.getDouble("present"));
				info.setAccum(obj.getDouble("accum"));
				info.setRemark(obj.getString("remark"));
				info.setRule(obj.getString("rule"));
				info.setMin(obj.getString("min"));
				info.setScore(obj.getInt("score"));
				info.setVip(obj.getDouble("vip"));
				info.setUrl(obj.getString("url"));
				info.setTotal_amount(obj.getInt("total_amount"));
				info.setTotal_menber(obj.getInt("total_menber"));
				info.setSpecial(obj.getInt("specials"));
				return info;
			}catch (Exception e){
				e.getStackTrace();
			}
		}else {
			Toast.makeText(getActivity(),"FinanceInfo为空",Toast.LENGTH_SHORT).show();
		}
		return null ;
	}

	// 用于隐藏天数
	private boolean hideTextDay(double day){
		if (day==0.0){
			return true ;
		}
		return false ;
	}
	// 用于隐藏 新手标
	private boolean hideTextPerson(double strs){
		if (strs==1){
			return false ;
		}
		return true ;
	}

}

package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Activity.GuXiBaoActivity;
import com.laxiong.Activity.TimeXiTongActivity;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Json.FinanceJsonBean;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.View.CircleProgressBar;
import com.laxiong.View.FinancingListView;
import com.laxiong.entity.FinanceInfo;
import com.laxiong.yitouhang.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi") 
public class FinancingFragment extends Fragment implements OnClickListener{
	/****
	 * 理财的碎片
	 */
	
	private View view ;
	private ImageView mConcel_img ;
	private LinearLayout mFinancelMessage ; // 提示消息
	
//	private PinnedSectionListView mPinnedSectionListView ;
//	private PinnedListViewAdapter adapter ;

	private FinancingListView mListView ;
	
	private Handler handler = new Handler() {
	      @Override
	      public void handleMessage(Message msg) {
	           super.handleMessage(msg);
	           switch (msg.what){
	               case 1:
	            	    Toast.makeText(getActivity(), "完成刷新", Toast.LENGTH_SHORT).show();
//	            	    mPinnedSectionListView.completeRefresh();
					   	mListView.completeRefresh();
	                    break;
	               case 2:
	            	    Toast.makeText(getActivity(), "完成加载更多", Toast.LENGTH_SHORT).show();
//	            	    mPinnedSectionListView.completeRefresh();
					   	mListView.completeRefresh();
	                    break;
	           }
	       }
	  };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.financing_layout, null);
		initView();
		return view;
	}

	private void initView() {
		//TODO 做PinnedSection的操作....
//		mPinnedSectionListView = (PinnedSectionListView)view.findViewById(R.id.sectionListview);
		mConcel_img = (ImageView)view.findViewById(R.id.concel_img);
		mFinancelMessage = (LinearLayout)view.findViewById(R.id.finance_message);
		mConcel_img.setOnClickListener(this);
//
//		adapter = new PinnedListViewAdapter(getActivity(),Bean.getData());
//		mPinnedSectionListView.setAdapter(adapter);
//		mPinnedSectionListView.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onPullRefresh() {
//				//请求数据，更新数据
//				requestDataFromServer(true);
//				Toast.makeText(getActivity(), "正在刷新", 2).show();
//			}
//			@Override
//			public void onLoadingMore() {
//				requestDataFromServer(false);
//				Toast.makeText(getActivity(), "正在加载更多", 2).show();
//			}
//		});

		mListView = (FinancingListView)view.findViewById(R.id.Listview);
		getProductInfo();
		mListView.setOnRefreshListener(new FinancingListView.OnRefreshListener() {
			@Override
			public void onPullRefresh() {
				//请求数据，更新数据
				requestDataFromServer(true);
				Toast.makeText(getActivity(), "正在刷新", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onLoadingMore() {
				requestDataFromServer(false);
				Toast.makeText(getActivity(), "正在加载更多", Toast.LENGTH_SHORT).show();
			}
		});

	}
	
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
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.concel_img:
				if(mFinancelMessage!=null)
					mFinancelMessage.setVisibility(View.GONE);
				break;
		}
	}

	//Financing Adapter
	class mFinanceAdapter extends BaseAdapter{

		private int currentType;  //当前item类型
		private static final int TYPE_COUNT = 2;//item类型的总数
		private static final int TYPE_Title = 0;// 标题
		private static final int TYPE_Content = 1;//内容

		@Override
		public int getCount() {
			if(FinanceJsonBean.getInstance().getGxb().size()>0){
				int count = 3+FinanceJsonBean.getInstance().getGxb().size();
				return count ;
			}
			return 0;
		}
		@Override
		public Object getItem(int i) {
			return  null;
		}
		@Override
		public long getItemId(int i) {
			return i;
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

						break;
					case  TYPE_Content:
						view = LayoutInflater.from(getActivity()).inflate(R.layout.item_items,null);
						mViewHonder.mText_vip = (TextView)view.findViewById(R.id.vip_baifenbi);
						mViewHonder.mInterge=(TextView)view.findViewById(R.id.tv1);
						mViewHonder.mPoint = (TextView)view.findViewById(R.id.xiaoshu);
						mViewHonder.mCircleProgressView = (CircleProgressBar)view.findViewById(R.id.cricleprogress);

						mViewHonder.profit_tv = (TextView)view.findViewById(R.id.profit_tv);
						mViewHonder.mProject = (TextView)view.findViewById(R.id.rel_tv1);

						mViewHonder.mNewPerson = (RelativeLayout)view.findViewById(R.id.new_person);
						mViewHonder.mLimitDay = (TextView)view.findViewById(R.id.limit_day);
						break;
				}
				view.setTag(mViewHonder);
			}else {
				mViewHonder = (ViewHonder)view.getTag();
			}

			if(currentType==TYPE_Title){
				if(i==0){
					if(mViewHonder.mText_section!=null)
						mViewHonder.mText_section.setText("时息通");
				}else if(i==2){
					if(mViewHonder.mText_section!=null)
						mViewHonder.mText_section.setText("固息宝");
				}
			}
			if(currentType == TYPE_Content){
				if(i==1){  // 时息通
					FinanceInfo sxt = FinanceJsonBean.getInstance().getSxt();
					if(mViewHonder.mInterge!=null&&mViewHonder.mPoint!=null) {
						double apr = sxt.getApr();
						if(isInterge(apr)){
							mViewHonder.mInterge.setText(String.valueOf(apr));
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
					if(mViewHonder.mCircleProgressView!=null)
						mViewHonder.mCircleProgressView.setPaintColor("#FFEE4E42");
						mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(sxt.getPercent())),
								mViewHonder.iv);
					// 新手标
					double bird = sxt.getBird();
					if(mViewHonder.mNewPerson!=null){
						if(bird == 1){
							mViewHonder.mNewPerson.setVisibility(View.VISIBLE);
						}else{
							mViewHonder.mNewPerson.setVisibility(View.INVISIBLE);
						}
					}

					// 标题
					if(mViewHonder.mProject!=null)
						mViewHonder.mProject.setText(sxt.getTitle());

					//支付方式
					if(mViewHonder.profit_tv!=null)
						mViewHonder.profit_tv.setText(sxt.getPaytype());

					//日期
					double limit = sxt.getLimit();
					if(mViewHonder.mLimitDay!=null){
						String limit_day = String.valueOf(limit);
						String[] day = limit_day.split("[.]");
						mViewHonder.mLimitDay.setText(day[0]+"天");
					}

					if(view!=null)
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							getActivity().startActivity(new Intent(getActivity(),
									TimeXiTongActivity.class));
						}
					});

				}else if (i>=3){ // 固息宝
					List<FinanceInfo> mList = FinanceJsonBean.getInstance().getGxb();
					FinanceInfo gxb = mList.get(i-3);
					if(mViewHonder.mInterge!=null&&mViewHonder.mPoint!=null) {
						double apr = gxb.getApr();
						if(isInterge(apr)){
							mViewHonder.mInterge.setText(String.valueOf(apr));
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
					if(mViewHonder.mCircleProgressView!=null)
						mViewHonder.mCircleProgressView.setPaintColor("#FFEE4E42");
						mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(gxb.getPercent())),
								mViewHonder.iv);

					// 新手标
					double bird = gxb.getBird();
					if(mViewHonder.mNewPerson!=null){
						if(bird == 1){
							mViewHonder.mNewPerson.setVisibility(View.VISIBLE);
						}else{
							mViewHonder.mNewPerson.setVisibility(View.INVISIBLE);
						}
					}
					//日期
					double limit = gxb.getLimit();
					if(mViewHonder.mLimitDay!=null){
						String limit_day = String.valueOf(limit);
						String[] day = limit_day.split("[.]");
						mViewHonder.mLimitDay.setText(day[0]+"天");
					}

					//标题
					if(mViewHonder.mProject!=null)
						mViewHonder.mProject.setText(gxb.getTitle());

					//支付方式
					if(mViewHonder.profit_tv!=null)
						mViewHonder.profit_tv.setText(gxb.getPaytype());

					if(view!=null)
						view.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
								getActivity().startActivity(new Intent(getActivity(),
										GuXiBaoActivity.class));
							}
						});
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
	/**
	 * ViewHonder的类
	 */
	class ViewHonder {
		TextView mText_section;
		//CircleProgressView mCircleProgressView ;    // 没有百分比的
		CircleProgressBar mCircleProgressView;
		TextView mText_vip;
		TextView mInterge; //整位数
		TextView mPoint; // 小数点后的数
		ImageView iv; //放cricleprogressbar的节点

		TextView mProject ;
		TextView profit_tv ;

		RelativeLayout mNewPerson ; //新手标
		TextView mLimitDay ; // 日期
	}

	//判断是否为整数
	private boolean isInterge(double num){
		if(num%1==0){
			return  true;
		}else{
			return false;
		}
	}


	// 获取产品信息
   private void getProductInfo(){
	   RequestParams params = new RequestParams();
	   params.put("p","1");
	   params.put("limit", "10");

	   HttpUtil.get(InterfaceInfo.PRODUCT_URL,params,new JsonHttpResponseHandler(){
		   @Override
		   public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			   super.onSuccess(statusCode, headers, response);
			   FinanceJsonBean  mFinanBean = null;
			   List<FinanceInfo>   mList = null ;
			   if(response!=null){
				   try {
					   Log.i("URL", "code码：=" + response.getInt("code"));
					   if (response.getInt("code") == 0) {
						   mFinanBean = FinanceJsonBean.getInstance();

						   JSONObject sxt = response.getJSONObject("sxt");
						   FinanceInfo sxtInfo = setProductInfo(sxt);  // 时息通
						   mFinanBean.setSxt(sxtInfo);

						  JSONArray gxb =  response.getJSONArray("gxb"); // 固息宝
						   if(gxb.length()>0){
							   mList = new ArrayList<FinanceInfo>();
							   for (int i=0;i<gxb.length();i++){
								   JSONObject gxb_obj = gxb.getJSONObject(i);
								   FinanceInfo gxbInfo = setProductInfo(gxb_obj);
								   mList.add(gxbInfo);
							   }
							   mFinanBean.setGxb(mList);
						   }
						   mFinanBean.setMsg(response.getString("msg"));
						   mFinanBean.setTime(response.getString("time"));
						   //TODO 成功访问网络后setAdapter
						   mListView.setAdapter(new mFinanceAdapter());


					   } else {
						   Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
					   }
				   } catch (Exception e) {

				   }
			   }
		   }
		   @Override
		   public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			   super.onFailure(statusCode, headers, throwable, errorResponse);
			   Toast.makeText(getActivity(),"网络访问失败",Toast.LENGTH_SHORT).show();
		   }
	   },true);
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
				info.setRemark(obj.getString("remark"));
				info.setRule(obj.getString("rule"));
				info.setMin(obj.getString("min"));
				info.setScore(obj.getInt("score"));
				info.setVip(obj.getDouble("vip"));
				info.setUrl(obj.getString("url"));
				info.setTotal_amount(obj.getInt("total_amount"));
				info.setTotal_menber(obj.getInt("total_menber"));
				return info;
			}catch (Exception e){
				e.getStackTrace();
			}
		}else {
			Toast.makeText(getActivity(),"FinanceInfo为空",Toast.LENGTH_SHORT).show();
		}
		return null ;
	}
}

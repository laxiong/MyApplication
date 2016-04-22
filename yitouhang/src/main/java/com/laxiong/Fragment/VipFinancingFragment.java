package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
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

import com.laxiong.Json.FinanceJsonBean;
import com.laxiong.View.CircleProgressBar;
import com.laxiong.View.FinancingListView;
import com.laxiong.entity.FinanceInfo;
import com.laxiong.yitouhang.R;

import java.util.List;

@SuppressLint("NewApi") 
public class VipFinancingFragment extends Fragment implements View.OnClickListener{
	/****
	 * VIP理财的碎片
	 */
	private View mVipView ;
//	private PinnedSectionListView mPinnedSectionListView ;
//	private VipPinnedListViewAdapter mVipAdapter ;

	private ImageView mConcel_img ;
	private LinearLayout mFinancelMessage ; // 提示消息

	private FinancingListView mListView ;
	
	private Handler handler = new Handler() {
	      @Override
	      public void handleMessage(Message msg) {
	           super.handleMessage(msg);
	           switch (msg.what){
	               case 1:
	            	    Toast.makeText(getActivity(), "完成刷新", Toast.LENGTH_SHORT).show();
					   mListView.completeRefresh();
	                    break;
	               case 2:
	            	    Toast.makeText(getActivity(), "完成加载更多", Toast.LENGTH_SHORT).show();
					   mListView.completeRefresh();
	                    break;
	           }
	       }
	  };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mVipView = inflater.inflate(R.layout.financing_layout, null);
		initView();
		initData();
		return  mVipView;
	}
	private void initData() {
//		mVipAdapter = new VipPinnedListViewAdapter(getActivity(), Bean.getData());
//		mPinnedSectionListView.setAdapter(mVipAdapter);

		mListView.setAdapter(new VipAdapter());

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
	private void initView() {

		mConcel_img = (ImageView)mVipView.findViewById(R.id.concel_img);
		mFinancelMessage = (LinearLayout)mVipView.findViewById(R.id.finance_message);
		mConcel_img.setOnClickListener(this);

//		mPinnedSectionListView = (PinnedSectionListView)mVipView.findViewById(R.id.sectionListview);
		mListView = (FinancingListView)mVipView.findViewById(R.id.Listview);
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
			if(FinanceJsonBean.getInstance().getGxb().size()>0){
				return FinanceJsonBean.getInstance().getGxb().size()+3;
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
					if(mViewHonder.mText_section!=null) {
						mViewHonder.mText_section.setText("时息通");
						mViewHonder.mText_section.setTextColor(Color.parseColor("#FFE2A42A"));
					}
				}else if(i==2){
					if(mViewHonder.mText_section!=null) {
						mViewHonder.mText_section.setText("固息宝");
						mViewHonder.mText_section.setTextColor(Color.parseColor("#FFE2A42A"));
					}
				}
				if(mViewHonder.line!=null)
					mViewHonder.line.setBackgroundColor(Color.parseColor("#FFE2A42A"));
			}
			if(currentType == TYPE_Content){
				if(i==1){  // 时息通
					FinanceInfo sxt = FinanceJsonBean.getInstance().getSxt();
					if(mViewHonder.mInterge!=null&&mViewHonder.mPoint!=null) {
						mViewHonder.mInterge.setTextColor(Color.parseColor("#FFE2A42A"));
						mViewHonder.mPoint.setTextColor(Color.parseColor("#FFE2A42A"));
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
						mViewHonder.mCircleProgressView.setPaintColor("#FFE2A42A");
						mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(sxt.getPercent())),
								mViewHonder.iv);

					if(mViewHonder.vip_addbf!=null)
						mViewHonder.vip_addbf.setVisibility(View.VISIBLE);
					mViewHonder.vip_addbf.setText("+"+String.valueOf(sxt.getVip()));
					mViewHonder.vip_addbf.setTextColor(Color.parseColor("#FFE2A42A"));

					// 新手标
					double bird = sxt.getBird();
					if(mViewHonder.mNewPerson!=null){
						if(bird == 1){
							mViewHonder.mNewPerson.setVisibility(View.VISIBLE);
						}else{
							mViewHonder.mNewPerson.setVisibility(View.INVISIBLE);
						}
					}
					//日期
					double limit = sxt.getLimit();
					if(mViewHonder.mLimitDay!=null){
						String limit_day = String.valueOf(limit);
						String[] day = limit_day.split("[.]");
						mViewHonder.mLimitDay.setText(day[0]+"天");
					}

					if(mViewHonder.profit_tv!=null)
						mViewHonder.profit_tv.setTextColor(Color.parseColor("#FFE2A42A"));
					mViewHonder.profit_tv.setText(sxt.getPaytype());
					if(mViewHonder.baifenbi!=null)
						mViewHonder.baifenbi.setTextColor(Color.parseColor("#FFE2A42A"));
					if(mViewHonder.mProject!=null)
						mViewHonder.mProject.setTextColor(Color.parseColor("#FFE2A42A"));
					mViewHonder.mProject.setText(sxt.getTitle());
					if(mViewHonder.year_hua!=null)
						mViewHonder.year_hua.setTextColor(Color.parseColor("#FFE2A42A"));


				}else if (i>=3){ // 固息宝
					List<FinanceInfo> mList = FinanceJsonBean.getInstance().getGxb();
					FinanceInfo gxb = mList.get(i-3);
					if(mViewHonder.mInterge!=null&&mViewHonder.mPoint!=null) {
						mViewHonder.mInterge.setTextColor(Color.parseColor("#FFE2A42A"));
						mViewHonder.mPoint.setTextColor(Color.parseColor("#FFE2A42A"));
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
						mViewHonder.mCircleProgressView.setPaintColor("#FFE2A42A");
						mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(gxb.getPercent())),
								mViewHonder.iv);
//
					if(mViewHonder.vip_addbf!=null)
						mViewHonder.vip_addbf.setVisibility(View.VISIBLE);
					mViewHonder.vip_addbf.setText("+" + String.valueOf(gxb.getVip()));
					mViewHonder.vip_addbf.setTextColor(Color.parseColor("#FFE2A42A"));

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

					if(mViewHonder.profit_tv!=null)
						mViewHonder.profit_tv.setTextColor(Color.parseColor("#FFE2A42A"));
					mViewHonder.profit_tv.setText(gxb.getPaytype());
					if(mViewHonder.baifenbi!=null)
						mViewHonder.baifenbi.setTextColor(Color.parseColor("#FFE2A42A"));
					if(mViewHonder.mProject!=null)
						mViewHonder.mProject.setTextColor(Color.parseColor("#FFE2A42A"));
					mViewHonder.mProject.setText(gxb.getTitle());
					if(mViewHonder.year_hua!=null)
						mViewHonder.year_hua.setTextColor(Color.parseColor("#FFE2A42A"));
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
		TextView baifenbi;
		TextView mInterge;
		TextView mPoint;
		ImageView iv; //放cricleprogressbar的节点
		View line ;
		TextView vip_addbf ;

		TextView mProject ;
		TextView year_hua ;
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
}

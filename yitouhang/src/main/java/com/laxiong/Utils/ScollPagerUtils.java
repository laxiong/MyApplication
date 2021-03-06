package com.laxiong.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.laxiong.Activity.WebViewActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.View.ChildViewPager;
import com.laxiong.View.ChildViewPager.OnSingleTouchListener;
import com.laxiong.entity.Banner;
import com.laxiong.entity.ShareInfo;
import com.laxiong.entity.User;
import com.carfriend.mistCF.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/****
 */
public class ScollPagerUtils implements OnPageChangeListener {

    private Context mContext;
    private ArrayList<Banner> bannerList;
    /****
     * initView include PagerView and dot
     */
    private ChildViewPager mChildViewPager;
    private LinearLayout mDotLinearlayout;
    List<ImageView> ImagList = new ArrayList<ImageView>();  //ViewPager ImageView Collection
    List<ImageView> dotList = new ArrayList<ImageView>();  //dot ImageView Collection

    private int prePoint = 300; // 前面的小点的位置
    private int currentItem; //当前页面

    private PagerChangeAdapter mPagerChangeAdapter;
    private IndicateImageUtilLj mBannerIndicator;

    public ScollPagerUtils(ArrayList<Banner> bannerList, Context mContext, ChildViewPager mChildViewPager,
                           LinearLayout mDotLinearlayout) {
        this.mContext = mContext;
        this.bannerList = bannerList;
        this.mChildViewPager = mChildViewPager;
        this.mDotLinearlayout = mDotLinearlayout;
        mChildViewPager.setOnPageChangeListener(this);
        initDotView();
        initChildView();
        mPagerChangeAdapter = new PagerChangeAdapter();
        mChildViewPager.setAdapter(mPagerChangeAdapter);
    }

    /****
     * init Linearlayout dot
     */
    private void initDotView() {

        mDotLinearlayout.removeAllViewsInLayout();
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.setMargins(DensityUtils.dp2px(mContext, 5), 0, 0, 0);
            point.setLayoutParams(params);
            if (i == (prePoint % bannerList.size())) {
                point.setBackgroundResource(R.drawable.img_point_lang);
            } else {
                point.setBackgroundResource(R.drawable.img_point_short);
            }
            dotList.add(point);
            mDotLinearlayout.addView(point);
        }
    }

    /****
     * init ChildViewPager Images
     */
    private void initChildView() {
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView imgs = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            imgs.setScaleType(ScaleType.FIT_XY);
            imgs.setLayoutParams(params);
            //				1			2										3								   4
            Glide.with(mContext).load(bannerList.get(i).getImageurl()).centerCrop().placeholder(R.drawable.gongshi_banner_mr).crossFade().into(imgs);
            ImagList.add(imgs);
        }

        // 滑动时触发的事件
        mChildViewPager.setOnSingleTouchListener(new OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                if (bannerList.size() > 0) {
                    Banner banner = bannerList.get(mChildViewPager.getCurrentItem() % bannerList.size());
                    User user = YiTouApplication.getInstance().getUser();
//					String uri = bannerList.get(mChildViewPager.getCurrentItem() % bannerList.size()).getHref();
//					String title = bannerList.get(mChildViewPager.getCurrentItem() % bannerList.size()).getTitle();
//					if(uri!=null&&title!=null){
                    if (banner != null) {
//						mContext.startActivity(new Intent(mContext,WebViewActivity.class).
//								putExtra("url", uri).putExtra("title",title));
                        Bundle bundle = new Bundle();
                        ShareInfo sinfo=new ShareInfo(banner.getTitle(), banner.getContent(),
                                banner.getShareimageurl(), banner.getHref() + "?user_id=" + (user == null ? "" : user.getId()));
                        bundle.putSerializable("banner",sinfo);

                        //友盟Banner页面的统计
                        MobclickAgent.onEvent(mContext,"Banner"+banner.getTitle());

                        mContext.startActivity(new Intent(mContext, WebViewActivity.class).putExtra("title",banner.getTitle()).putExtra("needshare", true).putExtra("url", banner.getHref() + "?id=" + (user == null ? "" : user.getId())).putExtras(bundle));

                    }
                }
            }
        });
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        setDotBackground(position);

        if (ImagList.size() > 0) {
            if (mPagerChangeAdapter != null) {
                mPagerChangeAdapter.notifyDataSetChanged();
            } else {
                mPagerChangeAdapter = new PagerChangeAdapter();
                if (bannerList.size() > 1) // 如果广告多于1张，无限循环
                    mChildViewPager.setCurrentItem(300);

                if (mBannerIndicator == null)
                    mBannerIndicator = new IndicateImageUtilLj((Activity) mContext, mChildViewPager, null);

                mBannerIndicator.initTask();
                mBannerIndicator.startRepeat();
            }
        }
    }

    /****
     * setAdapter for ChildViewPager
     */
    class PagerChangeAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (ImagList != null) {
                return ImagList.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ChildViewPager) container).removeView(ImagList.get(position % ImagList.size()));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ChildViewPager) container).addView((View) ImagList.get(position % ImagList.size()), 0);
            return ImagList.get(position % ImagList.size());
        }
    }

    /***
     * set dot Background
     */
    private void setDotBackground(int selectItem) {
        for (int i = 0; i < bannerList.size(); i++) {
            if (i == selectItem) {
                dotList.get(i).setBackgroundResource(R.drawable.img_point_lang);
                currentItem = i;
            } else {
                dotList.get(i).setBackgroundResource(R.drawable.img_point_short);
            }
        }
    }

    /***
     * set Auto play pic
     */
    class autoPlayImages implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            currentItem = (currentItem + 1) % bannerList.size();
            //更新界面
            handler.obtainMessage().sendToTarget();
        }
    }

    /***
     * Start play pic
     */
    private ScheduledExecutorService scheduledExecutorService = null;

    public void startPlayPic() {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleWithFixedDelay(new autoPlayImages(), 2, 2, TimeUnit.SECONDS);
        }
    }

    /****
     * Handler set
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mChildViewPager.setCurrentItem(currentItem);
        }

        ;
    };


}

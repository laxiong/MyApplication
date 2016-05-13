package com.laxiong.Activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Calender.CalendarView;
import com.laxiong.Calender.CalendarViewBuilder;
import com.laxiong.Calender.CalendarViewPagerLisenter;
import com.laxiong.Calender.CustomDate;
import com.laxiong.Calender.CustomViewPagerAdapter;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.gongshidai.mistGSD.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by admin on 2016/5/3.
 */
public class CalanderHorizontalActivity extends BaseActivity implements View.OnClickListener, CalendarView.CallBack {
    /****
     * 水平滑动的账单日历
     */
    private ViewPager viewPager;
    private CalendarView[] views;
    private TextView showYearView;  // 显示年
    private TextView showMonthView; //月
    private RelativeLayout mCalenderSelect ; // 选择周或月的
    private CalendarViewBuilder builder ;
    private SlidingDrawer mSlidingDrawer;
    private View mContentPager;
    private CustomDate mClickDate;
    private FrameLayout mBack ;
    private TextView mBackToday ;
    private ImageView mYearImg , mMonthImg ;
    public static final String MAIN_ACTIVITY_CLICK_DATE = "main_click_date";
    private TextView mCalendarPoint ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_horizonal);
        findViewbyId();
        selectWeekAndMonth();
        getCalenderInfo();
    }

    private void findViewbyId() {

        mYearImg =(ImageView)findViewById(R.id.img1);
        mMonthImg =(ImageView)findViewById(R.id.img2);
        mBackToday =(TextView)findViewById(R.id.now_circle_view);
        mCalendarPoint =(TextView) findViewById(R.id.calender_point);

        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        showMonthView = (TextView) this.findViewById(R.id.show_month_view);
        showYearView = (TextView) this.findViewById(R.id.show_year_view);
        mBack =(FrameLayout) this.findViewById(R.id.back_layout);
        mCalenderSelect =(RelativeLayout)this.findViewById(R.id.clendar_select);
        mContentPager = this.findViewById(R.id.contentPager);
        mSlidingDrawer = (SlidingDrawer) this.findViewById(R.id.sildingDrawer);

        mCalenderSelect.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mCalendarPoint.setOnClickListener(this);
        mBackToday.setOnClickListener(this);

        builder = new CalendarViewBuilder(this,this);
        views = builder.createMassCalendarViews(5);
        setViewPager();
        setOnDrawListener();
    }

    private void setViewPager() {
        CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(498);
        viewPager.setOnPageChangeListener(new CalendarViewPagerLisenter(viewPagerAdapter));
    }

    private void setOnDrawListener() {
        mSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {

            @Override
            public void onDrawerOpened() {
//                builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE);
                selectWeekAndMonthSlide();  // 周的
            }
        });
        mSlidingDrawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {

            @Override
            public void onScrollStarted() {
//                builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
                selectWeekAndMonthSlide(); // 月的
            }

            @Override
            public void onScrollEnded() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setShowDateViewText(int year ,int month){
        showYearView.setText(year + "");
        showMonthView.setText("." + month);
//        showWeekView.setText(DateUtil.weekName[DateUtil.getWeekDay() - 1]);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clendar_select:   // 选择的周月
                selectWeekAndMonth();
                break;
            case R.id.back_layout:   // 返回
                this.finish();
                break;
            case R.id.now_circle_view: // 返回今天
                builder.backTodayCalendarViews();
                break;
            case R.id.calender_point: // 三个点
                showPointUsefulMsg();
                break;
        }
    }
    //点击时  周或月的选择   默认是月的
    private boolean isSelect = true ;
    private void selectWeekAndMonth(){
        initKongPiontImg();
        if (isSelect){
            builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
            mSlidingDrawer.close();
            isSelect = false ;
            mYearImg.setImageResource(R.drawable.img_result_piont_mr);
        }else {
            builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE);
            mSlidingDrawer.open();
            isSelect = true ;
            mMonthImg.setImageResource(R.drawable.img_result_piont_mr);
        }
    }

    //上下拉动时的 周月的选择  默认为月
    private void selectWeekAndMonthSlide(){
        initKongPiontImg();
        if (isSelect){   //  下拉
            builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
            mYearImg.setImageResource(R.drawable.img_result_piont_mr);
            isSelect = false;

        }else {    // 上拉
            builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE);
            mMonthImg.setImageResource(R.drawable.img_result_piont_mr);
            isSelect = true;
        }
    }

    private void initKongPiontImg(){
        mYearImg.setImageResource(R.drawable.img_kong_point);
        mMonthImg.setImageResource(R.drawable.img_kong_point);
    }

    @Override
    public void clickDate(CustomDate date) {
        mClickDate = date;
    }

    @Override
    public void onMesureCellHeight(int cellSpace) {
        mSlidingDrawer.getLayoutParams().height = mContentPager.getHeight() - cellSpace;
    }
    @Override
    public void changeDate(CustomDate date) {
        setShowDateViewText(date.year, date.month);
    }

    // 显示三点点击的信息
    private PopupWindow mPointWindow ;
    private View mPointView ;
    private void showPointUsefulMsg(){

        mPointView = LayoutInflater.from(this).inflate(R.layout.calendar_point_msg,null);
        ImageView ImgClose = (ImageView)mPointView.findViewById(R.id.close_img);
        ImgClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mPointWindow!=null&&mPointWindow.isShowing()){
                    mPointWindow.dismiss();
                    mPointWindow = null ;
                }
            }
        });

        mPointWindow = new PopupWindow(mPointView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        mPointWindow.setTouchable(true);
        mPointWindow.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mPointWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mPointWindow.showAtLocation(mPointView, Gravity.BOTTOM, 0, 0);
    }

    // 用户单日 日历日期
    private void getCalenderInfo(){
        RequestParams params = new RequestParams();
        int mId = YiTouApplication.getInstance().getUser().getId();
        if (mId!=0)
            params.put("id",mId);
        params.put("date","2016-05-14");
        HttpUtil.get(InterfaceInfo.BASE_URL + "/daytrade/" + mId, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            Log.i("WK", "=====点击当天的数据=======：" + response);
                            try {


                            } catch (Exception e) {
                            }
                        } else {
                        }
                    } catch (Exception E) {
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        }, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
                YiTouApplication.getInstance().getUserLogin().getToken()));
    }

}
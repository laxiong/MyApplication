package com.laxiong.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.laxiong.Calender.CalendarView;
import com.laxiong.Calender.CalendarViewBuilder;
import com.laxiong.Calender.CalendarViewPagerLisenter;
import com.laxiong.Calender.CalendarViewVer;
import com.laxiong.Calender.CustomDate;
import com.laxiong.Calender.CustomViewPagerAdapter;
import com.laxiong.yitouhang.R;

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
    private CalendarViewBuilder builder = new CalendarViewBuilder();
    private SlidingDrawer mSlidingDrawer;
    private View mContentPager;
    private CustomDate mClickDate;
    private FrameLayout mBack ;
    private TextView mBackToday ;
    private ImageView mYearImg , mMonthImg ;
    public static final String MAIN_ACTIVITY_CLICK_DATE = "main_click_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_horizonal);
        findViewbyId();
        selectWeekAndMonth();
    }

    private void findViewbyId() {

        mYearImg =(ImageView)findViewById(R.id.img1);
        mMonthImg =(ImageView)findViewById(R.id.img2);
        mBackToday =(TextView)findViewById(R.id.now_circle_view);

        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        showMonthView = (TextView) this.findViewById(R.id.show_month_view);
        showYearView = (TextView) this.findViewById(R.id.show_year_view);
        mBack =(FrameLayout) this.findViewById(R.id.back_layout);
        mCalenderSelect =(RelativeLayout)this.findViewById(R.id.clendar_select);
        views = builder.createMassCalendarViews(this, 5, this);
        mContentPager = this.findViewById(R.id.contentPager);
        mSlidingDrawer = (SlidingDrawer) this.findViewById(R.id.sildingDrawer);
        mCalenderSelect.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mBackToday.setOnClickListener(this);
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
                builder.swtichCalendarViewsStyle(CalendarViewVer.WEEK_STYLE);
            }
        });
        mSlidingDrawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {

            @Override
            public void onScrollStarted() {
                builder.swtichCalendarViewsStyle(CalendarViewVer.MONTH_STYLE);
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
        showYearView.setText(year+"");
        showMonthView.setText("."+month);
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
        }
    }
    // 周或月的选择   默认是月的
    private boolean isSelect = true ;
    private void selectWeekAndMonth(){
        initKongPiontImg();
        if (isSelect){
            builder.swtichCalendarViewsStyle(CalendarViewVer.MONTH_STYLE);
            mSlidingDrawer.close();
            isSelect = false ;
            mYearImg.setImageResource(R.drawable.img_result_piont_mr);
        }else {
            mSlidingDrawer.open();
            isSelect = true ;
            mMonthImg.setImageResource(R.drawable.img_result_piont_mr);
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
        setShowDateViewText(date.year,date.month);
    }

}
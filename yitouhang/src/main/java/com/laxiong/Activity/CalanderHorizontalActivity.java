package com.laxiong.Activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongshidai.mistGSD.R;
import com.google.gson.GsonBuilder;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Calender.CalendarView;
import com.laxiong.Calender.CalendarViewBuilder;
import com.laxiong.Calender.CalendarViewPagerLisenter;
import com.laxiong.Calender.CustomDate;
import com.laxiong.Calender.CustomViewPagerAdapter;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.DensityUtils;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.entity.DaiesCell;
import com.laxiong.entity.DayDetails;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

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
    private CustomDate mClickDate;
    private FrameLayout mBack ;
    private TextView mBackToday ;
    private ImageView mYearImg , mMonthImg ;
    public static final String MAIN_ACTIVITY_CLICK_DATE = "main_click_date";
    private TextView mCalendarPoint ;
    private ListView mLvDaieds , mLvDaiedsWeek; // 点击每一天的显示详情
    private RelativeLayout mCalendarbg ,mCalendarbgWeek; //日历的后面的背景
    private RelativeLayout mContentPager ;
    private LinearLayout mhandlerText ,mhandlerTextWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_horizonal);
        findViewbyId();
        selectWeekAndMonth();
//        getCalenderInfo();
    }

    private void findViewbyId() {

        mYearImg =(ImageView)findViewById(R.id.img1);
        mMonthImg =(ImageView)findViewById(R.id.img2);
        mBackToday =(TextView)findViewById(R.id.now_circle_view);
        mContentPager =(RelativeLayout)findViewById(R.id.contentPager);
        // 月
        mCalendarPoint =(TextView) findViewById(R.id.calender_point);
        mLvDaieds = (ListView)findViewById(R.id.calender_listview);
        mCalendarbg =(RelativeLayout)findViewById(R.id.calender_bg);
        mhandlerText =(LinearLayout)findViewById(R.id.handlerText);

        // 周
        mhandlerTextWeek = (LinearLayout)findViewById(R.id.handlerText_Week);
        mCalendarbgWeek = (RelativeLayout)findViewById(R.id.calender_bg_Week);
        mLvDaiedsWeek = (ListView)findViewById(R.id.calender_listview_Week);


        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        showMonthView = (TextView) this.findViewById(R.id.show_month_view);
        showYearView = (TextView) this.findViewById(R.id.show_year_view);
        mBack =(FrameLayout) this.findViewById(R.id.back_layout);
        mCalenderSelect =(RelativeLayout)this.findViewById(R.id.clendar_select);

        mCalenderSelect.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mCalendarPoint.setOnClickListener(this);
        mBackToday.setOnClickListener(this);

        builder = new CalendarViewBuilder(this,this);
        views = builder.createMassCalendarViews(5);
        setViewPager();
    }

    private void setViewPager() {
        CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(498);
        viewPager.setOnPageChangeListener(new CalendarViewPagerLisenter(viewPagerAdapter));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setShowDateViewText(int year ,int month){
        showYearView.setText(year + "年");
        showMonthView.setText(month+"月");
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
        if (isSelect){//选择月的
            builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
            isSelect = false ;
            mYearImg.setImageResource(R.drawable.img_result_piont_mr);
            setViewPagerHeigh(1);
//            mhandlerTextWeek.setVisibility(View.GONE);
//            mhandlerText.setVisibility(View.VISIBLE);


        }else {//选择周的
            builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE);
            // 变成周之后再把进入今天的日期
            builder.backTodayCalendarViews();
            isSelect = true ;
            mMonthImg.setImageResource(R.drawable.img_result_piont_mr);
            setViewPagerHeigh(2);
//            mhandlerTextWeek.setVisibility(View.VISIBLE);
//            mhandlerText.setVisibility(View.GONE);
        }
        getCalenderInfo();
    }

    // 动态设置ViewPager的高度
    private void setViewPagerHeigh(int index){
        RelativeLayout.LayoutParams llparams = (RelativeLayout.LayoutParams)viewPager.getLayoutParams(); //ViewPager的
        switch (index){
            case 1 :
                llparams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                viewPager.setLayoutParams(llparams);
                break;

            case 2 :
                llparams.height = DensityUtils.px2dp(this, 2600);
                viewPager.setLayoutParams(llparams);
//                viewPager.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
                break;
        }
    }

    private void initKongPiontImg(){
        mYearImg.setImageResource(R.drawable.img_kong_point);
        mMonthImg.setImageResource(R.drawable.img_kong_point);
    }

    @Override
    public void clickDate(CustomDate date) {
        mClickDate = date;
        getCalenderInfo();
    }
    @Override
    public void onMesureCellHeight(int cellSpace) {
        int dex = mContentPager.getHeight() - cellSpace ;
        if (dex > 250){
            mhandlerText.getLayoutParams().height = dex;
        }
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
        if (mClickDate!=null) {
            String datas = spriltDateStr(String.valueOf(mClickDate));
            params.put("date", datas);
        }
        HttpUtil.get(InterfaceInfo.BASE_URL + "/daytrade/" + mId, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            try {
                                DaiesCell mDaiesCell = new GsonBuilder().create().fromJson(response.toString(), DaiesCell.class);
                                if (mDaiesCell.getList()!=null
                                        &&mDaiesCell.getList().size()>0){
//                                     // 点击每一天的显示详情 //日历的后面的背景
//                                    mLvDaieds.setVisibility(View.VISIBLE);
//                                    mCalendarbg.setVisibility(View.GONE);
//                                    mLvDaieds.setAdapter(new DayAdapter(mDaiesCell.getList()));

                                    mLvDaiedsWeek.setVisibility(View.VISIBLE);
                                    mCalendarbgWeek.setVisibility(View.GONE);
                                    mLvDaiedsWeek.setAdapter(new DayAdapter(mDaiesCell.getList()));

                                }else {
//                                    mLvDaieds.setVisibility(View.GONE);
//                                    mCalendarbg.setVisibility(View.VISIBLE);
                                    mLvDaiedsWeek.setVisibility(View.GONE);
                                    mCalendarbgWeek.setVisibility(View.VISIBLE);

                                }
                            } catch (Exception e) {
                            }
                        } else {
                            Toast.makeText(CalanderHorizontalActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception E) {
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(CalanderHorizontalActivity.this,"获取网络数据失败",Toast.LENGTH_SHORT).show();
            }

        }, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
                YiTouApplication.getInstance().getUserLogin().getToken()));
    }

    // 每天的详情信息的适配器
    class DayAdapter extends BaseAdapter{
        List<DayDetails> list ;
        public DayAdapter(List<DayDetails> list){
            this.list = list ;
        }
        @Override
        public int getCount() {
            return list.size() ;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHorder mViewHorder =null ;
            if (convertView==null){
                mViewHorder = new ViewHorder();
                convertView = LayoutInflater.from(CalanderHorizontalActivity.this).inflate(R.layout.calendar_day_listview_item,null);
                mViewHorder.calType = (TextView)convertView.findViewById(R.id.calender_type);
                mViewHorder.calAmount =(TextView)convertView.findViewById(R.id.calender_amount);
                mViewHorder.calTime = (TextView)convertView.findViewById(R.id.calender_time);
                mViewHorder.calDec = (TextView)convertView.findViewById(R.id.calender_dec);
                convertView.setTag(mViewHorder);
            }else {
                mViewHorder=(ViewHorder)convertView.getTag();
            }
            DayDetails mDayDetails = list.get(position);
            mViewHorder.calDec.setText(mDayDetails.getDesc());
            mViewHorder.calTime.setText(mDayDetails.getTime());
            mViewHorder.calAmount.setText(mDayDetails.getAmount());
            mViewHorder.calType.setText(mDayDetails.getType());
            return convertView;
        }
    };

    class ViewHorder{
        TextView calType ;
        TextView calAmount ;
        TextView calTime ;
        TextView calDec ;
    }

    // 日期截取字段的方法
    private String spriltDateStr(String date){
        //2016-5-24
        String year = date.substring(0, 4);
        String month = date.substring(5, 6);
        String day = date.substring(7, date.length());
        if (month.length()==1&&day.length()==1){
            String dates = year+"-0"+month+"-0"+day;
            return dates;
        }else if (month.length()==1&&day.length()>1){
            String dates = year+"-0"+month+"-"+day;
            return dates;
        }else if (month.length()>1&&day.length()==1){
            String dates = year+month+"-0"+day;
            return dates;
        }else {
            return date;
        }
    }


}
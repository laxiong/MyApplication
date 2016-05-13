package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.laxiong.Adapter.CalenderBean;
import com.laxiong.Adapter.SelectCalenderAdapter;
import com.laxiong.Calender.CalendarView;
import com.laxiong.Calender.CalendarView.CallBack;
import com.laxiong.Calender.CalendarViewBuilder;
import com.laxiong.Calender.CustomDate;
import com.laxiong.Mvp_presenter.MonthCal_Presenter;
import com.laxiong.Mvp_view.IViewMonthCal;
import com.laxiong.View.PinnedSectionListView;
import com.gongshidai.mistGSD.R;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("NewApi")
public class SelectMonthCalenderFragment extends Fragment implements CallBack, IViewMonthCal, PinnedSectionListView.IViewFragUpdate {
    /****
     * ListView的Calender
     */
    private PinnedSectionListView mSelectCalender;
    private View calendarview;
    private CalendarViewBuilder builder = new CalendarViewBuilder();
    private static CalendarView[] views;
    private SelectCalenderAdapter adapter;
    private CustomDate date;
    private int prenowyear, lastnowyear;
    private ArrayList<CalendarView> allMonth;
    private ArrayList<CalenderBean> addList;
    private int clickrow, clickcol;
    private TextView tv_year;
    private TextView tv_month;
    private MonthCal_Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (calendarview == null)
            calendarview = inflater.inflate(R.layout.select_month_calender_frag, null);
        else {
            ViewParent parent = calendarview.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(calendarview);
            }
        }
        initView();
        initData();
        initListener();
        return calendarview;
    }

    private void initData() {
        presenter = new MonthCal_Presenter(this);
        prenowyear = Calendar.getInstance().get(Calendar.YEAR);
        lastnowyear = prenowyear;
        adapter = new SelectCalenderAdapter(getActivity(), CalenderBean.getData(getActivity(), allMonth, prenowyear), date, clickrow, clickcol);
        mSelectCalender.setAdapter(adapter);
        Calendar ca = Calendar.getInstance();
        updateTitle(ca.get(Calendar.YEAR), 1);//更新标题
    }

    private void initView() {
        tv_year = (TextView) getActivity().findViewById(R.id.year_btn);
        tv_month = (TextView) getActivity().findViewById(R.id.month_change);
        mSelectCalender = (PinnedSectionListView) calendarview.findViewById(R.id.sectionListviewCalender);
        mSelectCalender.setIViewListener(this);
        views = builder.createMassCalendarViews(getActivity(), 12, this);
//        allMonth = ListViewCalenderUtil.getInstance().allMonthCalenderView(date, views);
    }
    //根据是上拉还是下拉加载相应list
    @Override
    public void refreshOrLoadList(boolean isrefresh) {
//        views = builder.createMassCalendarViews(getActivity(), 12, SelectMonthCalenderFragment.this);
//        allMonth = ListViewCalenderUtil.getInstance().allMonthCalenderView(date, views);
        int data = isrefresh ? --prenowyear : ++lastnowyear;
        addList = CalenderBean.getData(getActivity(), allMonth, data);
        adapter.addListPre(addList);
    }
    // 上拉或下拉加载完成
    @Override
    public void refreshOrLoadComplete(boolean isfresh) {
        mSelectCalender.completeRefresh();
        if (!isfresh && addList != null) adapter.addListLast(addList);
        mSelectCalender.setSelection(isfresh ? (adapter.getCount() / 2 - 1) : adapter.getCount() - 2 * 12 + 1);
        tv_year.setText(isfresh ? prenowyear + "" : lastnowyear + "");
        tv_month.setText(isfresh ? ".12" : ".01");
    }

//    @Override
//    public void clickDate(CustomDate date, int row, int col) {
//        this.date = date;
//        this.clickrow = row;
//        this.clickcol = col;
//        if (adapter != null) {
//            adapter.setClickDate(date, row, col);
//        }
//        ((CalanderCountActivity) getActivity()).setClickDate(date);
//    }


    @Override
    public void clickDate(CustomDate date) {

    }

    @Override
    public void onMesureCellHeight(int cellSpace) {


    }

    private void initListener() {
        mSelectCalender.setOnRefreshListener(new PinnedSectionListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                presenter.refresh();
            }

            @Override
            public void onLoadingMore() {
                presenter.loadmore();
            }
        });
    }

    @Override
    public void updateTitle(int year, int month) {
        tv_year.setText(year + "");
        tv_month.setText("." + (month < 10 ? "0" + month : month));
    }

    @Override
    public void changeDate(CustomDate date) {
//        this.date = date;
        date.getMonth();
        date.getYear();
    }


}

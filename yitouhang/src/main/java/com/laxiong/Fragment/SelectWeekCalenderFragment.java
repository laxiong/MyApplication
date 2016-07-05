package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.laxiong.Calender.CalendarView;
import com.laxiong.Calender.CustomDate;
import com.laxiong.Mvp_presenter.WeekCal_Presenter;
import com.laxiong.Mvp_view.IWeekCalendar;
import com.carfriend.mistCF.R;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class SelectWeekCalenderFragment extends Fragment implements CalendarView.CallBack, IWeekCalendar {
    private View layout;
    private LinearLayout llwrap;
    private ListView lv_things;
    private CalendarView view;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private WeekCal_Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout == null) {
            layout = inflater.inflate(R.layout.layout_week, null);
        } else {
            ViewParent parent = layout.getParent();
            if (parent != null && parent instanceof ViewGroup)
                ((ViewGroup) parent).removeView(layout);
        }
        initView();
//        initData();
//        initListener();
        return layout;
    }

    private void initView() {
        llwrap = (LinearLayout) layout.findViewById(R.id.ll_wrap);
        lv_things = (ListView) layout.findViewById(R.id.lv_things);
    }

//    private void initData() {
//        presenter = new WeekCal_Presenter(this);
//        Bundle bundle = getArguments();
//        CustomDate date = (CustomDate) bundle.getSerializable(Constants.KEY_DATE);
//        if (date != null) {
//            if (view == null) {
////                view = new CalendarView(getActivity(), CalendarView.WEEK_STYLE, this, date);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                view.setLayoutParams(lp);
//                llwrap.addView(view, 0);
//                if (list != null && list.size() > 0) {
//                    View v = new View(getActivity());
//                    v.setBackgroundColor(getResources().getColor(R.color.grey_bg));
//                    llwrap.addView(v, 1);
//                }
//            } else {
//                view.resetCustomDate(date);
//                view.setStyle(CalendarView.WEEK_STYLE);
//                view.update();
//            }
//        }
//        list = presenter.reqDataList(date);
//        if (list != null && list.size() > 0) {
//            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
//            lv_things.setAdapter(adapter);
//        }
//    }


    private void initListener() {
    }

//    @Override
//    public void clickDate(CustomDate date, int clickrow, int clickcol) {
//        list = presenter.reqDataList(date);
//        if (adapter != null && list != null && list.size() > 0) {
//            adapter.notifyDataSetChanged();
//        } else {
//            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
//            lv_things.setAdapter(adapter);
//        }
//    }

    @Override
    public void clickDate(CustomDate date) {

    }

    @Override
    public void onMesureCellHeight(int cellSpace) {

    }

    @Override
    public void changeDate(CustomDate date) {

    }
}

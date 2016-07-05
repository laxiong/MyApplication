package com.laxiong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.laxiong.Calender.CalendarView;
import com.laxiong.Calender.CustomDate;
import com.laxiong.Common.Settings;
import com.laxiong.View.PinnedSectionListView.PinnedSectionListAdapter;
import com.carfriend.mistCF.R;
import java.util.ArrayList;

public class SelectCalenderAdapter extends BaseAdapter implements PinnedSectionListAdapter {

    private ArrayList<CalenderBean> mlist;
    private Context mContext;
    private CustomDate clickdate;
    private int clickrow,clickcol;

    public SelectCalenderAdapter(Context mContext, ArrayList<CalenderBean> mlist, CustomDate date,int clickrow,int clickcol) {
        this.mContext = mContext;
        this.mlist = mlist;
        this.clickdate = date;
        this.clickrow=clickrow;
        this.clickcol=clickcol;
    }
    public void addListPre(ArrayList<CalenderBean> list){
        mlist.addAll(0,list);
        this.notifyDataSetChanged();
    }
    public void addListLast(ArrayList<CalenderBean> list){
        mlist.addAll(list);
        this.notifyDataSetChanged();
    }
    public void setClickDate(CustomDate date,int row,int col) {
        this.clickdate = date;
        this.clickrow=row;
        this.clickcol=col;
    }

    @Override
    public int getCount() {
        if (mlist.size() == 0) {
            return 0;
        } else {
            return mlist.size();
        }
    }

    @Override
    public Object getItem(int arg0) {
        return mlist.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View converView, ViewGroup arg2) {
        ViewHonder vh = null;
        CalenderBean bean = (CalenderBean) getItem(position);
        if (converView == null) {
            switch (bean.type) {
                case CalenderBean.ITEM:   // 日历显示
                    vh = new ViewHonder();
                    AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, Settings.DISPLAY_WIDTH - (Settings.DISPLAY_WIDTH / 7));
                    int index=position - (position + 1) / 2;
                    converView = (bean.mCalendarView).get(index>=bean.mCalendarView.size()?bean.mCalendarView.size()-1:index);
                    converView.setLayoutParams(lp);
                    break;
                case CalenderBean.SECTION:   //标题
                    vh = new ViewHonder();
                    converView = LayoutInflater.from(mContext).inflate(R.layout.item_section, null);
                    vh.mMonth = (TextView) converView.findViewById(R.id.section);
                    break;
            }
            converView.setTag(vh);
        } else {
            vh = (ViewHonder) converView.getTag();
        }
        // TODO 操作
        if (bean.type == CalenderBean.ITEM) {
            CalendarView view = (CalendarView) converView;
            view.resetCustomDate(bean.getCurrentDate());
            view.update();
//            if (bean.getCurrentDate().getMonth() != Calendar.MONTH + 1 && clickdate != null
            if (clickdate != null
                    && bean.getCurrentDate().monthequal(clickdate)) {
                view.performclick(clickrow,clickcol);
            }
        }
        if (bean.type == CalenderBean.SECTION) {
            vh.mMonth.setText(bean.text); // 赋值月
        }

        return converView;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == CalenderBean.SECTION;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return ((CalenderBean) getItem(position)).type;
    }

    // 复用的方法
    class ViewHonder {
        TextView mMonth;
        CalendarView mCalender;
    }


}

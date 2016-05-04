package com.laxiong.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.laxiong.Calender.CalendarView;
import com.laxiong.Calender.CalendarView.CallBack;
import com.laxiong.Calender.CalendarViewBuilder;
import com.laxiong.Calender.CustomDate;


public class CalenderBean {

	/****
	 * 解析获取的数据可以暂时的加载在此
	 */
	public static final int ITEM = 0;
	public static final int SECTION = 1;
	public final int type;
	public final String text;
//	public final CalendarView[] mCalendarView;
	private static String[] datas={"一月份","二月份","三月份","四月份","五月份",
"六月份","七月份","八月份","九月份","十月份","十一月份","十二月份"};
	public final ArrayList<CalendarView> mCalendarView;
	public final  Context mContext ;
	private  CustomDate currentDate;
	public CalenderBean(int type, String text,ArrayList<CalendarView> mCalendarView,Context mContext,CustomDate date) {
		this.type = type;
		this.text = text;
		this.mCalendarView = mCalendarView ;
		this.mContext = mContext ;
		this.currentDate=date;
	}

	public CustomDate getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(CustomDate currentDate) {
		this.currentDate = currentDate;
	}

	public static ArrayList<CalenderBean> getData(Context context,ArrayList<CalendarView> views,int year){
		ArrayList<CalenderBean>  list=new ArrayList<CalenderBean>();
		for(int i=0;i<views.size();i++){
			list.add(new CalenderBean(SECTION,datas[i],views,context,new CustomDate(year,i+1,1)));
			list.add(new CalenderBean(ITEM,year+"-"+i+1+"-01",views,context,new CustomDate(year,i+1,1)));
		}
		return list;
	}



}

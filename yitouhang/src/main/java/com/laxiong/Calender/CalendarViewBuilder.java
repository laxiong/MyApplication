package com.laxiong.Calender;

import android.content.Context;

import com.laxiong.Calender.CalendarView.CallBack;

/**
 * CalendarView的辅助类
 * @author huang
 *
 */
public class CalendarViewBuilder {
	private CalendarView[] calendarViews;
	private Context context ;
	private CallBack callBack ;
	public CalendarViewBuilder(Context context,CallBack callBack){
		this.context = context ;
		this.callBack = callBack ;
	}
	/**
	 生产多个CalendarView
	 * @param count
	 * @param style
	 * @return
	 */
	public  CalendarView[] createMassCalendarViews(int count,int style){
		calendarViews = new CalendarView[count];
		for(int i = 0; i < count;i++){
			calendarViews[i] = new CalendarView(context, style,callBack);
		}
		return calendarViews;
	}

	public  CalendarView[] createMassCalendarViews(int count){
		return createMassCalendarViews(count, CalendarView.MONTH_STYLE);
	}
	/**
	 * 切换CandlendarView的样式
	 * @param style
	 */
	public void swtichCalendarViewsStyle(int style){
		if (calendarViews != null)
			for(int i = 0 ;i < calendarViews.length;i++){
				calendarViews[i].switchStyle(style);
			}
	}
	/**
	 *  CandlendarView回到当前日期
	 */
	public void backTodayCalendarViews(){
		if(calendarViews != null)
			for(int i = 0 ;i < calendarViews.length;i++){
				calendarViews[i].backToday();
			}
	}

}
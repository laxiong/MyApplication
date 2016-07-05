package com.laxiong.Calender;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carfriend.mistCF.R;
import com.laxiong.Common.Settings;
import com.laxiong.Interface.NotifyDateSelect;
import com.laxiong.Utils.OnSingleClickListener;
import com.laxiong.Utils.TimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarGridViewAdapter extends BaseAdapter {

	private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
	private Calendar calSelected = Calendar.getInstance(); // 选择的日历
	private View selectedView;
	private NotifyDateSelect mNotifyDateSelect;
	private Activity activity;
	Resources resources;
	List<String> keys = new ArrayList<String>(); // date list
	List<Integer> values = new ArrayList<Integer>(); // 1,2,3 2 red 3 green

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
		keyLength = keys.size();
	}

	public List<Integer> getValues() {
		return values;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}

	/*
	 * constructor
	 */
	public CalendarGridViewAdapter(Activity a, Calendar cal) {
		calStartDate = cal;
		activity = a;
		resources = activity.getResources();
		titles = getDates();
	}

	public CalendarGridViewAdapter(Activity a) {
		activity = a;
		resources = activity.getResources();
		titles = getDates();
	}

	public CalendarGridViewAdapter(Context context) {
		resources = activity.getResources();
		titles = getDates();
	}

	public void setSelectedDate(Calendar cal) {
		calSelected = cal;
	}

	public Calendar getSelectedDate() {
		return calSelected;
	}

	public Calendar getCalStartDate() {
		return calStartDate;
	}

	// 刷新日历
	public void setCalStartDate(Calendar calStartDate) {
		this.calStartDate = calStartDate;
		setSelectedDate(calStartDate);
		titles = getDates();
		CalendarGridViewAdapter.this.notifyDataSetChanged();
	}

	private Calendar calToday = Calendar.getInstance(); // 今日
	private int iMonthViewCurrentMonth = 0; // 当前视图月

	// 根据改变的日期更新日历
	// 填充日历控件用
	private void UpdateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月

		// 星期一是2 星期天是1 填充剩余天数
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);

		calStartDate.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位

	}

	ArrayList<Date> titles;

	private ArrayList<Date> getDates() {

		UpdateStartDateForMonth();

		ArrayList<Date> alArrayList = new ArrayList<Date>();

		for (int i = 1; i <= 42; i++) {
			alArrayList.add(calStartDate.getTime());
			calStartDate.add(Calendar.DAY_OF_MONTH, 1);
		}

		return alArrayList;
	}

	@Override
	public int getCount() {
		return titles.size();
	}

	@Override
	public Object getItem(int position) {
		return titles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 日期开始
	TextView txtDay;
	int keyLength = 0; // keys list的长度

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Date myDate = (Date) getItem(position);
		Calendar calCalendar = Calendar.getInstance();
		calCalendar.setTime(myDate);

		int iMonth = calCalendar.get(Calendar.MONTH);

		View iv = LayoutInflater.from(activity).inflate(R.layout.item_date_picker, null);
		iv.setLayoutParams(
				new AbsListView.LayoutParams((Settings.DISPLAY_WIDTH - 12) / 7, (Settings.DISPLAY_WIDTH - 12) / 7));

		txtDay = (TextView) iv.findViewById(R.id.calendar_date);
		View state = (View) iv.findViewById(R.id.state);
		View bg = (View) iv.findViewById(R.id.color_bg);

		int day = myDate.getDate(); // 设置日期
		txtDay.setText(String.valueOf(day));
		txtDay.setId(position + 500);
		iv.setTag(myDate); 

		// 判断是否是当前月
		if (iMonth == iMonthViewCurrentMonth) {
			if (equalsDate(calToday.getTime(), myDate)) {
				// 当前日期
				txtDay.setTextColor(resources.getColor(R.color.v2_green));
				iv.setClickable(true);
				iv.setOnClickListener(new DateClickListener(calCalendar, iv));
			} else {
				txtDay.setTextColor(resources.getColor(R.color.black));
				iv.setClickable(true);
				iv.setOnClickListener(new DateClickListener(calCalendar, iv));
			}
		} else {
			txtDay.setTextColor(resources.getColor(R.color.v2_divider_grey));
			iv.setClickable(false);
		}

		/*
		 * 设置圆圈
		 */
		if (equalsDate(calToday.getTime(), myDate)) {
			// 今天显示红色圆圈
			state.setBackgroundResource(R.drawable.shape_stoke_cirlce_red);
			state.setVisibility(View.VISIBLE);
		} else {
			// 设置背景颜色
			if (equalsDate(calSelected.getTime(), myDate)) {
				// 选择的
				state.setVisibility(View.VISIBLE);
				selectedView = iv;
			} else {
				state.setVisibility(View.INVISIBLE);
			}
		}
 
		/*
		 * 设置圆形背景
		 */
		bg.setVisibility(View.INVISIBLE);
		for (int i = 0; i < keyLength; i++) { // 遍历匹配
			if (TimeUtil.calendarToString(calCalendar).equals(keys.get(i))) {
				if (values.get(i) == 1 || values.get(i) == 2) {
					bg.setBackgroundResource(R.drawable.shape_rili_red_circle);
				} else {
					bg.setBackgroundResource(R.drawable.shape_rili_green_circle);
				}
				bg.setVisibility(View.VISIBLE);
				
				break;
			}
		}

		return iv;
	}

	private Calendar getCalandar(int position) {
		Date myDate = (Date) getItem(position);
		Calendar calCalendar = Calendar.getInstance();
		calCalendar.setTime(myDate);
		return calCalendar;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	private class DateClickListener extends OnSingleClickListener {

		Calendar mCarlandar;
		View view;

		public DateClickListener(Calendar mCarlandar, View view) {
			this.mCarlandar = mCarlandar;
			this.view = view;
		}

		@Override
		public void doOnClick(View v) {
			if (equalsDate(mCarlandar.getTime(), calSelected.getTime())) {
				return; // click selected item
			}
			calSelected = mCarlandar;
			selectedView = v;

			if (mNotifyDateSelect != null)
				mNotifyDateSelect.notify(calSelected);

			keyLength = keys.size();
			CalendarGridViewAdapter.this.notifyDataSetInvalidated();
		}

	}

	void changeColor(ViewGroup group, int colorRes) {
		for (int i = 0; i < group.getChildCount(); i++) {
			if (group.getChildAt(i) instanceof TextView) {
				((TextView) group.getChildAt(i)).setTextColor(colorRes);
			} else if (group.getChildAt(i) instanceof ViewGroup)
				changeColor((ViewGroup) group.getChildAt(i), colorRes);
		}

	}

	private Boolean equalsDate(Date date1, Date date2) {

		if (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth()
				&& date1.getDate() == date2.getDate()) {
			return true;
		} else {
			return false;
		}

	}

	private Boolean beforeDate(Date date1, Date date2) {

		if (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth()
				&& date1.getDate() < date2.getDate()) {
			return true;
		} else {
			return false;
		}
	}

	public NotifyDateSelect getmNotifyDateSelect() {
		return mNotifyDateSelect;
	}

	public void setmNotifyDateSelect(NotifyDateSelect mNotifyDateSelect) {
		this.mNotifyDateSelect = mNotifyDateSelect;
	}

}

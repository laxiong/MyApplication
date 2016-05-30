package com.laxiong.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongshidai.mistGSD.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Calender.CalendarGridView;
import com.laxiong.Calender.CalendarGridViewAdapter;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Common.Settings;
import com.laxiong.Interface.NotifyDateSelect;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.OnSingleClickListener;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.TimeUtil;
import com.laxiong.entity.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/*
 * 账单日历
 */
public class BillCalendarActivity extends BaseActivity {
	private CalendarGridView mCalendarGrid; // GridView
	private CalendarGridViewAdapter mAdapter;
	FrameLayout mBack;
	TextView mTitle;
	RelativeLayout mContainer;
	LinearLayout mSelMonthLayout;
	TextView mShowMonth;
	String date/* 传给服务器日期 */;
	Calendar mSelCalendar;
	String selDate;
	ListView mListView;
	TextView mDate; // 显示选中日期
	TextView mTotalInv /*总投资*/, mTotalGet/*总收益*/;
	BillListAdapter mListAdapter;
	LinearLayout mLinLayout1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bill_calendar);

		mBack = (FrameLayout) findViewById(R.id.back_layout);
		mTitle = (TextView) findViewById(R.id.title);
		mSelMonthLayout = (LinearLayout) findViewById(R.id.sel_month_layout);
		mShowMonth = (TextView) findViewById(R.id.billcalendar_show_month);
		mListView = (ListView) findViewById(R.id.bill_listview);
		mDate = (TextView) findViewById(R.id.billcalendar_date);
		mTotalInv = (TextView) findViewById(R.id.bill_calendar_total_invest);
		mTotalGet = (TextView) findViewById(R.id.bill_calendar_total_get);
		mLinLayout1 = (LinearLayout) findViewById(R.id.billcal_lin1);
		mLinLayout1.getLayoutParams().height = (int) (130 * Settings.RATIO_HEIGHT);

		mContainer = (RelativeLayout) findViewById(R.id.calendar_container);
		mCalendarGrid = new CalendarGridView(BillCalendarActivity.this);
		mAdapter = new CalendarGridViewAdapter(BillCalendarActivity.this);
		mCalendarGrid.setAdapter(mAdapter);
		mContainer.addView(mCalendarGrid);

		mSelMonthLayout.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				startActivityForResult(new Intent(BillCalendarActivity.this, DarkThemeActivity.class), 1020);
			}
		});

		mBack.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				finish();
			}

		});
		// 监听选中日期变化
		mAdapter.setmNotifyDateSelect(new NotifyDateSelect() {

			@Override
			public void notify(Calendar calendar) {
				mSelCalendar = calendar;
				date = TimeUtil.calendarToString(mSelCalendar);
				mDate.setText(
						(mSelCalendar.get(Calendar.MONTH) + 1) + "月" + mSelCalendar.get(Calendar.DAY_OF_MONTH) + "日");

				getListForDate();
			}
		});

		mTitle.setText("账单日历");

		mSelCalendar = Calendar.getInstance(Locale.getDefault());
		mDate.setText((mSelCalendar.get(Calendar.MONTH) + 1) + "月" + mSelCalendar.get(Calendar.DAY_OF_MONTH) + "日");
		mShowMonth.setText((mSelCalendar.get(Calendar.MONTH) + 1) + "月");
		date = TimeUtil.calendarToString(mSelCalendar);

		User info = YiTouApplication.getInstance().getUser();

		mTotalInv.setText(info.getAmount() + "");
		mTotalGet.setText(info.getProfit() + "");

		mListAdapter = new BillListAdapter();
		mListView.setAdapter(mListAdapter);

		getDataForSpecificDay();
		getListForDate();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1021 && data != null) {
			if (data.getStringExtra("time") != null) {
				String[] times = data.getStringExtra("time").split("-");

				try {
					mShowMonth.setText(times[1] + "月");
					mDate.setText(times[1] + "月" + Integer.parseInt(times[2]) + "日");
					// 刷新view
					Calendar calendar = TimeUtil.StringToCalendar(data.getStringExtra("time"));
					calendar.add(Calendar.MONTH, -1);
					mSelCalendar = calendar;
					mAdapter.setCalStartDate(mSelCalendar);
					date = data.getStringExtra("time");
					getDataForSpecificDay();
					getListForDate();
				} catch (NumberFormatException e) {
				} catch (ParseException e) {
				}
			}
		}
	}
	/*
	 * save
	 */
	List<String> keys = new ArrayList<String>();
	List<Integer> values = new ArrayList<Integer>();

	private void getDataForSpecificDay() {

		RequestParams params = new RequestParams();
		int mId = YiTouApplication.getInstance().getUser().getId();
		if (mId!=0)
			params.put("id",mId);
		params.put("date", date);
		HttpUtil.get(InterfaceInfo.BASE_URL + "/monthtrade/" + mId, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							keys.clear();
							values.clear();
							try {
								JSONObject object = response.getJSONObject("list"); // 返回null的
								Iterator<String> keysIter = object.keys();
								while (keysIter.hasNext()) {
									String keyName = keysIter.next().toString();
									keys.add(keyName);
									values.add(object.getInt(keyName));  // TODO
								}
							} catch (Exception e) {
							}
							// 刷新ui
							mAdapter.setKeys(keys);
							mAdapter.setValues(values);
							mAdapter.setCalStartDate(mSelCalendar);
						} else {
							if (response.getInt("code") == 401) {
//								showReloginDialog();
							} else {
								if (!TextUtils.isEmpty(response.getString("msg")))
									Toast.makeText(BillCalendarActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(BillCalendarActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFinish() {
				super.onFinish();
//				if (mLoadingDialog != null && mLoadingDialog.isShowing())
//					mLoadingDialog.dismiss();
			}
		}, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
				YiTouApplication.getInstance().getUserLogin().getToken()));
	}

	class BillListAdapter extends BaseAdapter {
		LayoutInflater mInflater;

		public BillListAdapter() {
			mInflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			convertView = (View) mInflater.inflate(R.layout.item_bill_calendar, null);
			TextView time = (TextView) convertView.findViewById(R.id.ibc_time);
			TextView tip = (TextView) convertView.findViewById(R.id.ibc_tip);
			TextView amount = (TextView) convertView.findViewById(R.id.ibc_amount);

			DayInfo info = datas.get(position);
			// set time
			if (info.getTime() != null) {
				time.setText(info.getTime());
			} else {
				time.setText("");
			}
			// set tip
			if (info.getDesc() != null) {
				tip.setText(info.getDesc());
			} else {
				tip.setText("");
			}
			
			// set amount
			if (info.getSymbol() == 0) {
				amount.setText("-" + StringUtils.FormatFloatFour(new BigDecimal(info.getAmount()).toPlainString()));
				amount.setTextColor(Color.parseColor("#fe9e8b"));
			} else {
				amount.setText("+" + StringUtils.FormatFloatFour(new BigDecimal(info.getAmount()).toPlainString()));
				amount.setTextColor(Color.parseColor("#85dcd9"));
			}
			return convertView;
		}

	}

	List<DayInfo> datas = new ArrayList<DayInfo>();

	private void getListForDate() {

		RequestParams params = new RequestParams();
		params.put("date", date);
		int mId = YiTouApplication.getInstance().getUser().getId();
		if (mId!=0)
			params.put("id",mId);
		HttpUtil.get(InterfaceInfo.BASE_URL + "/daytrade/" + mId, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response != null) {
					try {
						if (response.getInt("code") == 0) {
							JSONArray array = response.getJSONArray("list");
							int length = array.length();

							datas.clear();
							for (int i = 0; i < length; i++) {
								JSONObject obj = array.getJSONObject(i);
								DayInfo info = new DayInfo();

								info.setAmount(obj.getDouble("amount"));
								info.setId(obj.getLong("id"));
								info.setDesc(obj.getString("desc"));
								info.setTime(obj.getString("time"));
								info.setSymbol(obj.getInt("symbol"));
								info.setType(obj.getString("type"));
								info.setStatus(obj.getInt("status"));

								datas.add(info);
							}

							mListAdapter.notifyDataSetChanged();

						} else {
							datas.clear();
							mListAdapter.notifyDataSetChanged();
						}
					} catch (JSONException e) {
						datas.clear();
						mListAdapter.notifyDataSetChanged();
					}
				}
			}

			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				datas.clear();
				mListAdapter.notifyDataSetChanged();
			};

			@Override
			public void onFinish() {
				super.onFinish();
//				if (mLoadingDialog != null && mLoadingDialog.isShowing())
//					mLoadingDialog.dismiss();
			}
		}, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
				YiTouApplication.getInstance().getUserLogin().getToken()));
	}

	class DayInfo {
		double amount;
		long id;
		String time;
		String desc;
		int symbol;
		String type;
		int status;

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public int getSymbol() {
			return symbol;
		}

		public void setSymbol(int symbol) {
			this.symbol = symbol;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

	}
}

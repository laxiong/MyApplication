package com.laxiong.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
import com.laxiong.Utils.OnSingleClickListener;
import com.laxiong.Utils.TimeUtil;

import java.util.Calendar;
import java.util.Locale;

public class DarkThemeActivity extends BaseActivity {

	private NumberPicker mMonthSpinner;
	private NumberPicker mYearSpinner;
	private EditText mMonthSpinnerInput;
	private EditText mYearSpinnerInput;
	private Calendar mTempDate;
	// private LinearLayout mLayout;
	private TextView mSure, mCancle;
	private String year, month, day;
	private int requestCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.personal_datepicker);

		year = getIntent().getStringExtra("year");
		month = getIntent().getStringExtra("month");
		day = getIntent().getStringExtra("day");

		if (year == null) {
			Calendar instance = Calendar.getInstance(Locale.getDefault());
			String[] timenow = TimeUtil.calendarToString(instance).split("-");
			year = timenow[0];
			month = timenow[1];
			day = "1"; // 日期不做处理
		}

		requestCode = getIntent().getIntExtra("pub", 0);

		// mLayout = (LinearLayout) findViewById(R.id.personal_datepicker);
		mSure = (TextView) findViewById(R.id.sure);
		mCancle = (TextView) findViewById(R.id.cancel);
		mSure.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				if (mYearSpinner!=null&&mMonthSpinner!=null) {
					setResult(1021, new Intent().putExtra("time",
							mYearSpinner.getValue() + "-" + ((mMonthSpinner.getValue() + 1) < 10 ? "0" + (mMonthSpinner.getValue() + 1) : (mMonthSpinner.getValue() + 1)) + "-01"));
					finish();
				}
			}
		});
		mCancle.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				finish();
			}
		});

		mYearSpinner = (NumberPicker) findViewById(R.id.num1);
		mYearSpinner.setMaxValue(2060);
		mYearSpinner.setMinValue(1960);
		mYearSpinner.setValue(Integer.parseInt(year));
		mYearSpinner.setFocusable(true);
		mYearSpinner.setFocusableInTouchMode(true);
		mYearSpinnerInput = (EditText) mYearSpinner.findViewById(R.id.np__numberpicker_input);

		mMonthSpinner = (NumberPicker) findViewById(R.id.num2);
		mMonthSpinner.setMaxValue(11);
		mMonthSpinner.setMinValue(0);
		mMonthSpinner.setValue(Integer.parseInt(month) - 1);
		mMonthSpinner.setFocusable(true);
		mMonthSpinner.setFocusableInTouchMode(true);
		mMonthSpinnerInput = (EditText) mMonthSpinner.findViewById(R.id.np__numberpicker_input);
		mMonthSpinner.setDisplayedValues(
				new String[] { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" });

		mTempDate = Calendar.getInstance(Locale.getDefault());
		mTempDate.setTimeInMillis(System.currentTimeMillis());
		mTempDate.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);

		NumberPicker.OnValueChangeListener onChangeListener = new NumberPicker.OnValueChangeListener() {
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				updateInputState();
				// take care of wrapping of days and months to update greater
				// fields
				if (picker == mMonthSpinner) {
					// if (oldVal == 11 && newVal == 0) {
					// mTempDate.add(Calendar.MONTH, 1);
					// } else if (oldVal == 0 && newVal == 11) {
					// mTempDate.add(Calendar.MONTH, -1);
					// } else {
					mTempDate.add(Calendar.MONTH, newVal - oldVal);
					// }
				} else if (picker == mYearSpinner) {
					mTempDate.set(Calendar.YEAR, newVal);
				} else {
					throw new IllegalArgumentException();
				}
				// setDate(mTempDate.get(Calendar.YEAR),
				// mTempDate.get(Calendar.MONTH),
				// mTempDate.get(Calendar.DAY_OF_MONTH));
				updateSpinners();
			}
		};

		mYearSpinner.setOnValueChangedListener(onChangeListener);
		mMonthSpinner.setOnValueChangedListener(onChangeListener);

	}

	protected void updateSpinners() {
		mMonthSpinner.setWrapSelectorWheel(true);
		mYearSpinner.setWrapSelectorWheel(true);
	}

	private void updateInputState() {
		InputMethodManager inputMethodManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			if (inputMethodManager.isActive(mYearSpinnerInput)) {
				mYearSpinnerInput.clearFocus();
				inputMethodManager.hideSoftInputFromWindow(mYearSpinnerInput.getWindowToken(), 0);
			} else if (inputMethodManager.isActive(mMonthSpinnerInput)) {
				mMonthSpinnerInput.clearFocus();
				inputMethodManager.hideSoftInputFromWindow(mMonthSpinnerInput.getWindowToken(), 0);
			}
		}
	}
}

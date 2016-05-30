package com.laxiong.Calender;

import android.content.Context;
import android.widget.LinearLayout;

import com.laxiong.View.NoScrollGridView;

public class CalendarGridView extends NoScrollGridView {
	

	public CalendarGridView(Context context) {
		super(context);
		setGirdView();
	}

	private void setGirdView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		setLayoutParams(params);
		setPadding(0, 0, 0, 0);
		setNumColumns(7);// 设置每行列数
		setVerticalSpacing(2);// 垂直间隔
		setHorizontalSpacing(2);// 水平间隔
		setStretchMode(STRETCH_COLUMN_WIDTH);
		setSelector(getResources().getDrawable(android.R.color.transparent));
	}
}

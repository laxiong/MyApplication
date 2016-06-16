package com.laxiong.View;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laxiong.Basic.BackListener;
import com.laxiong.Utils.DensityUtils;
import com.gongshidai.mistGSD.R;
/**
 * Created by win7 on 2016/4/6.
 * Types CommonActionBar.java
 */
public class CommonActionBar extends RelativeLayout {
    private String text;
    private float textsize;
    private int textcolor;
    private int bgcolor;
    private FrameLayout fl_back;
    private TextView tv_title;
    private static final int DEFAULT_BG = android.R.color.transparent;
    private static final int DEFAULT_TBG = R.color.white;
    private static final int DEFAULT_TSIZE=14;

    public CommonActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray tary = context.obtainStyledAttributes(attrs, R.styleable.CommonActionBar);
        for (int i = 0; i < tary.getIndexCount(); i++) {
            int index = tary.getIndex(i);
            switch (index) {
                case R.styleable.CommonActionBar_texts:
                    text = tary.getString(R.styleable.CommonActionBar_texts);
                    break;
                case R.styleable.CommonActionBar_bgcolor:
                    bgcolor = tary.getColor(R.styleable.CommonActionBar_bgcolor, getResources().getColor(DEFAULT_BG));
                    break;
                case R.styleable.CommonActionBar_textcolor:
                    textcolor = tary.getColor(R.styleable.CommonActionBar_textcolor, getResources().getColor(DEFAULT_TBG));
                    break;
                default:
                    break;
            }
        }
        init(context);
        tary.recycle();
    }

    public void setBackListener(Activity activity) {
        fl_back.setOnClickListener(new BackListener(activity));
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_commonactionbar, this);
        this.setBackgroundColor(bgcolor);
        fl_back = (FrameLayout) this.findViewById(R.id.fl_back);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText(text);
        tv_title.setTextSize(DEFAULT_TSIZE);
        tv_title.setTextColor(textcolor);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void addClickTextRight(Context context, String msg, OnClickListener listener) {
        TextView tv = new TextView(context);
        tv.setText(msg);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setOnClickListener(listener);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0,0,15,0);
        tv.setLayoutParams(params);
        this.addView(tv);
    }
}

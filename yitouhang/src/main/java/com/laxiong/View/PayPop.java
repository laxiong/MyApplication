package com.laxiong.View;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.laxiong.Utils.DialogUtils;
import com.laxiong.yitouhang.R;

import org.w3c.dom.Text;

/**
 * Created by xiejin on 2016/4/6.
 * Types PayPop.java
 */
public class PayPop extends PopupWindow {
    private View view;

    public PayPop(final Activity context, int yuan, View.OnClickListener listener, View.OnClickListener listener2) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_inputpass, null);
        TextView et_pass = (TextView) view.findViewById(R.id.et_pass);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_input_tip = (TextView) view.findViewById(R.id.tv_input_tip);
        TextView tv_input_num = (TextView) view.findViewById(R.id.tv_input_num);
        TextView tv_forget = (TextView) view.findViewById(R.id.tv_forget);
        tv_input_tip.setText("从1T商城-兑换" + yuan + "元红包");
        tv_input_num.setText(yuan * 100 + "壹币");
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = Math.round(3 * (float) metric.widthPixels / 4);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                DialogUtils.bgalpha(context, 1.0f);
            }
        });
        tv_forget.setOnClickListener(listener2);
        tv_confirm.setOnClickListener(listener);
        this.setContentView(view);
        this.setWidth(width);
        this.setHeight(height);
        this.setFocusable(true);
        this.setBackgroundDrawable(null);
        this.setOutsideTouchable(false);
    }

    public String getExcTextPwd() {
        return ((TextView)view.findViewById(R.id.et_pass)).getText().toString();
    }
    public View getViewById(View v,int id){
        return v.findViewById(id);
    }
    public PayPop(View v,final Activity context,String title,String msg,View.OnClickListener listener){
        TextView tv_title= (TextView) v.findViewById(R.id.tv_title);
        TextView tv_msg= (TextView) v.findViewById(R.id.tv_msg);
        TextView tv_cancel= (TextView) v.findViewById(R.id.tv_cancel);
        TextView tv_confirm= (TextView) v.findViewById(R.id.tv_confirm);
        tv_title.setText(title);
        tv_msg.setText(msg);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                DialogUtils.bgalpha(context, 1.0f);
            }
        });
        tv_confirm.setOnClickListener(listener);
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = Math.round(3 * (float) metric.widthPixels / 4);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        this.setContentView(v);
        this.setWidth(width);
        this.setHeight(height);
        this.setFocusable(true);
        this.setBackgroundDrawable(null);
        this.setOutsideTouchable(false);
    }
}

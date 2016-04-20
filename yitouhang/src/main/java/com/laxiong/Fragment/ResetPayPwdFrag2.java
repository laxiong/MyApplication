package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laxiong.Activity.ResetPayPwdActivity;
import com.laxiong.yitouhang.R;

/**
 * Created by xiejin on 2016/4/20.
 * Types ResetPayPwdFrag1.java
 */

@SuppressLint("NewApi")
public class ResetPayPwdFrag2 extends Fragment implements View.OnClickListener {
    /****
     * 重置支付密码第二层
     */
    private TextView mNextPage;
    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.activity_resetpay_pswd2, null);
        initView();
        initData();
        return layout;
    }

    private void initData() {
        mNextPage.setOnClickListener(this);
    }

    private void initView() {
        mNextPage = (TextView) layout.findViewById(R.id.nextpage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextpage:
                ResetPayPwdFrag3 f3 = new ResetPayPwdFrag3();
                ((ResetPayPwdActivity) getActivity()).setFragment(f3, "flag3");
                break;
        }
    }
}

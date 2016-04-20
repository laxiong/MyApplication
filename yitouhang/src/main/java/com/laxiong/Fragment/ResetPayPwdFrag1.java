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
public class ResetPayPwdFrag1 extends Fragment implements View.OnClickListener {
    /****
     * 重置支付密码第一层
     */
    private TextView mNextPage;
    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.activity_resetpay_pswd1, null);
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
                ResetPayPwdFrag2 f2 = new ResetPayPwdFrag2();
                ((ResetPayPwdActivity) getActivity()).setFragment(f2, "flag3");
                break;
        }
    }
}

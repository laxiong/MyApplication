package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.entity.User;

@SuppressLint("NewApi")
public class AvailableBalanaceFragment extends Fragment {
    /***
     * 可用余额的碎片
     */
    private View availableView;

    private TextView money, tv_sxt, tv_gxb, tv1p, tv2p;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        availableView = inflater.inflate(R.layout.availablebalanace_frag, null);
        initView();
        initData();
        return availableView;
    }

    private void initData() {
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            money.setText(user.getYesterday().getTotal() + "");
            tv_sxt.setText(user.getYesterday().getSxt() + "元");
            tv_gxb.setText(user.getYesterday().getGxb() + "元");
            tv1p.setText(user.getYesterday().getRm() + "元");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void initView() {
        money = (TextView) availableView.findViewById(R.id.money);
        tv_sxt = (TextView) availableView.findViewById(R.id.tv_sxt);
        tv_gxb = (TextView) availableView.findViewById(R.id.tv_gxb);
        tv1p = (TextView) availableView.findViewById(R.id.tv_1p);
    }

}

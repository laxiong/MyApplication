package com.laxiong.Fragment;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.entity.Profit;
import com.laxiong.entity.User;
import com.gongshidai.mistGSD.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AccumulatedEarningsFragment extends Fragment {
    /****
     * 累计收益的碎片
     */
    private View accimlatedView;
    private TextView money, tv_gxb, tv_sxt, tv_rm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        accimlatedView = inflater.inflate(R.layout.accumlatedearnings_frag, null);
        initView();
        intiData();
        return accimlatedView;
    }

    private void intiData() {
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            money.setText(user.getProfit() + "");
            if (user.getProfit_list() != null) {
                Profit profit = user.getProfit_list();
                tv_gxb.setText(profit.getGxb() + "");
                tv_sxt.setText(profit.getSxt() + "");
                tv_rm.setText(profit.getRenmai() + "");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        intiData();
    }

    public void initView() {
        money = (TextView) accimlatedView.findViewById(R.id.money);
        tv_gxb = (TextView) accimlatedView.findViewById(R.id.tv_gxb);
        tv_sxt = (TextView) accimlatedView.findViewById(R.id.tv_sxt);
        tv_rm = (TextView) accimlatedView.findViewById(R.id.tv_rm);
    }


}

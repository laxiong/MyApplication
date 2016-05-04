package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laxiong.Activity.AssetActivity;
import com.laxiong.Activity.AvailableBalanceActivity;
import com.laxiong.Activity.InvestmentRecordActivity;
import com.laxiong.Activity.LoginActivity;
import com.laxiong.Activity.RechargeActivity;
import com.laxiong.Activity.TMallActivity;
import com.laxiong.Activity.WelCenterActivity;
import com.laxiong.Activity.WithdrawCashActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.entity.User;
import com.laxiong.yitouhang.R;

@SuppressLint("NewApi")
public class MySelfFragment extends Fragment implements OnClickListener {
    /****
     * 我的碎片
     */
    View view;
    private TextView totleMoney;
    private RelativeLayout AvailableBalance, WithdrawCash, Recharge, mRedBao, mItMall, mTouZiLayout;// 投资明细
    private TextView iv_yesterprofit;//昨日收益
    private TextView textView1;//账户余额
    private TextView togetche_tv;//在投资产

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.myself_layout, null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        totleMoney.setOnClickListener(this);
        AvailableBalance.setOnClickListener(this);
        WithdrawCash.setOnClickListener(this);
        Recharge.setOnClickListener(this);
        mTouZiLayout.setOnClickListener(this);
        mItMall.setOnClickListener(this);
        mRedBao.setOnClickListener(this);
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        iv_yesterprofit.setText(user.getYesterday().getTotal() + "元");
        textView1.setText(user.getAvailable_amount() + "元");
        togetche_tv.setText(user.getAmount() + "元");
    }

    private void initView() {
        totleMoney = (TextView) view.findViewById(R.id.togetche_tv);
        AvailableBalance = (RelativeLayout) view.findViewById(R.id.availablebalance);
        WithdrawCash = (RelativeLayout) view.findViewById(R.id.withdrawcash);
        Recharge = (RelativeLayout) view.findViewById(R.id.recharge);
        mTouZiLayout = (RelativeLayout) view.findViewById(R.id.touzi_layout);

        mItMall = (RelativeLayout) view.findViewById(R.id.rl_1t);
        mRedBao = (RelativeLayout) view.findViewById(R.id.myredbao);
        iv_yesterprofit = (TextView) view.findViewById(R.id.iv_yesterprofit);
        textView1 = (TextView) view.findViewById(R.id.textView1);
        togetche_tv = (TextView) view.findViewById(R.id.togetche_tv);
    }

    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.togetche_tv:  // 总资产
                startActivity(new Intent(getActivity(),
                        AssetActivity.class));
                break;
            case R.id.availablebalance:  // 可用余额
                startActivity(new Intent(getActivity(),
                        AvailableBalanceActivity.class));
                break;
            case R.id.withdrawcash:  // 提现
                startActivity(new Intent(getActivity(),
                        WithdrawCashActivity.class));
                break;
            case R.id.recharge:  // 充值
                startActivity(new Intent(getActivity(),
                        RechargeActivity.class));
                break;
            case R.id.touzi_layout: //投资明细
                startActivity(new Intent(getActivity(),
                        InvestmentRecordActivity.class));
                break;
            case R.id.myredbao:  //我的红包
                startActivity(new Intent(getActivity(),
                        WelCenterActivity.class));
                break;
            case R.id.rl_1t: //IT商城
                startActivity(new Intent(getActivity(),
                        TMallActivity.class));
                break;
        }
    }
}

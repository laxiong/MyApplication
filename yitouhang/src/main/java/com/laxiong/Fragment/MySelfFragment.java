package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
import com.laxiong.Activity.AssetActivity;
import com.laxiong.Activity.AtHallActivity;
import com.laxiong.Activity.AvailableBalanceActivity;
import com.laxiong.Activity.ChangeCountActivity;
import com.laxiong.Activity.InvestmentRecordActivity;
import com.laxiong.Activity.LoginActivity;
import com.laxiong.Activity.Profit_Activity;
import com.laxiong.Activity.RechargeActivity;
import com.laxiong.Activity.TMallActivity;
import com.laxiong.Activity.WebViewActivity;
import com.laxiong.Activity.WelCenterActivity;
import com.laxiong.Activity.WithdrawCashActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.LogUtils;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.entity.CalendarMonthTrade;
import com.laxiong.entity.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressLint("NewApi")
public class MySelfFragment extends Fragment implements OnClickListener {
    /****
     * 我的碎片
     */
    View view;
    private TextView totleMoney;
    private RelativeLayout AvailableBalance,WithdrawCash,Recharge;
    private LinearLayout  rl_invite,mRedBao,rl_rm, rl_hall, mItMall, mTouZiLayout;// 投资明细
    private TextView iv_yesterprofit;//昨日收益
    private TextView textView1;//账户余额
    private TextView togetche_tv;//在投资产
    private TextView tv_pnum;//红包数量
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.myself_layout, null);
        }else{
            ViewParent parent=view.getParent();
            if(parent!=null&&parent instanceof ViewGroup){
                ((ViewGroup)parent).removeView(view);
            }
        }
        initView();
        initData();
        getCalenderInfo();
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
        rl_hall.setOnClickListener(this);
        rl_rm.setOnClickListener(this);
        rl_invite.setOnClickListener(this);
        user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        iv_yesterprofit.setText(user.getYesterday().getTotal() + "元");
        textView1.setText(user.getAvailable_amount() + "元");
        togetche_tv.setText(user.getAmount() + "元");
        tv_pnum.setText("可用红包:"+user.getPacketcount());
    }
    private void initView() {
        totleMoney = (TextView) view.findViewById(R.id.togetche_tv);
        AvailableBalance = (RelativeLayout) view.findViewById(R.id.availablebalance);
        WithdrawCash = (RelativeLayout) view.findViewById(R.id.withdrawcash);
        Recharge = (RelativeLayout) view.findViewById(R.id.recharge);
        mTouZiLayout = (LinearLayout) view.findViewById(R.id.touzi_layout);
        rl_hall = (LinearLayout) view.findViewById(R.id.rl_hall);
        mItMall = (LinearLayout) view.findViewById(R.id.rl_1t);
        mRedBao = (LinearLayout) view.findViewById(R.id.myredbao);
        iv_yesterprofit = (TextView) view.findViewById(R.id.iv_yesterprofit);
        textView1 = (TextView) view.findViewById(R.id.textView1);
        togetche_tv = (TextView) view.findViewById(R.id.togetche_tv);
        rl_invite = (LinearLayout) view.findViewById(R.id.rl_invite);
        rl_rm = (LinearLayout) view.findViewById(R.id.rl_rm);
        tv_pnum= (TextView) view.findViewById(R.id.tv_pnum);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
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
            case R.id.rl_hall://活动大厅
                startActivity(new Intent(getActivity(), AtHallActivity.class));
                break;
            case R.id.withdrawcash:  // 提现
                if(user.getBankcount()==0) {
                    ToastUtil.customAlert(getActivity(),"请先绑定银行卡");
                    return;
                }
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
            case R.id.rl_invite:
                String phonnum = SpUtils.getStrValue(SpUtils.getSp(getActivity()), SpUtils.USER_KEY);
                if (user != null && !StringUtils.isBlank(phonnum)) {
                    String url = InterfaceInfo.INVITE_URL + "userId=" + user.getId() + "&mobile=" + phonnum;
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("needshare",true);
                    startActivity(intent);
                }
                break;
            case R.id.rl_rm:
                startActivity(new Intent(getActivity(), Profit_Activity.class));
                break;

        }
    }
    /***
     * 预获取日历的信息
     */
    private List<String> keys = new ArrayList<String>(); // date list
    private List<Integer> values = new ArrayList<Integer>(); // 1,2,3  {1.收益的  2.有操作的  3.有股息的}
    private void getCalenderInfo(){
        RequestParams params = new RequestParams();
        User user=YiTouApplication.getInstance().getUser();
        if(user==null){
            startActivity(new Intent(getActivity(), ChangeCountActivity.class));
            return;
        }
        int mId = user.getId();
        if (mId!=0)
            params.put("id",mId);
        HttpUtil.get(InterfaceInfo.BASE_URL + "/monthtrade/" + mId, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            keys.clear();
                            values.clear();
                            Log.i("WK", "=====返回的结果=======：" + response);
                            try {
                                JSONObject object = response.getJSONObject("list"); // 返回null的
                                Iterator<String> keysIter = object.keys();
                                while (keysIter.hasNext()) {
                                    String keyName = keysIter.next().toString();
                                    keys.add(keyName);
                                    values.add(object.getInt(keyName));  // TODO
                                }

                                CalendarMonthTrade.getInstance().setKeys(keys);
                                CalendarMonthTrade.getInstance().setValues(values);

                            } catch (Exception e) {
                            }
                        } else {
                        }
                    } catch (Exception E) {
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        }, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
                YiTouApplication.getInstance().getUserLogin().getToken()));
    }

}

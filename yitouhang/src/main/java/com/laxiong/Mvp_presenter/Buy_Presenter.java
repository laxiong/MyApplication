package com.laxiong.Mvp_presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.allinpay.appayassistex.APPayAssistEx;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.Model_Basic;
import com.laxiong.Mvp_model.Model_Basic2;
import com.laxiong.Mvp_model.OnLoadBcObjListener;
import com.laxiong.Mvp_model.Order;
import com.laxiong.Mvp_view.IViewBasic;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.PaaCreator;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.entity.OrderInfo;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.okhttp.FormEncodingBuilder;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/6/28.
 * Types Buy_Presenter.java
 * Method buyByCard:用银行卡购买产品
 * Method recharge:用银行卡充值
 */
public class Buy_Presenter implements OnLoadBcObjListener<OrderInfo> {
    private IViewCommonBack iviewback;
    private Model_Basic2<OrderInfo> mdel;
    private Activity context;

    public Buy_Presenter(IViewCommonBack iviewback) {
        this.iviewback = iviewback;
        mdel = new Model_Basic2<OrderInfo>();
    }

    @Override
    public void onSuccss(OrderInfo obj) {
        JSONObject payorder = PaaCreator.randomPaa(obj);
        APPayAssistEx.startPay(context, payorder.toString(), APPayAssistEx.MODE_DEBUG);
        iviewback.reqbackSuc("");
    }

    @Override
    public void onFailure(String msg) {
        iviewback.reqbackFail(msg, "");
    }

    public void recharge(final Activity context, String amount, String bankid) {
        this.context = context;
        mdel.setListenerObj(this);
        mdel.reqAuthGetObj(context, InterfaceInfo.TLPAY_URL + "?amount=" + amount + "&&bank_id=" + bankid, "", OrderInfo.class);
    }

    public void buyByCard(final Activity context, FormEncodingBuilder builder) {
        this.context = context;
        mdel.setListenerObj(this);
        mdel.reqAuthPostObj(context, builder, InterfaceInfo.TLBUY_URL, "", OrderInfo.class);
    }
}

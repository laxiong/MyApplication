package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewBankCard;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.loopj.android.http.RequestParams;

/**
 * Created by xiejin on 2016/4/24.
 * Types BankCard_Presenter.java
 */
public class BankCard_Presenter extends CommonReq_Presenter {
    private IViewBankCard iviewbank;
    public static final String TYPE_CODE = "code";
    public static final String TYPE_CARD="card";

    public BankCard_Presenter(IViewBankCard iviewbank) {
        super(iviewbank);
        this.iviewbank = iviewbank;
    }
    public void sendCode(Context context) {
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.CPHONE.getVal());
        params.put("phone", iviewbank.getPhone());
        aureqByPost(InterfaceInfo.CODE_URL, context, params, TYPE_CODE);
    }
    public void bindCard(Context context){

    }
}

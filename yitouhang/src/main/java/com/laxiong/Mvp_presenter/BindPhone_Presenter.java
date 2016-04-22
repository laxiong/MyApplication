package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewBindPhone;
import com.loopj.android.http.RequestParams;

/**
 * Created by xiejin on 2016/4/22.
 * Types BindPhone_Presenter.java
 */
public class BindPhone_Presenter extends CommonReq_Presenter {
    private IViewBindPhone iviewbind;
    public static final String TYPE_CODE = "code";
    public static final String TYPE_BIND = "bind";

    public BindPhone_Presenter(IViewBindPhone iviewbind) {
        super(iviewbind);
        this.iviewbind = iviewbind;
    }

    public void sendCode(Context context) {
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.CPHONE.getVal());
        params.put("phone", iviewbind.getPhone());
        aureqByPost(InterfaceInfo.CODE_URL, context, params, TYPE_CODE);
    }

    public void bindOtherPhone(Context context) {
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.CPHONE.getVal());
        params.put("token", iviewbind.getToken());
        params.put("code", iviewbind.getCode());
        params.put("phone", iviewbind.getPhone());
        aureqByPut(InterfaceInfo.USER_URL, context, params, TYPE_BIND);
    }
}

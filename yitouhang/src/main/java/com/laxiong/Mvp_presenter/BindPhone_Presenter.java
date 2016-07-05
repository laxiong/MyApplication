package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewBindPhone;
import com.loopj.android.network.RequestParams;
import com.squareup.okhttp.FormEncodingBuilder;

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
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", Constants.ReqEnum.CPHONE.getVal());
        builder.add("phone", iviewbind.getPhone());
        aureqByPost(InterfaceInfo.CODE_URL, context, builder, TYPE_CODE);
    }

    public void bindOtherPhone(Context context) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", Constants.ReqEnum.CPHONE.getVal());
        builder.add("token", iviewbind.getToken());
        builder.add("code", iviewbind.getCode());
        builder.add("phone", iviewbind.getPhone());
        aureqByPut(InterfaceInfo.USER_URL, context, builder, TYPE_BIND);
    }
}

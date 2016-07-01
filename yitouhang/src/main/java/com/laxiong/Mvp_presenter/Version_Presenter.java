package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.pm.PackageManager;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/5/12.
 * Types Version_Presenter.java
 */
public class Version_Presenter extends CommonReq_Presenter {
    private IViewCommonBack iviewcommon;

    public Version_Presenter(IViewCommonBack iviewcommon) {
        super(iviewcommon);
        this.iviewcommon = iviewcommon;
    }

    public void judgeVersion(Context context) {
        RequestParams params=new RequestParams();
        try {
            params.put("ver",context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        HttpUtil.get(InterfaceInfo.VERSION_URL,params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewcommon.reqbackSuc(response.getInt("share") + "");
                        } else {
                            iviewcommon.reqbackFail(response.getString("msg"), null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewcommon.reqbackFail(e.toString(), null);
                    }
                } else {
                    iviewcommon.reqbackFail("无响应", null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewcommon.reqbackFail(responseString, null);
            }
        });
    }

    public void loadFeedBack(String fback, Context context) {
        if (StringUtils.isBlank(fback)) {
            iviewcommon.reqbackFail("意见不能为空", "feeback");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("text", fback);
        reqCommonBackByPost(InterfaceInfo.ADVICE_URL, context, params, "feeback");
    }
}

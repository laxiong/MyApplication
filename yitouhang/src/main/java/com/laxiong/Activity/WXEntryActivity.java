package com.laxiong.Activity;


import android.content.Intent;
import android.os.Bundle;

import com.laxiong.Common.Settings;
import com.laxiong.Utils.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    // 微信登录
    private IWXAPI mIwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 微信登录
        mIwxapi = WXAPIFactory.createWXAPI(this, Settings.WX_APP_ID, false);
        mIwxapi.registerApp(Settings.WX_APP_ID);
        mIwxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        mIwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                // 分享成功调接口
                ToastUtil.customAlert(getApplicationContext(), "分享成功");
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                ToastUtil.customAlert(getApplicationContext(), "分享失败");
                if (resp.errStr != null)
                    ToastUtil.customAlert(getApplicationContext(), resp.errStr);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtil.customAlert(getApplicationContext(), "分享取消");
                break;
            default:
                break;
        }
        this.finish();
    }
}

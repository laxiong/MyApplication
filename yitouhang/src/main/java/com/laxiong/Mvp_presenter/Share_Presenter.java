package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.Model_adapter;
import com.laxiong.Mvp_model.OnLoadBcObjListener;
import com.laxiong.Mvp_view.IViewBasicObj;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.entity.ShareInfo;
import com.loopj.android.http.RequestParams;

/**
 * Created by xiejin on 2016/5/5.
 * Types Share_Presenter.java
 */
public class Share_Presenter implements OnLoadBcObjListener<ShareInfo> {
    private IViewBasicObj<ShareInfo> iviewobj;
    private Model_adapter<ShareInfo> madapter;

    public Share_Presenter(IViewBasicObj<ShareInfo> iviewobj) {
        this.iviewobj = iviewobj;
        madapter = new Model_adapter<ShareInfo>();
    }

    @Override
    public void onSuccss(ShareInfo obj) {
        iviewobj.loadObjSuc(obj);
    }

    @Override
    public void onFailure(String msg) {
        iviewobj.loadObjFail(msg);
    }

    public void loadShareData(Context context) {
        RequestParams params = new RequestParams();
        params.put("type", "invite");
        madapter.loadObjGet(InterfaceInfo.SHARE_URL, context, params, this, "weixin", ShareInfo.class);
    }
}

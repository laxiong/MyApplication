package com.laxiong.Mvp_presenter;

import android.app.DownloadManager;
import android.content.Context;

import com.laxiong.Mvp_model.InvestItem;
import com.laxiong.Mvp_model.Model_Invest;
import com.laxiong.Mvp_model.OnLoadBasicListener;
import com.laxiong.Mvp_view.IViewInvest;
import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types InvestDetail_Presenter.java
 * 用于投资明细的所有操作
 */
public class InvestDetail_Presenter implements OnLoadBasicListener<InvestItem> {
    private IViewInvest iviewinvest;
    private Model_Invest minvest;

    public InvestDetail_Presenter(IViewInvest iviewinvest) {
        this.iviewinvest = iviewinvest;
        this.minvest = new Model_Invest();
    }

    public void loadInvestView(int limit, int page, String type, Context context) {
        RequestParams params = new RequestParams();
        params.put("limit", limit);
        params.put("p", page);
        params.put("type", type);
        minvest.loadInvestList("list", context, params, this,InvestItem.class);
    }

    @Override
    public void loadOnSuccess(List<InvestItem> list) {
        iviewinvest.loadListInvest(list);
    }

    @Override
    public void loadOnFailure(String msg) {
        iviewinvest.loadListFailure(msg);
    }
}

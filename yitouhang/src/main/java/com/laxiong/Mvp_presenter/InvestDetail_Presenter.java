package com.laxiong.Mvp_presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.laxiong.Mvp_model.InvestDetail;
import com.laxiong.Mvp_model.InvestItem;
import com.laxiong.Mvp_model.Model_Invest;
import com.laxiong.Mvp_model.OnLoadBasicListener;
import com.laxiong.Mvp_model.OnLoadBcObjListener;
import com.laxiong.Mvp_view.IViewBasicObj;
import com.laxiong.Mvp_view.IViewInvest;
import com.loopj.android.http.RequestParams;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types InvestDetail_Presenter.java
 * 用于投资明细的所有操作
 */
public class InvestDetail_Presenter implements OnLoadBasicListener<InvestItem>,OnLoadBcObjListener<InvestDetail>{
    private IViewInvest iviewinvest;
    private Model_Invest minvest;
    private IViewBasicObj<InvestDetail> iviewbasic;
    private long start, end;
    private WeakReference<Handler> weak;
    private Handler handler;

    public InvestDetail_Presenter(IViewInvest iviewinvest) {
        this.iviewinvest = iviewinvest;
        this.minvest = new Model_Invest();
        weak = new WeakReference<Handler>(new Handler());
        handler=weak.get();
    }
    public InvestDetail_Presenter(IViewBasicObj<InvestDetail> iviewbasic){
        this.iviewbasic=iviewbasic;
        if(minvest==null)
            minvest=new Model_Invest();
    }
    public void loadInvestView(int limit, int page, String type, Context context) {
        start = System.currentTimeMillis();
        RequestParams params = new RequestParams();
        params.put("limit", limit);
        params.put("p", page);
        params.put("type", type);
        minvest.loadInvestList("list", context, params, this, InvestItem.class);
    }
    public void loadInvestDetail(int id,String types,Context context){
        RequestParams params=new RequestParams();
        params.put("id",id);
        params.put("type",types);
        minvest.loadDetaiil(id,"",context,params,this,InvestDetail.class);
    }
    @Override
    public void onSuccss(InvestDetail obj) {
        iviewbasic.loadObjSuc(obj);
    }
    @Override
    public void onFailure(String msg) {
        iviewbasic.loadObjFail(msg);
    }

    @Override
    public void loadOnSuccess(final List<InvestItem> list) {
        delayShow(new Runnable() {
            @Override
            public void run() {
                iviewinvest.loadListInvest(list);
            }
        });
    }

    private void delayShow(Runnable r) {
        if (r == null)
            return;
        end = System.currentTimeMillis();
        long interval = end - start > 1500 ? 0 : 1500 - (end - start);
        handler.postDelayed(r, interval);
    }

    @Override
    public void loadOnFailure(final String msg) {
        delayShow(new Runnable() {
            @Override
            public void run() {
                iviewinvest.loadListFailure(msg);
            }
        });
    }
}

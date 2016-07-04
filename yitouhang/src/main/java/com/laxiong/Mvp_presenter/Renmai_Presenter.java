package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.Model_adapter;
import com.laxiong.Mvp_model.OnLoadBasicListener;
import com.laxiong.Mvp_model.Renmai;
import com.laxiong.Mvp_view.IViewBasic;
import com.laxiong.Mvp_view.IView_Renmai;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.User;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by xiejin on 2016/5/4.
 * Types Renmai_Presenter.java
 */
public class Renmai_Presenter implements OnLoadBasicListener<Renmai> {
    private IView_Renmai iviewrm;
    private IViewBasic<Renmai> iviewbasic;
    private Model_adapter<Renmai> madapter;
    private long start, end;
    private WeakReference<Handler> weak;
    private Handler handler;

    public Renmai_Presenter(IView_Renmai iviewrm) {
        this.iviewrm = iviewrm;
    }

    public Renmai_Presenter(IViewBasic<Renmai> iviewbasic) {
        this.iviewbasic = iviewbasic;
        this.madapter = new Model_adapter<Renmai>();
        handler = new Handler();
        weak = new WeakReference<Handler>(handler);
    }

    @Override
    public void loadOnSuccess(final List<Renmai> list) {
        delayShow(new Runnable() {
            @Override
            public void run() {
                iviewbasic.loadListSuc(list);
            }
        });
    }

    private void delayShow(Runnable r) {
        if (r == null)
            return;
        end = System.currentTimeMillis();
        long interval = end - start > 2000 ? 0 : 2000 - (end - start);
        handler.postDelayed(r, interval);
    }

    @Override
    public void loadOnFailure(final String msg) {
        delayShow(new Runnable() {
            @Override
            public void run() {
                iviewbasic.loadListFail(msg);
            }
        });
    }

    public void loadRmDetail(int page, int pagesize, String type, Context context) {
        start = System.currentTimeMillis();
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        String url = InterfaceInfo.FRIENDS_URL + "?id=" + user.getId() + "&&type=" + type + "&&page=" + page + "&&pageSize=" + pagesize;
        madapter.loadListGet(url, context, this, Renmai.class);
//        madapter.loadListGet(InterfaceInfo.FRIENDS_URL + "/" + user.getId(), context, params, this, Renmai.class);
    }

    public void loadRmNum(final Context context) {
        String authroi = CommonReq.getAuthori(context);
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return;
        }
        if (StringUtils.isBlank(authroi))
            return;
        HttpUtil2.get(InterfaceInfo.RMNUM_URL + "/" + user.getId(), new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewrm.loadRmNumSuc(response.getInt("one"), response.getInt("two"));
                        } else {
                            if (response.getInt("code") == 401)
                                CommonReq.showReLoginDialog(context);
                            else
                                iviewrm.loadRmNumFail(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewrm.loadRmNumFail(e.toString());
                    }
                } else {
                    iviewrm.loadRmNumFail("无响应");
                }
            }

            @Override
            public void onFailure(String msg) {
                iviewrm.loadRmNumFail(msg);
            }
        }, authroi);
    }
}

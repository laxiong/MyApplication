package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Mvp_model.Model_Yibi;
import com.laxiong.Mvp_model.OnLoadBasicListener;
import com.laxiong.Mvp_model.Score;
import com.laxiong.Mvp_view.IViewYibi;
import com.laxiong.entity.User;
import com.loopj.android.http.RequestParams;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by xiejin on 2016/4/29.
 * Types YibiDetail_Presenter.java
 */
public class YibiDetail_Presenter implements OnLoadBasicListener<Score> {
    private IViewYibi iviewyibi;
    private Model_Yibi<Score> myibi;
    private long start, end;
    private WeakReference<Handler> weak;
    private Handler handler;

    public YibiDetail_Presenter(IViewYibi iviewyibi) {
        this.iviewyibi = iviewyibi;
        myibi = new Model_Yibi();
        weak = new WeakReference<Handler>(new Handler());
        handler=weak.get();
    }

    public void loadYibiOutput(int page, int pagesize, Context context) {
        start = System.currentTimeMillis();
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("type", "used");
        params.put("page", page);
        params.put("pageSize", pagesize);
        myibi.loadYibiOutput(user.getId(), params, context, this);
    }

    public void loadYibiInput(int page, int pagesize, Context context) {
        start = System.currentTimeMillis();
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("type", "use");
        params.put("page", page);
        params.put("pageSize", pagesize);
        myibi.loadYibiInput(user.getId(), params, context, this);
    }

    @Override
    public void loadOnSuccess(final List<Score> list) {
        delayShow(new Runnable() {
            @Override
            public void run() {
                iviewyibi.loadListSuc(list);
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
                iviewyibi.loadListFailure(msg);
            }
        });
    }
}

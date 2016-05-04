package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Mvp_model.AtHall;
import com.laxiong.Mvp_model.Model_Hall;
import com.laxiong.Mvp_model.OnLoadBasicListener;
import com.laxiong.Mvp_view.IViewBasic;

import java.util.List;

/**
 * Created by xiejin on 2016/5/4.
 * Types AtHall_Presenter.java
 */
public class AtHall_Presenter implements OnLoadBasicListener<AtHall> {
    private IViewBasic<AtHall> iviewbasic;
    private Model_Hall mhall;

    public AtHall_Presenter(IViewBasic<AtHall> iviewbasic) {
        this.iviewbasic = iviewbasic;
        mhall = new Model_Hall();
    }

    public void loadListView(Context context) {
        mhall.loadHallList(context, this);
    }

    @Override
    public void loadOnSuccess(List<AtHall> list) {
        iviewbasic.loadListSuc(list);
    }

    @Override
    public void loadOnFailure(String msg) {
        iviewbasic.loadListFail(msg);
    }
}

package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Common.Constants;
import com.laxiong.Mvp_model.Model_Order;
import com.laxiong.Mvp_model.Order;
import com.laxiong.Mvp_view.IViewOrder;
import com.loopj.android.network.RequestParams;

import java.util.List;

/**
 * Created by xiejin on 2016/4/28.
 * Types Order_Presenter.java
 */
public class Order_Presenter implements Model_Order.OnLoadOrder {
    private IViewOrder ivieworder;
    private Model_Order morder;

    public Order_Presenter(IViewOrder ivieworder) {
        this.ivieworder = ivieworder;
        this.morder = new Model_Order();
    }

    public void loadMyOrder(Context context) {
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.ORDER.getVal());
        morder.loadOrderlist(context, params, this);
    }

    @Override
    public void onSuccess(List<Order> list) {
        ivieworder.loadListOrder(list);
    }

    @Override
    public void onFailure(String msg) {
        ivieworder.loadListFailure(msg);
    }
}

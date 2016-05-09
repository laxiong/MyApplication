package com.laxiong.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.Order;
import com.laxiong.Mvp_presenter.Order_Presenter;
import com.laxiong.Mvp_view.IViewOrder;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;
import com.laxiong.yitouhang.R;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends BaseActivity implements IViewOrder {
    private ListView lvlist;
    private CommonActionBar actionbar;
    private ReuseAdapter<Order> adapter;
    private List<Order> list;
    private Order_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        initData();
    }

    public void initView() {
        lvlist = (ListView) findViewById(R.id.lvlist);
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
    }

    public void initData() {
        actionbar.setBackListener(this);
        list = new ArrayList<Order>();
        adapter = new ReuseAdapter<Order>(this, list, R.layout.item_order) {
            @Override
            public void convert(ViewHolder viewholder, Order item) {
                viewholder.setText(R.id.tv_title, item.getTitle());
                viewholder.setText(R.id.tv_time, item.getAdd_time());
                viewholder.setText(R.id.tv_yibi, item.getRental() + "壹币");
                viewholder.setText(R.id.tv_status, item.getEvent_amount() == 1 ? "交易成功" : "交易失败");
            }
        };
        lvlist.setAdapter(adapter);
        presenter = new Order_Presenter(this);
        presenter.loadMyOrder(this);
    }

    @Override
    public void loadListOrder(List<Order> listdata) {
        if (listdata == null || listdata.size() == 0) {
            if (list == null || list.size() == 0)
                lvlist.setEmptyView(findViewById(R.id.ll_empty));
        } else {
            list.addAll(listdata);
            adapter.setList(list);
        }
    }

    @Override
    public void loadListFailure(String msg) {
        ToastUtil.customAlert(this, msg);
    }
}

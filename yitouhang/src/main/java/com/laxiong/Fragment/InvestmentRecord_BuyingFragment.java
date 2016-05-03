package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.InvestItem;
import com.laxiong.Mvp_presenter.InvestDetail_Presenter;
import com.laxiong.Mvp_view.IViewInvest;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.FinancingListView;
import com.laxiong.yitouhang.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/4/6.
 */
@SuppressLint("NewApi")
public class InvestmentRecord_BuyingFragment extends Fragment implements IViewInvest {
    private View view;
    private FinancingListView lvlist;
    private InvestDetail_Presenter presenter;
    private List<InvestItem> list;
    private ReuseAdapter<InvestItem> adapter;
    public static final int LIMIT = 10;
    public int pagenow = 1;
    public boolean flag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.withdraw_listview, null);
        initView();
        initData();
        return view;
    }

    public void initListener() {
        lvlist.setOnRefreshListener(new FinancingListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                if (lvlist != null) lvlist.completeRefresh();
            }

            @Override
            public void onLoadingMore() {
                if (!flag) {
                    ToastUtil.customAlert(getActivity(), "没数据了");
                    return;
                }
                presenter.loadInvestView(LIMIT, ++pagenow, "buy", getActivity());
            }
        });
    }

            public void initView() {
                lvlist = (FinancingListView) view.findViewById(R.id.listview);
            }

            public void initData() {
                list = new ArrayList<InvestItem>();
                adapter = new ReuseAdapter<InvestItem>(getActivity(), list, R.layout.investmentrecord_buying_item) {
                    @Override
                    public void convert(ViewHolder viewholder, InvestItem item) {
                        viewholder.setText(R.id.name, item.getTitle());
                        viewholder.setText(R.id.time, item.getAdd_time());
                        viewholder.setText(R.id.money_type, item.getAmount());
                        viewholder.setText(R.id.notif_msg, item.getMark());
                    }
                };
                lvlist.setAdapter(adapter);
                presenter = new InvestDetail_Presenter(this);
                presenter.loadInvestView(LIMIT, ++pagenow, "buy", getActivity());
            }

            @Override
            public void loadListInvest(List<InvestItem> listdata) {
                if (lvlist != null) lvlist.completeRefresh();
                if (list != null && listdata != null) {
                    this.list.addAll(listdata);
                    adapter.setList(list);
                } else {
                    flag = false;
                    ToastUtil.customAlert(getActivity(), "没数据了");
                }
            }

            @Override
            public void loadListFailure(String msg) {
                ToastUtil.customAlert(getActivity(), msg);
            }

        }

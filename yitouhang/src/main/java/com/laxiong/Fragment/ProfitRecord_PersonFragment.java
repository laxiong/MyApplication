package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.InvestItem;
import com.laxiong.Mvp_presenter.InvestDetail_Presenter;
import com.laxiong.Mvp_view.IViewInvest;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.FinancingListView;
import com.gongshidai.mistGSD.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/4/7.
 */
@SuppressLint("NewApi")
public class ProfitRecord_PersonFragment extends Fragment implements IViewInvest {
    /**
     * 收益记录--人脉
     */
    private View mView;
    private FinancingListView lvlist;
    private InvestDetail_Presenter presenter;
    private List<InvestItem> list;
    private ReuseAdapter<InvestItem> adapter;
    public static final int LIMIT = 10;
    public int pagenow = 1;
    public boolean flag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.withdraw_listview, null);
        initView();
        initData();
        return mView;
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
        presenter.loadInvestView(LIMIT, ++pagenow, "rm", getActivity());
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
                } else {
                    presenter.loadInvestView(LIMIT, ++pagenow, "rm", getActivity());
                }
            }
        });
    }

    @Override
    public void loadListInvest(List<InvestItem> listdata) {
        if (lvlist != null) lvlist.completeRefresh();
        if (list != null && listdata != null&&listdata.size()!=0) {
            this.list.addAll(listdata);
            adapter.setList(list);
        } else {
            flag = false;
        }
        if(list==null||list.size()==0)
            lvlist.setEmptyView(mView.findViewById(R.id.ll_empty));
    }

    @Override
    public void loadListFailure(String msg) {
        ToastUtil.customAlert(getActivity(), msg);
    }

    private void initView() {
        lvlist = (FinancingListView) mView.findViewById(R.id.listview);
        lvlist.setAdapter(adapter);
    }
}

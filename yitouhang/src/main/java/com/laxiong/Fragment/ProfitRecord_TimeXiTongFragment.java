package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carfriend.mistCF.R;
import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.InvestItem;
import com.laxiong.Mvp_presenter.InvestDetail_Presenter;
import com.laxiong.Mvp_view.IViewInvest;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.FinancingListView;
import com.laxiong.View.WaitPgView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/4/7.
 */
@SuppressLint("NewApi")
public class ProfitRecord_TimeXiTongFragment extends Fragment implements IViewInvest {
    /**
     * 收益记录--时息通
     */
    private View mView ;
    private FinancingListView lvlist;
    private InvestDetail_Presenter presenter;
    private List<InvestItem> list;
    private ReuseAdapter<InvestItem> adapter;
    public static final int LIMIT = 10;
    public int pagenow = 1;
    public boolean flag = true;
    private WaitPgView wp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView =inflater.inflate(R.layout.withdraw_listview,null);
        initView();
        initData();
        initListener();
        return mView;
    }
    public void showLoadView(boolean flag) {
        wp= (WaitPgView)mView.findViewById(R.id.wp_load);
        wp.setVisibility(flag ? View.VISIBLE : View.GONE);
    }
    public void initData() {
        list = new ArrayList<InvestItem>();
        lvlist.setHeaderDividersEnabled(false);
        lvlist.setFooterDividersEnabled(false);
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
        showLoadView(true);
        presenter = new InvestDetail_Presenter(this);
        presenter.loadInvestView(LIMIT, ++pagenow, "sxt", getActivity());
    }
    public void initListener() {
        lvlist.setOnRefreshListener(new FinancingListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                if (lvlist != null) {
                    presenter.loadInvestView(LIMIT,pagenow=1, "sxt", getActivity());
                    lvlist.setLoadMoreEnabled(true);
                }
            }

            @Override
            public void onLoadingMore() {
                if (!flag) {
                    return;
                } else {
                    presenter.loadInvestView(LIMIT, ++pagenow, "sxt", getActivity());
                }
            }
        });
    }
    @Override
    public void loadListInvest(List<InvestItem> listdata) {
        showLoadView(false);
        if (lvlist != null) lvlist.completeRefresh();
        if (list != null&&listdata!=null&&listdata.size()!=0) {
            if(pagenow==1)this.list.clear();
            this.list.addAll(listdata);
            adapter.setList(list);
        } else {
            flag = false;
            lvlist.setLoadMoreEnabled(false);
        }
        if(list==null||list.size()==0)
            lvlist.setEmptyView(mView.findViewById(R.id.ll_empty));
    }

    @Override
    public void loadListFailure(String msg) {
        showLoadView(false);
        if (lvlist != null) lvlist.completeRefresh();
        ToastUtil.customAlert(getActivity(), msg);
    }
    private void initView(){
        lvlist = (FinancingListView)mView.findViewById(R.id.listview);
        lvlist.setAdapter(adapter);
    }
}

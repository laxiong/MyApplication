package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.Renmai;
import com.laxiong.Mvp_presenter.Renmai_Presenter;
import com.laxiong.Mvp_view.IViewBasic;
import com.laxiong.Mvp_view.IView_Renmai;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.FinancingListView;
import com.carfriend.mistCF.R;
import com.laxiong.View.WaitPgView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiejin on 2016/5/4.
 * Types RmFragment.java
 */
@SuppressLint("NewApi")
public class RmFragment extends ListFragment implements IViewBasic<Renmai> {
    private View layout;
    private ReuseAdapter<Renmai> adapter;
    private Renmai_Presenter presenter;
    private List<Renmai> list;
    private int page = 1;
    private boolean flag = true;
    private static final int PAGESIZE = 10;
    private String type;
    private WaitPgView wp;
    private FinancingListView lvlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layout == null) {
            layout = inflater.inflate(R.layout.fragment_rmdetail, null);
        } else {
            ViewParent parent = layout.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(layout);
            }
        }
        initData();
        return layout;
    }
    public void showLoadView(boolean flag) {
        wp= (WaitPgView)layout.findViewById(R.id.wp_load);
        wp.setVisibility(flag ? View.VISIBLE : View.GONE);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        lvlist = (FinancingListView) getListView();
        lvlist.setHeaderDividersEnabled(false);
        lvlist.setFooterDividersEnabled(false);
        lvlist.setOnRefreshListener(new FinancingListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                presenter.loadRmDetail(page = 1, PAGESIZE, type, getActivity());
                lvlist.setLoadMoreEnabled(true);
            }

            @Override
            public void onLoadingMore() {
                if (!flag) {
                    return;
                }
                presenter.loadRmDetail(++page, PAGESIZE, type, getActivity());
            }
        });
    }

    @Override
    public void loadListSuc(List<Renmai> listdata) {
        showLoadView(false);
        lvlist.completeRefresh();
        if (listdata == null || listdata.size() == 0) {
            flag = false;
            lvlist.setLoadMoreEnabled(false);
            if (list == null || list.size() == 0)
                lvlist.setEmptyView(layout.findViewById(R.id.ll_empty));
            return;
        }
        if(page==1)this.list.clear();
        list.addAll(listdata);
        adapter.setList(list);
    }

    @Override
    public void loadListFail(String msg) {
        showLoadView(false);
        ToastUtil.customAlert(getActivity(), msg);
    }

    private void initData() {
        presenter = new Renmai_Presenter(this);
        list = new ArrayList<Renmai>();
        adapter = new ReuseAdapter<Renmai>(getActivity(), list, R.layout.item_rmdetail) {
            @Override
            public void convert(ViewHolder helper, Renmai item) {
                helper.setText(R.id.tv_phone, item.getName());
                helper.setText(R.id.tv_itdetail, item.getInvestment() == 0 ? "未投资" : ("投资情况: " + item.getInvestment() + "笔"));
                helper.setText(R.id.tv_profit, "已获收益: " + item.getInterest() + "元");
                helper.setText(R.id.tv_time, "加入时间: " + item.getDate());
            }
        };
        setListAdapter(adapter);
        showLoadView(true);
        Bundle bundle = this.getArguments();
        type = bundle.getString("type");
        if (!StringUtils.isBlank(type))
            presenter.loadRmDetail(++page, PAGESIZE, type, getActivity());
    }

}

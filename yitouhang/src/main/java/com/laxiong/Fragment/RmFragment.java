package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import com.laxiong.yitouhang.R;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        final FinancingListView lvlist = (FinancingListView) getListView();
        lvlist.setOnRefreshListener(new FinancingListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                lvlist.completeRefresh();
            }

            @Override
            public void onLoadingMore() {
                if (!flag) {
                    ToastUtil.customAlert(getActivity(), "没数据了");
                    return;
                }
                presenter.loadRmDetail(++page, PAGESIZE, type, getActivity());
            }
        });
    }

    @Override
    public void loadListSuc(List<Renmai> listdata) {
        if (listdata == null || listdata.size() == 0) {
            flag = false;
            if (list == null || list.size() == 0)
                getListView().setEmptyView(layout.findViewById(R.id.ll_empty));
            return;
        }
        list.addAll(listdata);
        adapter.setList(list);
    }

    @Override
    public void loadListFail(String msg) {
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
        Bundle bundle = this.getArguments();
        type = bundle.getString("type");
        if (!StringUtils.isBlank(type))
            presenter.loadRmDetail(++page, PAGESIZE, type, getActivity());
    }

}

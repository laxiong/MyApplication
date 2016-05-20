package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.laxiong.Activity.RecordDetailActivity;
import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.InvestItem;
import com.laxiong.Mvp_presenter.InvestDetail_Presenter;
import com.laxiong.Mvp_view.IViewInvest;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.FinancingListView;
import com.gongshidai.mistGSD.R;
import com.laxiong.View.WaitPgView;

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
    private  WaitPgView wp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.withdraw_listview, null);
        initView();
        initData();
        initListener();
        return view;
    }

    public void showLoadView(boolean flag) {
        wp= (WaitPgView) view.findViewById(R.id.wp_load);
        wp.setVisibility(flag?View.VISIBLE:View.GONE);
    }
    public void initListener() {
        lvlist.setOnRefreshListener(new FinancingListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                if (lvlist != null) {
                    presenter.loadInvestView(LIMIT,pagenow=1,"buy",getActivity());
                    lvlist.setLoadMoreEnabled(true);
                }
            }

            @Override
            public void onLoadingMore() {
                if (!flag) {
                    return;
                }
                presenter.loadInvestView(LIMIT, ++pagenow, "buy", getActivity());
            }
        });
        lvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), RecordDetailActivity.class);
                if(position!=-1) {
                    InvestItem item=list.get(position-1);
                    intent.putExtra("title",item.getTitle());
                    intent.putExtra("id", item.getId());
                    intent.putExtra("types",item.getTypes());
                    startActivity(intent);
                }
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
                showLoadView(true);
                presenter = new InvestDetail_Presenter(this);
                presenter.loadInvestView(LIMIT, ++pagenow, "buy", getActivity());
            }

            @Override
            public void loadListInvest(List<InvestItem> listdata) {
                showLoadView(false);
                if (lvlist != null) lvlist.completeRefresh();
                if (list != null && listdata != null&&listdata.size()!=0) {
                    if(pagenow==1)this.list.clear();
                    this.list.addAll(listdata);
                    adapter.setList(list);
                } else {
                    flag = false;
                    lvlist.setLoadMoreEnabled(false);
                }
                if(list==null||list.size()==0)
                    lvlist.setEmptyView(view.findViewById(R.id.ll_empty));
            }

            @Override
            public void loadListFailure(String msg) {
                showLoadView(false);
                if (lvlist != null) lvlist.completeRefresh();
                ToastUtil.customAlert(getActivity(), msg);
            }

        }

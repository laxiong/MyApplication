package com.laxiong.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.Order;
import com.laxiong.Mvp_model.Score;
import com.laxiong.Mvp_presenter.YibiDetail_Presenter;
import com.laxiong.Mvp_view.IViewYibi;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;
import com.laxiong.View.FinancingListView;
import com.carfriend.mistCF.R;
import com.laxiong.View.WaitPgView;

import java.util.ArrayList;
import java.util.List;

public class YibiDetailActivity extends BaseActivity implements IViewYibi {
    private CommonActionBar actionbar;
    private FinancingListView lvlist;
    private YibiDetail_Presenter presenter;
    private List<Score> list;
    private int pagenum = 1;
    private static final int PAGESIZE = 10;
    private ReuseAdapter<Score> adapter;
    private boolean flag = true;
    private boolean isshouru = true;
    private WaitPgView wp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yibi_detail);
        initView();
        initData();
        initListener();
    }
    public void showLoadView(boolean flag) {
        wp= (WaitPgView)findViewById(R.id.wp_load);
        wp.setVisibility(flag ? View.VISIBLE : View.GONE);
    }
    private void initListener() {
        lvlist.setOnRefreshListener(new FinancingListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                if (lvlist != null) {
                    if (isshouru) {
                        presenter.loadYibiInput(pagenum=1, PAGESIZE, YibiDetailActivity.this);
                    } else {
                        presenter.loadYibiOutput(pagenum=1, PAGESIZE, YibiDetailActivity.this);
                    }
                }
            }

            @Override
            public void onLoadingMore() {
                if (!flag) {
                    return;
                }
                if (isshouru) {
                    presenter.loadYibiInput(++pagenum, PAGESIZE, YibiDetailActivity.this);
                } else {
                    presenter.loadYibiOutput(++pagenum, PAGESIZE, YibiDetailActivity.this);
                }
            }
        });
    }

    @Override
    public void loadListSuc(List<Score> listdata) {
        showLoadView(false);
        if (lvlist != null) lvlist.completeRefresh();
        if (list != null && listdata != null&&listdata.size()!=0) {
            if(pagenum==1)this.list.clear();
            this.list.addAll(listdata);
            adapter.setList(list);
        } else {
            flag = false;
            lvlist.setLoadMoreEnabled(false);
        }
        if(list==null||list.size()==0)
            lvlist.setEmptyView(findViewById(R.id.ll_empty));
    }

    @Override
    public void loadListFailure(String msg) {
        ToastUtil.customAlert(this, msg);
        if (lvlist != null) lvlist.completeRefresh();
        showLoadView(false);
    }

    private void initData() {
        presenter = new YibiDetail_Presenter(this);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        isshouru = "shouru".equals(type) ? true : false;
        actionbar.setTitle(isshouru ? "收入" : "支出");
        list = new ArrayList<Score>();
        showLoadView(true);
        adapter = new ReuseAdapter<Score>(this, list, R.layout.item_order) {
            @Override
            public void convert(ViewHolder helper, Score item) {
                helper.setText(R.id.tv_title, item.getDesc());
                helper.setText(R.id.tv_time, item.getDates());
                helper.setText(R.id.tv_yibi,(isshouru ? "+" : "-") + item.getScore() + "壹币");
                helper.setText(R.id.tv_status, item.getStatus() == 1 ? "交易成功" : "交易失败");
                TextView tv_yibi = helper.getView(R.id.tv_yibi);
                tv_yibi.setTextColor(getResources().getColor(isshouru ? R.color.money_org : R.color.money_blue));
            }
        };
        lvlist.setAdapter(adapter);
        if (isshouru) {
            presenter.loadYibiInput(++pagenum, PAGESIZE, this);
        } else {
            presenter.loadYibiOutput(++pagenum, PAGESIZE, this);
        }
    }

    private void initView() {
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        actionbar.setBackListener(this);
        lvlist = (FinancingListView) findViewById(R.id.lvlist);
    }
}

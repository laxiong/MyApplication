package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongshidai.mistGSD.R;
import com.laxiong.Adapter.RedPaper;
import com.laxiong.Adapter.RedPaperAdapter;
import com.laxiong.Basic.BackListener;
import com.laxiong.Mvp_presenter.WelCenter_Presenter;
import com.laxiong.Mvp_view.IViewWelcenter;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;
import com.laxiong.View.WaitPgView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win7 on 2016/4/6.
 * Types WelCenterActivity.java
 */
public class WelCenterActivity extends BaseActivity implements IViewWelcenter {
    private static final String TAG = "WelCenterActivity";
    private FrameLayout fl_back;
    private CommonActionBar actionbar;
    private TextView tv_explain;
    private ListView lvlist;
    private Intent intent_select;
    private ArrayList<RedPaper> listdata, listselect;
    private WelCenter_Presenter presenter;
    private RedPaperAdapter adapter;
    private int totalpage = -1, pagenow = -1;
    private LinearLayout ll_bottom;
    private boolean isbottom;
    private boolean flag;
    private WaitPgView wp;
    private boolean isall;
    private double money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcenter);
        initView();
        initData();
        initListener();
    }

    public void showLoadView(boolean flag) {
        wp = (WaitPgView) findViewById(R.id.wp_load);
        wp.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPageNow(int pagenow) {
        this.pagenow = pagenow;
    }

    private void initView() {
        presenter = new WelCenter_Presenter(this);
        fl_back = (FrameLayout) findViewById(R.id.fl_back);
        fl_back.setOnClickListener(new BackListener(this));
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        tv_explain = (TextView) findViewById(R.id.tv_explain);
        lvlist = (ListView) findViewById(R.id.lvlist);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
    }

    @Override
    public void setEmptyView() {
        showLoadView(false);
        if (lvlist == null)
            return;
        lvlist.setEmptyView(findViewById(R.id.ll_empty));
    }

    @Override
    public void setMaxPage(int num) {
        this.totalpage = num;
    }

    //初始化数据包括初始化红包数据
    private void initData() {
        intent_select = getIntent();
        actionbar.setTitle("福利中心");
        isall = getIntent() == null || getIntent().getBooleanExtra("isAll", true);
        showLoadView(true);
        if (isall) presenter.loadAll(this);
        else {
            money = getIntent().getDoubleExtra("money", 0.0);
            flag = getIntent() != null && getIntent().getBooleanExtra("used", false);
            listselect = new ArrayList<RedPaper>();
            listdata = new ArrayList<RedPaper>();
            presenter.loadListData(pagenow = 1, flag, this);
        }
    }

    @Override
    public void loadAll(List<RedPaper> list) {
        showLoadView(false);
        if (adapter == null) {
            listdata = new ArrayList<RedPaper>();
            listdata.addAll(list);
            adapter = new RedPaperAdapter(this, listdata);
            lvlist.setAdapter(adapter);
            return;
        }
        listdata.addAll(list);
        adapter.setList(listdata);
    }

    @Override
    public void reqListFailure(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    public void reUpdateUsed(boolean isused, List<RedPaper> list) {
        if (!isused) {
            for (RedPaper paper : list) {
                if (money < paper.getMin()) {
                    if (!paper.isSelected())
                        paper.setIs_used(RedPaper.UsetypeEnum.USED.getVal());
                } else {
                    paper.setIs_used(RedPaper.UsetypeEnum.UNUSED.getVal());
                }
            }
        }
    }

    @Override
    public void addList(boolean init, boolean isused, List<RedPaper> list) {
        showLoadView(false);
        reUpdateUsed(isused, list);
        if (adapter == null) {
            listdata = new ArrayList<RedPaper>();
            listdata.addAll(list);
            adapter = new RedPaperAdapter(this, listdata);
            lvlist.setAdapter(adapter);
            if (!flag)
                presenter.initUnusedFootView(this);
            else
                presenter.initUsedFootView(this);
            return;
        }
        if (init)
            listdata.clear();
        listdata.addAll(list);
        adapter.setList(listdata);
        if (isused)
            presenter.initUsedFootView(this);
        else
            presenter.initUnusedFootView(this);
        if (init && isused)
            lvlist.setSelection(0);
    }

    @Override
    public int getMaxPage() {
        return totalpage;
    }

    @Override
    public void addFootView(View view) {
        ll_bottom.removeAllViews();
        ll_bottom.addView(view);
        ll_bottom.setVisibility(View.INVISIBLE);
        isbottom = false;
    }

    @Override
    public int getPageNow() {
        return pagenow;
    }

    @Override
    public void setBottomTipVisibily(boolean flag) {
        ll_bottom.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
    }

    private void initListener() {
        actionbar.setBackListener(this);
        tv_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelCenterActivity.this, RpExplainActivity.class);
                startActivity(intent);
            }
        });
        lvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isall)
                    return;
                if (position != -1) {
                    RedPaper item = listdata.get(position);
                    if (item.getIs_used() == RedPaper.UsetypeEnum.UNUSED.getVal()) {
                        ImageView iv_select = (ImageView) view.findViewById(R.id.iv_select);
                        item.setSelected(!item.isSelected());
                        if (item.isSelected()) {
                            listselect.add(item);
                            money -= item.getMin();
                            reUpdateUsed(false, listdata);
                            adapter.notifyDataSetChanged();
                        } else {
                            listselect.remove(item);
                            money += item.getMin();
                            reUpdateUsed(false, listdata);
                            adapter.notifyDataSetChanged();
                        }
                        iv_select.setImageResource(item.isSelected() ? R.drawable.choose : 0);
                    }
                }
            }
        });
        if (intent_select != null && intent_select.getBooleanExtra("isBuying", false)) {
            actionbar.addClickTextRight(this, "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listselect.size() == 0)
                        Toast.makeText(WelCenterActivity.this, "请先选择", Toast.LENGTH_SHORT).show();
                    else {
                        intent_select.putParcelableArrayListExtra("data", listselect);
                        WelCenterActivity.this.setResult(RESULT_OK, intent_select);
                        finish();
                    }
                }
            });
        }
        lvlist.setOnScrollListener(presenter.getScrollListener());
    }

}

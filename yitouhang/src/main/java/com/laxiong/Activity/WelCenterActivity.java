package com.laxiong.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Adapter.RedPaper;
import com.laxiong.Adapter.RedPaperAdapter;
import com.laxiong.Basic.BackListener;
import com.laxiong.Mvp_presenter.WelCenter_Presenter;
import com.laxiong.Mvp_view.IViewWelcenter;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;
import com.laxiong.yitouhang.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcenter);
        initView();
        initData();
        initListener();
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
    public void setEmptyView(View emptyview) {
        if (lvlist == null)
            return;
        ViewGroup parent = (ViewGroup) lvlist.getParent();
        parent.addView(emptyview, 2);
        lvlist.setEmptyView(emptyview);
    }

    @Override
    public void setMaxPage(int num) {
        this.totalpage = num;
    }

    //初始化数据包括初始化红包数据
    private void initData() {
        intent_select = getIntent();
        actionbar.setTitle("福利中心");
        flag = getIntent() != null && getIntent().getBooleanExtra("used", false);
        listselect = new ArrayList<RedPaper>();
        listdata = new ArrayList<RedPaper>();
        presenter.loadListData(pagenow = 1, flag, this);
    }

    @Override
    public void reqListFailure(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    @Override
    public void addList(boolean init, boolean isused, List<RedPaper> list) {
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
//        listdata.addAll(presenter.reqRedPaperList(init ? (pagenow = 1) : ++pagenow, isused));
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
                if (position != -1) {
                    RedPaper item = listdata.get(position);
                    if (item.getIs_used() == RedPaper.UsetypeEnum.UNUSED.getVal()) {
                        ImageView iv_select = (ImageView) view.findViewById(R.id.iv_select);
                        listselect.add(item);
                        item.setSelected(!item.isSelected());
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
                        WelCenterActivity.this.setResult(RESULT_OK);
                    }
                }
            });
        }
        lvlist.setOnScrollListener(presenter.getScrollListener());
    }

}

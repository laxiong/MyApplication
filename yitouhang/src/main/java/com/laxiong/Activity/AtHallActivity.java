package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.AtHall;
import com.laxiong.Mvp_presenter.AtHall_Presenter;
import com.laxiong.Mvp_view.IViewBasic;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;
import com.laxiong.yitouhang.R;

import java.util.ArrayList;
import java.util.List;

public class AtHallActivity extends BaseActivity implements IViewBasic<AtHall> {
    private CommonActionBar actionbar;
    private ListView lvlist;
    private ReuseAdapter<AtHall> adapter;
    private AtHall_Presenter presenter;
    private List<AtHall> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_hall);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        actionbar.setBackListener(this);
        lvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == -1)
                    return;
                AtHall hall = list.get(position);
                Intent intent = new Intent(AtHallActivity.this, WebViewActivity.class);
                intent.putExtra("url", hall.getHref());
                startActivity(intent);
                return;
            }
        });
    }

    private void initData() {
        presenter = new AtHall_Presenter(this);
        list = new ArrayList<AtHall>();
        adapter = new ReuseAdapter<AtHall>(this, list, R.layout.item_athall) {
            @Override
            public void convert(ViewHolder helper, AtHall item) {
                Glide.with(AtHallActivity.this).load(item.getImageurl())
                        .placeholder(R.drawable.gongshi_mr).crossFade().into((ImageView) helper.getView(R.id.iv_top));
                helper.setText(R.id.tv_name, item.getTitle());
                helper.setText(R.id.tv_time, item.getBegin_date());
            }
        };
        lvlist.setAdapter(adapter);
        presenter.loadListView(this);
    }

    @Override
    public void loadListSuc(List<AtHall> listdata) {
        if (listdata == null || listdata.size() == 0) {
            if(list==null||list.size()==0)
                lvlist.setEmptyView(findViewById(R.id.ll_empty));
            return;
        }
        this.list.addAll(listdata);
        adapter.setList(list);
    }

    @Override
    public void loadListFail(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    private void initView() {
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        lvlist = (ListView) findViewById(R.id.lvlist);
    }
}

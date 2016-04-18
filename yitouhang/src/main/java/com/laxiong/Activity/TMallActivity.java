package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.laxiong.Adapter.PaperYuan;
import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Mvp_presenter.TMall_Presenter;
import com.laxiong.Mvp_view.IViewTMall;
import com.laxiong.View.CustomGridView;
import com.laxiong.yitouhang.R;

import java.util.ArrayList;
import java.util.List;

public class TMallActivity extends BaseActivity implements View.OnClickListener, IViewTMall {
    private CustomGridView gv_list;
    private List<PaperYuan> list;
    private ScrollView sl;
    private TMall_Presenter presenter;
    private RelativeLayout rl_yibi, rl_rule;
    private ViewPager vp_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmall);
        initView();
        initData();
    }

    private void initView() {
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
        rl_yibi = (RelativeLayout) findViewById(R.id.rl_yibi);
        rl_rule = (RelativeLayout) findViewById(R.id.rl_rule);
    }

    @Override
    public void loadPageAdapter(ArrayList<ImageView> list) {
        if (list != null && list.size() > 0) {
            PagerAdapter adapter = presenter.getPageAdapter(list);
            if (adapter != null)
                vp_ad.setAdapter(adapter);
        }
    }

    private void initData() {
        list = new ArrayList<PaperYuan>();
        list.add(new PaperYuan("", 1));
        list.add(new PaperYuan("", 5));
        list.add(new PaperYuan("", 10));
        list.add(new PaperYuan("", 20));
        list.add(new PaperYuan("", 50));
        list.add(new PaperYuan("", 100));
        sl = (ScrollView) findViewById(R.id.sl);
        sl.smoothScrollTo(0, 0);
        gv_list = (CustomGridView) findViewById(R.id.gv_list);
        gv_list.setAdapter(new ReuseAdapter<PaperYuan>(TMallActivity.this, list, R.layout.item_execrdpaper) {
            @Override
            public void convert(ViewHolder viewholder, final PaperYuan item) {
                viewholder.setTextView(R.id.tv_price, item.getNum() + "元");
                viewholder.setTextView(R.id.tv_num, item.getNum() * 100 + "");
//                viewholder.setImageBitmap(R.id.ivpic, StringUtils.isBlank(item.getPath())? BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher):);
                TextView tv_btn_exc = viewholder.getView(R.id.tv_btn_exc);
                tv_btn_exc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TMallActivity.this, ExChangeActivity.class);
                        intent.putExtra("type", item.getNum());
                        TMallActivity.this.startActivity(intent);
                    }
                });
            }
        });
        presenter.reqLoadPagerAdaper();
        initListener();
    }

    private void initListener() {
        rl_yibi.setOnClickListener(this);
        rl_rule.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_yibi:
                intent = new Intent(TMallActivity.this, MyYiBiActivity.class);
                break;
            case R.id.rl_rule:
                intent = new Intent(TMallActivity.this, RpExplainActivity.class);
                break;
        }
        TMallActivity.this.startActivity(intent);
    }
}

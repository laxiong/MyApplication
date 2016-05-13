package com.laxiong.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.gongshidai.mistGSD.R;
import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.BindCardItem;
import com.laxiong.Mvp_presenter.BankCard_Presenter;
import com.laxiong.Mvp_view.IViewCardList;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;

import java.util.List;

public class BankListActivity extends BaseActivity implements IViewCardList {
    private ListView lvlist;
    private CommonActionBar actionbar;
    private BankCard_Presenter presenter;
    private ReuseAdapter<BindCardItem> adapter;
    private List<BindCardItem> listitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
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
                Intent intent = new Intent();
                intent.putExtra("bankname", listitem.get(position).getName());
                intent.putExtra("id",listitem.get(position).getId());
                setResult(RESULT_OK, intent);
                BankListActivity.this.finish();
            }
        });
    }

    private void initData() {
        presenter = new BankCard_Presenter(this);
        presenter.loadCardlist(this);
    }

    @Override
    public void loadCardListData(List<BindCardItem> listitem) {
        this.listitem = listitem;
        Log.i("kk", listitem.toString());
        adapter = new ReuseAdapter<BindCardItem>(BankListActivity.this, listitem, R.layout.item_bankcard) {
            @Override
            public void convert(ViewHolder viewholder, BindCardItem item) {
                viewholder.setImageResource(R.id.iv_mark, getResources().getIdentifier("logo_" + item.getLogoKey(), "drawable", getPackageName()));
                viewholder.setText(R.id.tv_name, item.getName());
            }
        };
        lvlist.setAdapter(adapter);
    }

    @Override
    public void loadCardListFailure(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    private void initView() {
        lvlist = (ListView) findViewById(R.id.lvlist);
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
    }
}

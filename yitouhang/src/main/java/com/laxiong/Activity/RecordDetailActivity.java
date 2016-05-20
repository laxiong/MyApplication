package com.laxiong.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
import com.laxiong.Mvp_model.InvestDetail;
import com.laxiong.Mvp_presenter.InvestDetail_Presenter;
import com.laxiong.Mvp_view.IViewBasic;
import com.laxiong.Mvp_view.IViewBasicObj;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;

import java.util.List;

public class RecordDetailActivity extends BaseActivity implements IViewBasicObj<InvestDetail> {
    private InvestDetail_Presenter presenter;
    private CommonActionBar actionBar;
    private TextView tv_tum, tv_name, tv_pltnum, tv_idc, tv_ttime, tv_ttype, tv_endtime, tv_tmoney, tv_fee, tv_look;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        initView();
        initData();
        initListener();
    }

    @Override
    public void loadObjSuc(final InvestDetail obj) {
        tv_name.setText(obj.getName());
        tv_tum.setText(obj.getId() + "");
        tv_ttype.setText(obj.getType());
        tv_ttime.setText(obj.getAdd_time());
        tv_tmoney.setText(obj.getAmount() + "元");
        tv_pltnum.setText(obj.getPhone() + "");
        tv_endtime.setText(obj.getCollection_time());
        tv_fee.setText(obj.getRoutine() + "元");
        tv_idc.setText(obj.getIdc() + "");
        tv_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                if (obj.getTtnum() > 0) {//固息宝
                    intent=new Intent(RecordDetailActivity.this,GuXiBaoActivity.class);
                    intent.putExtra("id", obj.getBorrow_id()).putExtra("ttnum", obj.getTtnum());
                } else {
                    intent=new Intent(RecordDetailActivity.this,TimeXiTongActivity.class);
                    intent.putExtra("id", obj.getBorrow_id());
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void loadObjFail(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    private void initListener() {
        actionBar.setBackListener(this);
    }

    private void initData() {
        presenter = new InvestDetail_Presenter(this);
        Intent intent = getIntent();
        if (intent == null)
            return;
        int id = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        actionBar.setTitle(title);
        String types = intent.getStringExtra("types");
        if (id != -1) {
            presenter.loadInvestDetail(id, types, this);
        }

    }

    private void initView() {
        actionBar = (CommonActionBar) findViewById(R.id.actionbar);
        tv_tum = (TextView) findViewById(R.id.tv_tnum);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_endtime = (TextView) findViewById(R.id.tv_endtime);
        tv_fee = (TextView) findViewById(R.id.tv_fee);
        tv_idc = (TextView) findViewById(R.id.tv_idc);
        tv_pltnum = (TextView) findViewById(R.id.tv_pltnum);
        tv_tmoney = (TextView) findViewById(R.id.tv_tmoney);
        tv_ttime = (TextView) findViewById(R.id.tv_ttime);
        tv_ttype = (TextView) findViewById(R.id.tv_ttype);
        tv_look = (TextView) findViewById(R.id.tv_look);
    }
}

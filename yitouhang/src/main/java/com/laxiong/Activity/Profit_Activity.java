package com.laxiong.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.Renmai_Presenter;
import com.laxiong.Mvp_view.IView_Renmai;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;
import com.laxiong.entity.User;
import com.laxiong.yitouhang.R;

public class Profit_Activity extends BaseActivity implements IView_Renmai, View.OnClickListener {
    private CommonActionBar actionbar;
    private TextView tv_rmshouyi, tv_num1, tv_num2;
    private RelativeLayout rl_improve, rl_1du, rl_2du;
    private Renmai_Presenter presenter;
    private static final int YIDU = 0;
    private static final int ERDU = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        actionbar.setBackListener(this);
        rl_improve.setOnClickListener(this);
        rl_1du.setOnClickListener(this);
        rl_2du.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_improve:
                String phonnum = SpUtils.getStrValue(SpUtils.getSp(this), SpUtils.USER_KEY);
                User user=YiTouApplication.getInstance().getUser();
                if (user != null && !StringUtils.isBlank(phonnum)) {
                    String url = InterfaceInfo.INVITE_URL + "userId=" + user.getId() + "&mobile=" + phonnum;
                    intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }else{
                    intent=new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                break;
            case R.id.rl_1du:
                intent = new Intent(Profit_Activity.this, RmDetailActivity.class);
                intent.putExtra("type", YIDU);
                break;
            case R.id.rl_2du:
                intent = new Intent(Profit_Activity.this, RmDetailActivity.class);
                intent.putExtra("type", ERDU);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void loadRmNumSuc(int yidu, int erdu) {
        tv_num1.setText(yidu + "人");
        tv_num2.setText(erdu + "人");
    }

    @Override
    public void loadRmNumFail(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    private void initData() {
        presenter = new Renmai_Presenter(this);
        actionbar.addClickTextRight(this, "说明", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profit_Activity.this, WebViewActivity.class);
                intent.putExtra("url", InterfaceInfo.RENMAIEXP_URL);
                startActivity(intent);
            }
        });
        presenter.loadRmNum(this);
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if (user.getProfit_list() == null)
                return;
            tv_rmshouyi.setText(user.getProfit_list().getRenmai() + "元");
        }

    }

    private void initView() {
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        tv_rmshouyi = (TextView) findViewById(R.id.tv_rmshouyi);
        tv_num1 = (TextView) findViewById(R.id.tv_num1);
        tv_num2 = (TextView) findViewById(R.id.tv_num2);
        rl_improve = (RelativeLayout) findViewById(R.id.rl_improve);
        rl_1du = (RelativeLayout) findViewById(R.id.rl_1du);
        rl_2du = (RelativeLayout) findViewById(R.id.rl_2du);

    }
}

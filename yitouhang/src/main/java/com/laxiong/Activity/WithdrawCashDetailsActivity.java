package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laxiong.Utils.StringUtils;
import com.gongshidai.mistGSD.R;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WithdrawCashDetailsActivity extends BaseActivity implements OnClickListener {
    /***
     * 提现结果
     */
    private TextView mTitle, tv_cash, tv_bank, tv_fee, tv_result, tv_time;
    private FrameLayout mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawcashdetails);
        initView();
        initData();
    }

    private void initData() {
        mBack.setOnClickListener(this);
        Intent intent = getIntent();
        String cash = intent.getStringExtra("cash");
        String fee = intent.getStringExtra("fee");
        String bankname = intent.getStringExtra("bankname");
        if (!StringUtils.testBlankAll(cash, fee, bankname)) {
            tv_cash.setText(cash+"元");
            tv_fee.setText(fee+"元");
            tv_bank.setText(bankname);
            tv_result.setText((Double.valueOf(cash) - Double.valueOf(fee)) + "元");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            tv_time.setText(df.format(new Date()));
        }
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("提现结果");
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        tv_cash = (TextView) findViewById(R.id.tv_cash);
        tv_bank = (TextView) findViewById(R.id.tv_bank);
        tv_fee = (TextView) findViewById(R.id.tv_fee);
        tv_result = (TextView) findViewById(R.id.tv_result);
        tv_time = (TextView) findViewById(R.id.tv_time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
        }
    }


}

package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.carfriend.mistCF.R;
import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Mvp_presenter.SecurityVali_Presenter;
import com.laxiong.Mvp_view.IViewSecurity;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;

public class ChangeBindPhoneActivity1 extends BaseActivity implements OnClickListener, IViewSecurity {
    /***
     * 修改手机号的第一层
     */
    private TextView nextPage;
    private FrameLayout mBack;
    private SecurityVali_Presenter spresenter;
    private EditText et_pwd, et_name, et_identi;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changebindphone1);
        initView();
        initData();
    }

    private void initData() {
        spresenter = new SecurityVali_Presenter(this);
        nextPage.setOnClickListener(this);
        mBack.setOnClickListener(this);
        ValifyUtil.setEnabled(nextPage, false);
        TextWatcher tw = new BasicWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isBlank(et_name.getText().toString())&&ValifyUtil.valifyName(ChangeBindPhoneActivity1.this,et_name.getText().toString().trim())&&!StringUtils.isBlank(et_pwd.getText().toString()) &&
                        ValifyUtil.valifyIdenti(et_identi.getText().toString())&&ValifyUtil.valifyPwd(et_pwd.getText().toString().trim())) {
                    ValifyUtil.setEnabled(nextPage, true);
                } else {
                    ValifyUtil.setEnabled(nextPage, false);
                }
            }
        };
        et_pwd.addTextChangedListener(tw);
        et_identi.addTextChangedListener(tw);
        et_name.addTextChangedListener(tw);
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return et_name.getText().toString();
    }

    @Override
    public String getIdenti() {
        return et_identi.getText().toString();
    }

    @Override
    public String getPwd() {
        return et_pwd.getText().toString();
    }

    @Override
    public void reqbackSuc(String tag) {
        if (StringUtils.isBlank(token))
            return;
        ToastUtil.customAlert(this, "成功");
        Intent intent = new Intent(ChangeBindPhoneActivity1.this,
                ChangeBindPhoneActivity2.class);
        intent.putExtra("token", token);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void reqbackFail(String msg, String tag) {
        ToastUtil.customAlert(this, msg);
    }

    private void initView() {
        nextPage = (TextView) findViewById(R.id.nextPage);
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        TextView mText = (TextView) findViewById(R.id.title);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_identi = (EditText) findViewById(R.id.et_identi);
        et_name = (EditText) findViewById(R.id.et_name);
        mText.setText("修改手机");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextPage:        /**下一步**/
                spresenter.valifySecurity(this);
                break;

            case R.id.back_layout:
                this.finish();
                break;
        }
    }

}

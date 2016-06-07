package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Mvp_presenter.Setting_Presenter;
import com.laxiong.Mvp_view.IViewSetting;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.laxiong.entity.User;
import com.gongshidai.mistGSD.R;
public class NameSettingActivity extends BaseActivity implements OnClickListener, IViewSetting {
    /***
     * 昵称设置
     */
    private FrameLayout mBack;
    private TextView mSave;
    private EditText et_nickname;
    private Setting_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_setting);
        initView();
        initData();
    }

    @Override
    public String getNickName() {
        return et_nickname.getText().toString();
    }

    @Override
    public void setNickSuccess() {
        ToastUtil.customAlert(this, "设置昵称成功");
        Intent intent = new Intent(this, PersonalSettingActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void setNickFailure(String msg) {
        ToastUtil.customAlert(this, msg);
        et_nickname.setText("");
        et_nickname.setFocusable(true);
        et_nickname.setFocusableInTouchMode(true);
        et_nickname.requestFocus();
    }

    private void initView() {
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        mSave = (TextView) findViewById(R.id.save);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
    }

    private void initData() {
        presenter = new Setting_Presenter(this);
        User user = YiTouApplication.getInstance().getUser();
        String nickname = user == null || StringUtils.isBlank(user.getNickname()) && StringUtils
                .isBlank(user.getNamed()) ? "请输入昵称" : (StringUtils.isBlank(user.getNickname())
                ? user.getNamed() : user.getNickname());
        et_nickname.setHint(nickname);
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.back_layout:
                this.finish();
                break;

            case R.id.save:
                if (StringUtils.isBlank(et_nickname.getText().toString())) {
                    ToastUtil.customAlert(NameSettingActivity.this, "昵称不能为空!");
                } else {
                    presenter.reqSetNickName(this);
                }
                break;
        }
    }


}

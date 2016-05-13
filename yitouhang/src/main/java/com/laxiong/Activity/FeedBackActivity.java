package com.laxiong.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.parser.Feature;
import com.gongshidai.mistGSD.R;
import com.laxiong.Mvp_presenter.Version_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;

public class FeedBackActivity extends BaseActivity implements IViewCommonBack {
    private CommonActionBar actionbar;
    private Version_Presenter presenter;
    private TextView tv_subbtn;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feed_back);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        presenter = new Version_Presenter(this);
        tv_subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString();
                if (StringUtils.isBlank(content)) {
                    ToastUtil.customAlert(FeedBackActivity.this, "意见不能为空");
                    return;
                }
                if (content.trim().length() < 10) {
                    ToastUtil.customAlert(FeedBackActivity.this, "意见不能少于10个字");
                    return;
                }
                presenter.loadFeedBack(content, FeedBackActivity.this);
            }
        });
    }

    @Override
    public void reqbackSuc(String tag) {
        ToastUtil.customAlert(this, "意见反馈成功");
        finish();
    }

    @Override
    public void reqbackFail(String msg, String tag) {
        ToastUtil.customAlert(this, msg);
    }

    private void initListener() {
        actionbar.setBackListener(this);
    }

    private void initView() {
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        tv_subbtn = (TextView) findViewById(R.id.tv_subbtn);
        et_content = (EditText) findViewById(R.id.et_content);
    }
}

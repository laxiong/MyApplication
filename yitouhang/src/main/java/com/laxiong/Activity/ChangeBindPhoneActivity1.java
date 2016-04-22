package com.laxiong.Activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.CommonReq_Presenter;
import com.laxiong.Mvp_presenter.Handler_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Mvp_view.IViewTimerHandler;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.laxiong.yitouhang.R;
import com.loopj.android.http.RequestParams;

import org.w3c.dom.Text;

public class ChangeBindPhoneActivity1 extends BaseActivity implements OnClickListener, IViewCommonBack, IViewTimerHandler {
    /***
     * 修改手机号的第一层
     */
    private TextView nextPage, mCode;
    private FrameLayout mBack;
    private CommonReq_Presenter reqPresenter;
    private Handler_Presenter timepresenter;
    private EditText et_code, et_name, et_identi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changebindphone1);
        initView();
        initData();
    }

    private void initData() {
        reqPresenter = new CommonReq_Presenter(this);
        timepresenter = new Handler_Presenter(this);
        nextPage.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mCode.setOnClickListener(this);
        ValifyUtil.setEnabled(nextPage, false);
        TextWatcher tw = new BasicWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isBlank(et_code.getText().toString()) &&
                        !StringUtils.isBlank(et_name.getText().toString())
                        && ValifyUtil.valifyIdenti(et_identi.getText().toString())) {
                    ValifyUtil.setEnabled(nextPage, true);
                } else {
                    ValifyUtil.setEnabled(nextPage, false);
                }
            }
        };
        et_code.addTextChangedListener(tw);
    }

    @Override
    public void handlerViewByTime(int seconds) {
        if (seconds > 0) {
            mCode.setEnabled(false);
            mCode.setText(seconds + "s");
        } else {
            mCode.setEnabled(true);
            mCode.setText("验证码");
        }
    }

    @Override
    public void reqbackSuc() {
        ToastUtil.customAlert(this, "成功");
        startActivity(new Intent(ChangeBindPhoneActivity1.this,
                ChangeBindPhoneActivity2.class));
        this.finish();
    }

    @Override
    public void reqbackFail(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    private void initView() {
        nextPage = (TextView) findViewById(R.id.nextPage);
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        mCode = (TextView) findViewById(R.id.code1);
        TextView mText = (TextView) findViewById(R.id.title);
        et_code = (EditText) findViewById(R.id.et_code);
        et_identi = (EditText) findViewById(R.id.et_identi);
        et_name = (EditText) findViewById(R.id.et_name);
        mText.setText("修改手机");
    }

    public RequestParams getParams() {
        String name = et_name.getText().toString();
        String identi = et_identi.getText().toString();
        String code = et_code.getText().toString();
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.CPHONE);
        params.put("code", code);
        params.put("realname", name);
        params.put("idc", identi);
        return params;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextPage:        /**下一步**/
                reqPresenter.reqCommonBackByPost(InterfaceInfo.VERIFY_URL, this, getParams());
                break;

            case R.id.back_layout:
                this.finish();
                break;

            case R.id.code1: //点击验证码获取验证码
                timepresenter.loadHandlerTimer(1000, 30000);
                break;
        }
    }

}

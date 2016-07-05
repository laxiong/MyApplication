package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.laxiong.Activity.ResetPayPwdActivity;
import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Common.Constants;
import com.laxiong.Mvp_presenter.Handler_Presenter;
import com.laxiong.Mvp_presenter.Password_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Mvp_view.IViewTimerHandler;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.carfriend.mistCF.R;
/**
 * Created by xiejin on 2016/4/20.
 * Types ResetPayPwdFrag1.java
 */

@SuppressLint("NewApi")
public class ResetPayPwdFrag2 extends Fragment implements View.OnClickListener, IViewCommonBack,IViewTimerHandler{
    /****
     * 重置支付密码第二层
     */
    private TextView mNextPage;
    private View layout;
    private InterSecond intersecond;
    private EditText et_vali;
    private Password_Presenter presenter;
    private Handler_Presenter timepresenter;
    private TextView tv_getCode;

    public interface InterSecond {
        public void recordVali(String vali);
    }

    @Override
    public void reqbackSuc(String tag) {
        if("vali".equals(tag)){
            ResetPayPwdFrag3 f3 = new ResetPayPwdFrag3();
            ((ResetPayPwdActivity) getActivity()).setFragment(f3, "flag3");
        }else {
            timepresenter.loadHandlerTimer(Constants.INTERVAL, Constants.TIME);
            ToastUtil.customAlert(getActivity(), "获取验证码成功");
        }
    }

    @Override
    public void handlerViewByTime(int seconds) {
        if(seconds>0){
            tv_getCode.setEnabled(false);
            tv_getCode.setText(seconds+"s");
        }else{
            tv_getCode.setEnabled(true);
            tv_getCode.setText("获取验证码");
        }
    }

    @Override
    public void reqbackFail(String msg,String tag) {
        ToastUtil.customAlert(getActivity(), msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.activity_resetpay_pswd2, null);
        initView();
        initData();
        return layout;
    }

    private void initData() {
        mNextPage.setOnClickListener(this);
        et_vali.addTextChangedListener(new BasicWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String vali = et_vali.getText().toString();
                if (StringUtils.isBlank(vali)) {
                    ValifyUtil.setEnabled(mNextPage, false);
                } else {
                    ValifyUtil.setEnabled(mNextPage, true);
                }
            }
        });
        presenter = new Password_Presenter(this);
        timepresenter=new Handler_Presenter(this);
        presenter.reqPayCode(getActivity());
        ValifyUtil.setEnabled(mNextPage, false);
    }

    private void initView() {
        mNextPage = (TextView) layout.findViewById(R.id.nextpage);
        et_vali = (EditText) layout.findViewById(R.id.et_vali);
        tv_getCode= (TextView) layout.findViewById(R.id.tv_getCode);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        intersecond = (InterSecond) activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextpage:
                if (intersecond != null)
                    intersecond.recordVali(et_vali.getText().toString());
                presenter.valifyCode(getActivity(),et_vali.getText().toString());
                break;
        }
    }
}

package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.laxiong.Activity.ResetPayPwdActivity;
import com.laxiong.Basic.BasicWatcher;
import com.laxiong.Mvp_presenter.Password_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.gongshidai.mistGSD.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiejin on 2016/4/20.
 * Types ResetPayPwdFrag1.java
 */

@SuppressLint("NewApi")
public class ResetPayPwdFrag1 extends Fragment implements View.OnClickListener,IViewCommonBack{
    /****
     * 重置支付密码第一层
     */
    private TextView mNextPage;
    private View layout;
    private InterFirst interfirst;
    private Password_Presenter presenter;
    private EditText et_name, et_identi;

    public interface InterFirst {
        public void recordNameIdenti(String name, String identi);
    }

    @Override
    public void reqbackSuc(String tag) {
        ResetPayPwdFrag2 f2 = new ResetPayPwdFrag2();
        ((ResetPayPwdActivity) getActivity()).setFragment(f2, "flag3");
    }

    @Override
    public void reqbackFail(String msg, String tag) {
        ToastUtil.customAlert(getActivity(),msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.activity_resetpay_pswd1, null);
        initView();
        initData();
        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.interfirst = (InterFirst) activity;
    }

    private void initData() {
        presenter=new Password_Presenter(this);
        mNextPage.setOnClickListener(this);
    }

    private void initView() {
        mNextPage = (TextView) layout.findViewById(R.id.nextpage);
        ValifyUtil.setEnabled(mNextPage, false);
        TextWatcher watcher = new BasicWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isBlank(et_name.getText().toString())&&ValifyUtil.valifyName(getActivity(), et_name.getText().toString())&&!TextUtils.isEmpty(et_identi.getText().toString().trim())&&ValifyUtil.valifyIdenti(et_identi.getText().toString())) {
                    ValifyUtil.setEnabled(mNextPage,true);
                } else {
                    ValifyUtil.setEnabled(mNextPage, false);
                }
            }
        };
        et_identi = (EditText) layout.findViewById(R.id.et_identi);
        et_identi.addTextChangedListener(watcher);
        et_name = (EditText) layout.findViewById(R.id.et_name);
        et_name.addTextChangedListener(watcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextpage:
                if (interfirst != null)
                    interfirst.recordNameIdenti(et_name.getText().toString(), et_identi.getText().toString());
                presenter.valifyIdenti(getActivity(), et_name.getText().toString(), et_identi.getText().toString());
                break;
        }
    }
}

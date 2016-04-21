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
import com.laxiong.Mvp_presenter.Password_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.yitouhang.R;

/**
 * Created by xiejin on 2016/4/20.
 * Types ResetPayPwdFrag1.java
 */

@SuppressLint("NewApi")
public class ResetPayPwdFrag2 extends Fragment implements View.OnClickListener, IViewCommonBack {
    /****
     * 重置支付密码第二层
     */
    private TextView mNextPage;
    private View layout;
    private InterSecond intersecond;
    private EditText et_vali;
    private Password_Presenter presenter;

    public interface InterSecond {
        public void recordVali(String vali);
    }

    @Override
    public void reqbackSuc() {
        ToastUtil.customAlert(getActivity(), "获取验证码成功");
    }

    @Override
    public void reqbackFail(String msg) {
        ToastUtil.customAlert(getActivity(), msg);
    }
    class PwdWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String vali=et_vali.getText().toString();
            if(StringUtils.isBlank(vali)){
                setEnabled(mNextPage,false);
            }else{
                setEnabled(mNextPage,true);
            }
        }
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
        et_vali.addTextChangedListener(new PwdWatcher());
        presenter = new Password_Presenter(this);
        presenter.reqPayCode(getActivity());
        setEnabled(mNextPage,false);
    }

    private void initView() {
        mNextPage = (TextView) layout.findViewById(R.id.nextpage);
        et_vali = (EditText) layout.findViewById(R.id.et_vali);
    }
    public void setEnabled(View view, boolean flag) {
        view.setEnabled(flag);
        view.setBackgroundResource(flag ? R.drawable.button_change_bg_border : R.drawable.button_grey_corner_border);
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
                ResetPayPwdFrag3 f3 = new ResetPayPwdFrag3();
                ((ResetPayPwdActivity) getActivity()).setFragment(f3, "flag3");
                break;
        }
    }
}

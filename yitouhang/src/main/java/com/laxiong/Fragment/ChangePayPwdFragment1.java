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
import android.widget.Toast;

import com.laxiong.Activity.ChangePayPwdActivity;
import com.laxiong.Mvp_presenter.Handler_Presenter;
import com.laxiong.Mvp_presenter.Password_Presenter;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Mvp_view.IViewTimerHandler;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.yitouhang.R;

import org.w3c.dom.Text;

/**
 * Created by xiejin on 2016/4/21.
 * Types ChangePayPwdFragment1.java
 */
@SuppressLint("NewApi")
public class ChangePayPwdFragment1 extends Fragment implements View.OnClickListener, IViewCommonBack, IViewTimerHandler {
    private View layout;
    private TextView mNextPage, mCode;
    private InterChangePwd1 inter1;
    private EditText et_code;
    private Password_Presenter presenter;
    private Handler_Presenter time_presenter;

    public interface InterChangePwd1 {
        public void recordCode(String code);
    }

    @Override
    public void reqbackSuc() {
        ToastUtil.customAlert(getActivity(), "获取验证码成功");
    }

    @Override
    public void reqbackFail(String msg) {
        ToastUtil.customAlert(getActivity(), msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_change_pay_pwd_fragment1, null);
        initView();
        initData();
        return layout;
    }

    class CustomWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (StringUtils.isBlank(s.toString())) {
                setEnabled(mNextPage, false);
            } else {
                setEnabled(mNextPage, true);
            }
        }
    }

    @Override
    public void handlerViewByTime(int seconds) {
        if (seconds > 0) {
            mCode.setText(seconds + "s");
            mCode.setEnabled(false);
            mCode.setClickable(false);
        } else {
            mCode.setText("验证码");
            mCode.setEnabled(true);
            mCode.setClickable(true);
        }
    }

    public void setEnabled(View view, boolean flag) {
        view.setEnabled(flag);
        view.setBackgroundResource(flag ? R.drawable.button_change_bg_border : R.drawable.button_grey_corner_border);
    }

    private void initData() {
        time_presenter = new Handler_Presenter(this);
        presenter = new Password_Presenter(this);
        presenter.reqPayCode(getActivity());
        mNextPage.setOnClickListener(this);
        mCode.setOnClickListener(this);
        time_presenter.loadHandlerTimer(1000, 30000);
        et_code.addTextChangedListener(new CustomWatcher());
        setEnabled(mNextPage, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inter1 = (InterChangePwd1) activity;
    }

    private void initView() {
        mNextPage = (TextView) layout.findViewById(R.id.nextpage);
        mCode = (TextView) layout.findViewById(R.id.time);
        et_code = (EditText) layout.findViewById(R.id.et_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextpage:
                if (inter1 != null)
                    inter1.recordCode(et_code.getText().toString());
                ((ChangePayPwdActivity) getActivity()).setFragment(new ChangePayPwdFragment2(), "frag2");
                break;
            case R.id.code:
                presenter.reqPayCode(getActivity());
                time_presenter.loadHandlerTimer(1000, 30000);
                break;
        }
    }
}

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
import com.laxiong.Utils.StringUtils;
import com.laxiong.yitouhang.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiejin on 2016/4/20.
 * Types ResetPayPwdFrag1.java
 */

@SuppressLint("NewApi")
public class ResetPayPwdFrag1 extends Fragment implements View.OnClickListener {
    /****
     * 重置支付密码第一层
     */
    private TextView mNextPage;
    private View layout;
    private InterFirst interfirst;
    private EditText et_name, et_identi;

    public interface InterFirst {
        public void recordNameIdenti(String name, String identi);
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
        mNextPage.setOnClickListener(this);
    }

    private void initView() {
        mNextPage = (TextView) layout.findViewById(R.id.nextpage);
        setEnabled(mNextPage,false);
        et_identi = (EditText) layout.findViewById(R.id.et_identi);
        TextWatcher watcher = new PwdWatcher();
        et_identi.addTextChangedListener(watcher);
        et_name = (EditText) layout.findViewById(R.id.et_name);
        et_name.addTextChangedListener(watcher);
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
            if (valify(et_name.getText().toString(), et_identi.getText().toString())) {
                setEnabled(mNextPage, false);
            } else {
                setEnabled(mNextPage, true);
            }
        }
    }

    public void setEnabled(View view, boolean flag) {
        view.setEnabled(flag);
        view.setBackgroundResource(flag ? R.drawable.button_change_bg_border : R.drawable.button_grey_corner_border);
    }

    public boolean valify(String name, String identi) {
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher pm = idNumPattern.matcher(identi);
        if (!pm.matches() && !StringUtils.isBlank(name))
            return true;
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextpage:
                if (interfirst != null)
                    interfirst.recordNameIdenti(et_name.getText().toString(), et_identi.getText().toString());
                ResetPayPwdFrag2 f2 = new ResetPayPwdFrag2();
                ((ResetPayPwdActivity) getActivity()).setFragment(f2, "flag3");
                break;
        }
    }
}

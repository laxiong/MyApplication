package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
/**
 * Created by admin on 2016/4/6.
 */
@SuppressLint("NewApi")
public class WithdrawRecordFragment extends Fragment implements View.OnClickListener{
    /***
     * ���ּ�¼����Ƭ
     */
    private View mView ;
    private RelativeLayout mRecharge ;
    private RelativeLayout mCash ;
    private TextView mRecharge_line,mCash_line ;
    private FragmentManager mFragmentManager ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.withdrawrecord_fragment,null);
        initView();
        setListen();
        initFristFrag();
        return mView;
    }

    private void initView(){
        mFragmentManager = this.getFragmentManager();
        mRecharge = (RelativeLayout)mView.findViewById(R.id.Recharge);
        mCash = (RelativeLayout)mView.findViewById(R.id.cash);
        mRecharge_line = (TextView)mView.findViewById(R.id.Recharge_text_line);
        mCash_line=(TextView)mView.findViewById(R.id.cash_text_line);

    }

    private void setListen(){
        mRecharge.setOnClickListener(this);
        mCash.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        hideFragment(mTransaction);
        switch (view.getId()){
            case R.id.Recharge:
                selectTitle(1);
                if(mRechargeFrag==null){
                    mRechargeFrag = new WithdrawRecord_RechargeFragment();
                    mTransaction.add(R.id.withdrawcash_content,mRechargeFrag);
                }else{
                    mTransaction.show(mRechargeFrag);
                }

                break;
            case R.id.cash:
                selectTitle(2);
                if(mCashFrag==null){
                    mCashFrag = new WithdrawRecord_CashFragment();
                    mTransaction.add(R.id.withdrawcash_content,mCashFrag);
                }else{
                    mTransaction.show(mCashFrag);
                }

                break;
        }
        mTransaction.commit();
    }
    private WithdrawRecord_RechargeFragment mRechargeFrag ;
    private WithdrawRecord_CashFragment mCashFrag ;
    private void hideFragment(FragmentTransaction mTransaction){
        if(mRechargeFrag!=null){
            mTransaction.hide(mRechargeFrag);
        }
        if(mCashFrag!=null){
            mTransaction.hide(mCashFrag);
        }
    }


    private void selectTitle(int index){
        mCash_line.setVisibility(View.VISIBLE);
        mRecharge_line.setVisibility(View.VISIBLE);
        switch (index){
            case 1://��ֵ
                mRecharge_line.setBackgroundColor(Color.parseColor("#FF00C021"));
                mCash_line.setVisibility(View.INVISIBLE);
                mCash_line.setBackgroundColor(Color.parseColor("#A6A6A6"));
                break;
            case 2://����
                mCash_line.setBackgroundColor(Color.parseColor("#FF00C021"));
                mRecharge_line.setBackgroundColor(Color.parseColor("#A6A6A6"));
                mRecharge_line.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void initFristFrag(){
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mRechargeFrag = new WithdrawRecord_RechargeFragment();
        mTransaction.add(R.id.withdrawcash_content,mRechargeFrag);
        mTransaction.commit();
        selectTitle(1);
    }

}

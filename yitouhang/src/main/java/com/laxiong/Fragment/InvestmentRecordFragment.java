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

import com.laxiong.yitouhang.R;

/**
 * Created by admin on 2016/4/6.
 */
@SuppressLint("NewApi")
public class InvestmentRecordFragment extends Fragment implements View.OnClickListener{
    /***
     * 投资
     */

    private View mTouziFrag ;
    private TextView mBuyLine,mRansomLine ;
    private RelativeLayout mBuyBtn,mRansomBtn ;
    private FragmentManager mFragmentManager ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTouziFrag = inflater.inflate(R.layout.touzirecord_fragment,null);
        initView();
        setListenner();
        initFristFrag();
        return mTouziFrag;
    }

    private void initView(){
        mFragmentManager = getActivity().getFragmentManager();
        mBuyLine = (TextView) mTouziFrag.findViewById(R.id.buy_text_line);
        mRansomLine =(TextView) mTouziFrag.findViewById(R.id.ransom_text_line);
        mBuyBtn=(RelativeLayout) mTouziFrag.findViewById(R.id.buy);
        mRansomBtn=(RelativeLayout)mTouziFrag.findViewById(R.id.ransom);

    }

    private void setListenner(){
        mRansomBtn.setOnClickListener(this);
        mBuyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        hideFragment(mTransaction);
        switch (view.getId()){
            case R.id.buy:
                selectTitle(1);
                if(mBuyFrag==null){
                    mBuyFrag = new InvestmentRecord_BuyingFragment();
                    mTransaction.add(R.id.touzi_content,mBuyFrag);
                }else {
                    mTransaction.show(mBuyFrag);
                }
                 break;
            case R.id.ransom:
                selectTitle(2);
                if(mRansomFrag==null){
                    mRansomFrag = new InvestmentRecord_RansomFragment();
                    mTransaction.add(R.id.touzi_content,mRansomFrag);
                }else {
                    mTransaction.show(mRansomFrag);
                }
                 break;
        }
        mTransaction.commit();
    }

    private InvestmentRecord_BuyingFragment mBuyFrag ;
    private InvestmentRecord_RansomFragment mRansomFrag ;

    private void hideFragment(FragmentTransaction mTransaction){
        if(mBuyFrag!=null){
            mTransaction.hide(mBuyFrag);
        }
        if(mRansomFrag!=null){
            mTransaction.hide(mRansomFrag);
        }

    }

    private void selectTitle(int index){
        mRansomLine.setVisibility(View.VISIBLE);
        mBuyLine.setVisibility(View.VISIBLE);
        switch (index){
            case 1://����
                mBuyLine.setBackgroundColor(Color.parseColor("#FF00C021"));
                mRansomLine.setVisibility(View.INVISIBLE);
                mRansomLine.setBackgroundColor(Color.parseColor("#A6A6A6"));
                 break;
            case 2://���
                mRansomLine.setBackgroundColor(Color.parseColor("#FF00C021"));
                mBuyLine.setVisibility(View.INVISIBLE);
                mBuyLine.setBackgroundColor(Color.parseColor("#A6A6A6"));
                 break;
        }
    }

    private void initFristFrag(){
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mBuyFrag = new InvestmentRecord_BuyingFragment();
        mTransaction.add(R.id.touzi_content,mBuyFrag);
        mTransaction.commit();
        selectTitle(1);
    }


}

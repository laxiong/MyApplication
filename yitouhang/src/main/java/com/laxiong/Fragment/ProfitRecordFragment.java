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

import com.carfriend.mistCF.R;
/**
 * Created by admin on 2016/4/6.
 */
@SuppressLint("NewApi")
public class ProfitRecordFragment extends Fragment implements View.OnClickListener{
    /***
     * �����¼����Ƭ
     */
    private View profitView ;
    private RelativeLayout mGuXiBao,mTimeXiTong,mPerson ;
    private TextView mGuXiBao_line,mTimeXiTong_line,mPerson_line ;
    private FragmentManager mFragmentManager ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profitView = inflater.inflate(R.layout.profitrecord_fragment,null);
        initView();
        setListenner();
        initFristFrag();
        return profitView;
    }

    private void initView(){
        mFragmentManager = this.getFragmentManager();
        mGuXiBao=(RelativeLayout) profitView.findViewById(R.id.guxibao);
        mTimeXiTong=(RelativeLayout)profitView.findViewById(R.id.shixitong);
        mPerson=(RelativeLayout)profitView.findViewById(R.id.person);
        mGuXiBao_line = (TextView)profitView.findViewById(R.id.guxibao_line);
        mTimeXiTong_line = (TextView)profitView.findViewById(R.id.shixitong_line);
        mPerson_line = (TextView)profitView.findViewById(R.id.person_line);
    }

    private void setListenner(){
        mGuXiBao.setOnClickListener(this);
        mTimeXiTong.setOnClickListener(this);
        mPerson.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        hideFragment(mTransaction);
        switch (view.getId()){
            case R.id.guxibao:
                selectTitle(2);
                if(mGuXiBaoFrag==null){
                    mGuXiBaoFrag = new ProfitRecord_GuXiBaoFragment();
                    mTransaction.add(R.id.profit_content,mGuXiBaoFrag);
                }else{
                    mTransaction.show(mGuXiBaoFrag);
                }

                break;
            case R.id.shixitong:
                selectTitle(1);
                if(mTimeXiTongFrag==null){
                    mTimeXiTongFrag = new ProfitRecord_TimeXiTongFragment();
                    mTransaction.add(R.id.profit_content,mTimeXiTongFrag);
                }else{
                    mTransaction.show(mTimeXiTongFrag);
                }

                break;
            case R.id.person:
                selectTitle(3);
                if(mPersonFrag==null){
                    mPersonFrag = new ProfitRecord_PersonFragment();
                    mTransaction.add(R.id.profit_content,mPersonFrag);
                }else{
                    mTransaction.show(mPersonFrag);
                }

                break;
        }
        mTransaction.commit();
    }

    private void selectTitle(int index){
        mGuXiBao_line.setVisibility(View.VISIBLE);
        mTimeXiTong_line.setVisibility(View.VISIBLE);
        mPerson_line.setVisibility(View.VISIBLE);
        switch (index){
            case 1://ʱϢͨ
                mTimeXiTong_line.setBackgroundColor(Color.parseColor("#FF00C021"));
                mPerson_line.setVisibility(View.INVISIBLE);
                mGuXiBao_line.setVisibility(View.INVISIBLE);
                mPerson_line.setBackgroundColor(Color.parseColor("#A6A6A6"));
                mGuXiBao_line.setBackgroundColor(Color.parseColor("#A6A6A6"));
                break;
            case 2://��Ϣ��
                mGuXiBao_line.setBackgroundColor(Color.parseColor("#FF00C021"));
                mPerson_line.setVisibility(View.INVISIBLE);
                mTimeXiTong_line.setVisibility(View.INVISIBLE);
                mPerson_line.setBackgroundColor(Color.parseColor("#A6A6A6"));
                mTimeXiTong_line.setBackgroundColor(Color.parseColor("#A6A6A6"));
                break;
            case 3://����
                mPerson_line.setBackgroundColor(Color.parseColor("#FF00C021"));
                mGuXiBao_line.setVisibility(View.INVISIBLE);
                mTimeXiTong_line.setVisibility(View.INVISIBLE);
                mTimeXiTong_line.setBackgroundColor(Color.parseColor("#A6A6A6"));
                mGuXiBao_line.setBackgroundColor(Color.parseColor("#A6A6A6"));
                break;
        }
    }
    private ProfitRecord_GuXiBaoFragment mGuXiBaoFrag ;
    private ProfitRecord_PersonFragment  mPersonFrag ;
    private ProfitRecord_TimeXiTongFragment mTimeXiTongFrag ;
    private void hideFragment(FragmentTransaction mTransaction){
        if(mGuXiBaoFrag!=null){
            mTransaction.hide(mGuXiBaoFrag);
        }
        if(mPersonFrag!=null){
            mTransaction.hide(mPersonFrag);
        }
        if(mTimeXiTongFrag!=null){
            mTransaction.hide(mTimeXiTongFrag);
        }
    }

    private void initFristFrag(){
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTimeXiTongFrag = new ProfitRecord_TimeXiTongFragment();
        mTransaction.add(R.id.profit_content,mTimeXiTongFrag);
        mTransaction.commit();
        selectTitle(1);
    }

}

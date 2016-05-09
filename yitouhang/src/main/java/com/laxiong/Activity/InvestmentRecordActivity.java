package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Fragment.InvestmentRecordFragment;
import com.laxiong.Fragment.ProfitRecordFragment;
import com.laxiong.Fragment.WithdrawRecordFragment;
import com.laxiong.Fragment.YuMoneyRecordFragment;
import com.laxiong.Utils.ImageUtil;
import com.laxiong.yitouhang.R;

/**
 *投资明细
 */
public class InvestmentRecordActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout mSelectType ;
    private TextView mSelectText ;
    private FrameLayout mSelectContent ,mBackBtn;
    private FragmentManager mFragmentManager ;
    private ImageView iv_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investmentrecord);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        reverse(90);
    }
    private void reverse(float degree){
        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.back3);
        iv_arrow.setImageBitmap(ImageUtil.reverse(bm,degree));
    }
    @SuppressLint("NewApi")
    private void initView(){
        mSelectContent=(FrameLayout) findViewById(R.id.select_content);
        mSelectType=(LinearLayout) findViewById(R.id.select_type);
        mSelectText=(TextView) findViewById(R.id.select_text);
        iv_arrow= (ImageView) findViewById(R.id.iv_arrow);
        mBackBtn = (FrameLayout)findViewById(R.id.back_layout);

        mFragmentManager = this.getFragmentManager();
        initFristFragment();
    }

    private void setListener(){

        mSelectType.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.select_type:
                showSelectTypePopupWindow();
                // TODO ��ͷ��������ת����Ч��

                break;
            case R.id.back_layout:
                this.finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        reverse(90);
    }

    // show popupwindow
    private PopupWindow mSelectWindow ;
    private View mSelectView ;
    private  TextView mYuTag,mTiTag,mTouTag,mShouTag ; // ��� ���� Ͷ�� ���� ��¼
    private void showSelectTypePopupWindow(){
        reverse(-90);
        mSelectView = LayoutInflater.from(this).inflate(R.layout.select_type_popupwindow,null);
        mYuTag = (TextView) mSelectView.findViewById(R.id.yu_tag);
        mTiTag = (TextView) mSelectView.findViewById(R.id.ti_tag);
        mTouTag = (TextView) mSelectView.findViewById(R.id.tou_tag);
        mShouTag = (TextView) mSelectView.findViewById(R.id.shou_tag);

        selectTextColor();

        mYuTag.setOnClickListener(listener);
        mTouTag.setOnClickListener(listener);
        mShouTag.setOnClickListener(listener);
        mTiTag.setOnClickListener(listener);


        mSelectWindow = new PopupWindow(mSelectView,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
        mSelectWindow.setTouchable(true);
        mSelectWindow.setOutsideTouchable(true);
        // ���������PopupWindow�ı����������ǵ���ⲿ������Back�����޷�dismiss����
        // �Ҿ���������API��һ��bug
        mSelectWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.transfer_bg)); // 设置半透明的背景
        mSelectWindow.showAtLocation(mSelectView, Gravity.BOTTOM, 0, 0);

    }


    View.OnClickListener listener = new View.OnClickListener(){
        @Override@SuppressLint("NewApi")
        public void onClick(View view) {
            reverse(90);
            FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
            hideFragment(mTransaction);
            switch (view.getId()){
                case R.id.yu_tag:
                    if(mYuMoneyFrag==null){
                        mYuMoneyFrag = new YuMoneyRecordFragment();
                        mTransaction.add(R.id.select_content,mYuMoneyFrag);
                    }else{
                        mTransaction.show(mYuMoneyFrag);
                    }
                    mSelectText.setText(mYuTag.getText().toString());
                    mSelectText.setTextColor(Color.parseColor("#ffffff"));

                    break;
                case R.id.ti_tag:
                    if(mTiXianFrag==null){
                        mTiXianFrag = new WithdrawRecordFragment();
                        mTransaction.add(R.id.select_content,mTiXianFrag);
                    }else{
                        mTransaction.show(mTiXianFrag);
                    }
                    mSelectText.setText(mTiTag.getText().toString());
                    mSelectText.setTextColor(Color.parseColor("#ffffff"));

                    break;
                case R.id.tou_tag:
                    if(mTouziFrag==null){
                        mTouziFrag = new InvestmentRecordFragment();
                        mTransaction.add(R.id.select_content,mTouziFrag);
                    }else{
                        mTransaction.show(mTouziFrag);
                    }
                    mSelectText.setText(mTouTag.getText().toString());
                    mSelectText.setTextColor(Color.parseColor("#ffffff"));

                    break;
                case R.id.shou_tag:
                    if(mProfitFrag==null){
                        mProfitFrag = new ProfitRecordFragment();
                        mTransaction.add(R.id.select_content,mProfitFrag);
                    }else{
                        mTransaction.show(mProfitFrag);
                    }
                    mSelectText.setText(mShouTag.getText().toString());
                    mSelectText.setTextColor(Color.parseColor("#ffffff"));

                    break;
            }
            mTransaction.commit();
            dismissPopwindow();
        }
    };

     private void dismissPopwindow(){
        if(mSelectWindow!=null&&mSelectWindow.isShowing()){
            mSelectWindow.dismiss();
            mSelectWindow = null ;
        }
     }

    private InvestmentRecordFragment mTouziFrag ; // Ͷ投资
    private YuMoneyRecordFragment mYuMoneyFrag ; //  余额
    private WithdrawRecordFragment mTiXianFrag ; // 提现
    private ProfitRecordFragment mProfitFrag ; // 收益

    @SuppressLint("NewApi")
    private void hideFragment(FragmentTransaction mTransaction){

        if(mTouziFrag!=null){
           mTransaction.hide(mTouziFrag);
        }
        if(mYuMoneyFrag!=null){
            mTransaction.hide(mYuMoneyFrag);
        }
        if(mTiXianFrag!=null){
            mTransaction.hide(mTiXianFrag);
        }
        if(mProfitFrag!=null){
            mTransaction.hide(mProfitFrag);
        }

    }

    private void setTextColor(int index){
        switch(index){
            case 1://Ͷ投资
                mYuTag.setTextColor(Color.parseColor("#FF333333"));
                mTouTag.setTextColor(Color.parseColor("#FFF04F46"));
                mShouTag.setTextColor(Color.parseColor("#FF333333"));
                mTiTag.setTextColor(Color.parseColor("#FF333333"));
                 break;
            case 2:// 收益
                mYuTag.setTextColor(Color.parseColor("#FF333333"));
                mTouTag.setTextColor(Color.parseColor("#FF333333"));
                mShouTag.setTextColor(Color.parseColor("#FFF04F46"));
                mTiTag.setTextColor(Color.parseColor("#FF333333"));
                 break;
            case 3:// 提现
                mYuTag.setTextColor(Color.parseColor("#FF333333"));
                mTouTag.setTextColor(Color.parseColor("#FF333333"));
                mShouTag.setTextColor(Color.parseColor("#FF333333"));
                mTiTag.setTextColor(Color.parseColor("#FFF04F46"));
                 break;
            case 4: // 余额
                mYuTag.setTextColor(Color.parseColor("#FFF04F46"));
                mTouTag.setTextColor(Color.parseColor("#FF333333"));
                mShouTag.setTextColor(Color.parseColor("#FF333333"));
                mTiTag.setTextColor(Color.parseColor("#FF333333"));
                 break;
        }

    }

    //初始化碎片
    @SuppressLint("NewApi")
    private void initFristFragment(){
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTouziFrag = new InvestmentRecordFragment();
        mTransaction.add(R.id.select_content,mTouziFrag);
        mTransaction.commit();
    }


    private void selectTextColor(){
        if(mSelectText.getText().toString().equals(mTouTag.getText().toString())){
            Toast.makeText(this, mSelectText.getText().toString(), 2).show();
            setTextColor(1);
        }else if(mSelectText.getText().toString().equals(mYuTag.getText().toString())){
            Toast.makeText(this, mSelectText.getText().toString(), 2).show();
            setTextColor(4);
        }else if(mSelectText.getText().toString().equals(mTiTag.getText().toString())){
            Toast.makeText(this, mSelectText.getText().toString(), 2).show();
            setTextColor(3);
        }else if(mSelectText.getText().toString().equals(mShouTag.getText().toString())){
            Toast.makeText(this, mSelectText.getText().toString(), 2).show();
            setTextColor(2);
        }

    }



}

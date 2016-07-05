package com.laxiong.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.carfriend.mistCF.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.ToastUtil;

import org.json.JSONObject;

/**
 * Created by admin on 2016/4/29.
 */
public class MyBandBankCardActivity extends BaseActivity implements View.OnClickListener{
    /***
     * 我的银行卡
     */
    private TextView mTitle ,mBankName;
    private FrameLayout mBack ;
    private ImageView mBankImg ;
//    private CircleImageView mBankImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_bankcard);
        initView();
        getBankInfo();
    }
    private void initView(){
        mTitle =(TextView)findViewById(R.id.title);
        mBack =(FrameLayout)findViewById(R.id.back_layout);
        mTitle.setText("我的银行卡");
        mBack.setOnClickListener(this);
        mBankName =(TextView)findViewById(R.id.bankname);
        mBankImg =(ImageView)findViewById(R.id.bankicon);
//        mBankImg =(CircleImageView)findViewById(R.id.bankicon);
    }
    @Override
    public void onClick(View v) {
        this.finish();
    }

    private void getBankInfo(){
        HttpUtil2.get(InterfaceInfo.BASE_URL + "/bank", new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            upDataUi(response);
                        } else {
                            ToastUtil.customAlert(MyBandBankCardActivity.this,response.getString("msg"));
                        }
                    } catch (Exception E) {
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.customAlert(MyBandBankCardActivity.this,"获取数据失败");
            }
        },Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
                .getUserLogin().getToken()));
    }

    private void upDataUi(JSONObject response){
        if (response != null) {
            try {
                mBankName.setText(response.getString("name")+"(尾号"+response.getInt("snumber")+")");
                mBankImg.setImageResource(getResources().getIdentifier("logo_" + response.getString("logoKey"), "drawable", getPackageName()));
            } catch (Exception E) {
            }
        }
    }

}

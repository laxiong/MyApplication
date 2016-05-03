package com.laxiong.View;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laxiong.yitouhang.R;

/**
 * Created by admin on 2016/4/28.
 * 失败
 */
public class PayMethodSelectPop extends PopupWindow implements View.OnClickListener{

    private View PayView;
    private ImageView newCard_img,lateMoney_img ,constranceBank_img ,BankIcon,LateMoneyIcon ,NewCardIcon;
    private RelativeLayout newCard,lateMoney,constranceBank ;
    private TextView mConcel ,mBankName,mMyYuE,mNewCard;

    private String mBankStr ;  // 银行卡的名字
    private String banklogokey; // 银行卡的logoKey

    public String getBanklogokey() {
        return banklogokey;
    }
    public void setBanklogokey(String banklogokey) {
        this.banklogokey = banklogokey;
    }
    public String getmBankStr() {
        return mBankStr;
    }
    public void setmBankStr(String mBankStr) {
        this.mBankStr = mBankStr;
    }
    Activity context ;
    public PayMethodSelectPop(Activity context) {
        super(context);
        this.context = context ;
        PayView = LayoutInflater.from(context).inflate(R.layout.pay_mathod_popwindow,null);

        newCard = (RelativeLayout)PayView.findViewById(R.id.addnewcard);
        lateMoney = (RelativeLayout)PayView.findViewById(R.id.latemoney);
        constranceBank = (RelativeLayout)PayView.findViewById(R.id.concreatebank);
        mConcel = (TextView)PayView.findViewById(R.id.concel);
        //选中与否的图片
        newCard_img = (ImageView)PayView.findViewById(R.id.change_img_addnewcard);
        lateMoney_img = (ImageView)PayView.findViewById(R.id.change_img_latemoney);
        constranceBank_img = (ImageView)PayView.findViewById(R.id.change_img_concreatebank);
        //银行卡等信息
        mBankName =(TextView)PayView.findViewById(R.id.bankname);
        mMyYuE =(TextView)PayView.findViewById(R.id.myyue);
        mNewCard =(TextView)PayView.findViewById(R.id.newcard);
        //Item卡的图片
        BankIcon =(ImageView)PayView.findViewById(R.id.icon1);
        LateMoneyIcon =(ImageView)PayView.findViewById(R.id.icon2);
        NewCardIcon =(ImageView)PayView.findViewById(R.id.icon3);

        setListen();
        setItemSelect(1);
    }

    private void setListen(){
        newCard.setOnClickListener(this);
        lateMoney.setOnClickListener(this);
        constranceBank.setOnClickListener(this);
        mConcel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addnewcard:
                setItemSelect(2);
                break;
            case R.id.latemoney:
                setItemSelect(3);
                break;
            case R.id.concreatebank:
                setItemSelect(1);
                break;
            case R.id.concel:
                if (this.isShowing())
                    this.dismiss();
                break;
        }
    }

    private void setItemSelect(int index){
        initSelectImg();
        switch (index){
            case 1:
                constranceBank_img.setImageResource(R.drawable.img_read);
                BankMessageLoad();
                break;
            case 2:
                newCard_img.setImageResource(R.drawable.img_read);
                break;
            case 3:
                lateMoney_img.setImageResource(R.drawable.img_read);
                break;
        }
    }

    private void initSelectImg(){
        newCard_img.setImageResource(R.drawable.img_no_read);
        lateMoney_img.setImageResource(R.drawable.img_no_read);
        constranceBank_img.setImageResource(R.drawable.img_no_read);
    }

    private void BankMessageLoad(){
        if (mBankStr!=null)
            mBankName.setText(mBankStr);
        if (banklogokey!=null)
            BankIcon.setImageResource(context.getResources().getIdentifier("logo_" + banklogokey, "drawable", context.getPackageName()));
    }

    public View getPayView() {
        return PayView;
    }
    // 显示出来
    public void showPopLocation(){
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        this.setBackgroundDrawable(null); //设置半透明
        this.showAtLocation(PayView, Gravity.BOTTOM, 0, 0);
    }
}

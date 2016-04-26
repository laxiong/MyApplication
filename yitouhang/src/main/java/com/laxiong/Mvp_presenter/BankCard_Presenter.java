package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.BankCard;
import com.laxiong.Mvp_model.BaseCard;
import com.laxiong.Mvp_model.BindCardItem;
import com.laxiong.Mvp_model.Model_card;
import com.laxiong.Mvp_view.IViewBankCard;
import com.laxiong.Mvp_view.IViewBindCard;
import com.laxiong.Mvp_view.IViewCardList;
import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * Created by xiejin on 2016/4/24.
 * Types BankCard_Presenter.java
 */
public class BankCard_Presenter extends CommonReq_Presenter implements Model_card.OnLoadBankCardListener {
    private IViewBindCard iviewbank;
    private IViewBankCard iviewbankcard;
    private Model_card mcard;
    public static final String TYPE_CODE = "code";
    public static final String TYPE_CARD = "card";
    private IViewCardList ivlist;

    public BankCard_Presenter(IViewBindCard iviewbank) {
        super(iviewbank);
        this.iviewbank = iviewbank;
    }

    public BankCard_Presenter(IViewBankCard iviewbankcard) {
        this.iviewbankcard = iviewbankcard;
        this.mcard = new Model_card();
    }
    public BankCard_Presenter(IViewCardList iviewclist){
        this.ivlist=iviewclist;
        this.mcard=new Model_card();
    }

//    public void sendCode(Context context) {
//        RequestParams params = new RequestParams();
//        params.put("type", Constants.ReqEnum.CPHONE.getVal());
//        params.put("phone", iviewbank.getPhone());
//        aureqByPost(InterfaceInfo.CODE_URL, context, params, TYPE_CODE);
//    }
    public void loadCardlist(Context context){
        mcard.loadBankList(context,this);
    }

    @Override
    public void onSuccess(List<BindCardItem> listitem) {
        ivlist.loadCardListData(listitem);
    }

    @Override
    public void onFailureList(String msg) {
        ivlist.loadCardListFailure(msg);
    }

    @Override
    public void onSuccess(BankCard card) {
        iviewbankcard.loadCardData(card);
    }

    @Override
    public void onFailure(String msg) {
        iviewbankcard.loadCardFailure(msg);
    }

    public void bindCard(Context context) {
        RequestParams params=new RequestParams();
        params.put("number",iviewbank.getCardNum());
        params.put("name",iviewbank.getName());
        params.put("org_id",iviewbank.getCardId());
        params.put("phone", iviewbank.getPhoneNum());
        aureqByPost(InterfaceInfo.BINDCARD_URL,context,params,null);
    }
}

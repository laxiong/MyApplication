package com.laxiong.Mvp_presenter;

import android.content.Context;

import com.laxiong.Mvp_model.Message;
import com.laxiong.Mvp_model.Model_Msg;
import com.laxiong.Mvp_model.OnLoadBasicListener;
import com.laxiong.Mvp_view.IViewMessage;
import com.loopj.android.network.RequestParams;

import java.util.List;

/**
 * Created by xiejin on 2016/4/29.
 * Types Message_Presenter.java
 */
public class Message_Presenter implements OnLoadBasicListener<Message> {
    private IViewMessage iviewmsg;
    private Model_Msg mMsg;

    public Message_Presenter(IViewMessage iviewmsg) {
        this.iviewmsg = iviewmsg;
        mMsg = new Model_Msg();
    }

    public void loadMsgList(Context context) {
        RequestParams params = new RequestParams();
        mMsg.loadMsgList(context, params, this);
    }

    @Override
    public void loadOnSuccess(List<Message> list) {
        iviewmsg.loadMsgSuc(list);
    }

    @Override
    public void loadOnFailure(String msg) {
        iviewmsg.loadMsgFailure(msg);
    }
}

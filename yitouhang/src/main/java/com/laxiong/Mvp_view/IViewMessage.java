package com.laxiong.Mvp_view;

import com.laxiong.Mvp_model.Message;

import java.util.List;

/**
 * Created by xiejin on 2016/4/29.
 * Types IViewMessage.java
 */
public interface IViewMessage {
    public void loadMsgSuc(List<Message> list);
    public void loadMsgFailure(String msg);
}

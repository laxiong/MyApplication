package com.laxiong.Mvp_model;

import android.content.Context;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.network.RequestParams;

/**
 * Created by xiejin on 2016/4/29.
 * Types Model_Yibi.java
 */
public class Model_Yibi<T> extends Model_Basic2 {
    /**
     *
     * @param context
     * @param listener
     */
    public void loadYibiInput(String url, Context context, OnLoadBasicListener<Score> listener) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori) || listener == null)
            return;
        setListener(listener);
        reqAuthGetList(context, url, "list", Score.class);
    }

    /**
     *
     * @param context
     * @param listener
     */
    public void loadYibiOutput(String url,Context context, OnLoadBasicListener<Score> listener) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori) || listener == null)
            return;
        setListener(listener);
        reqAuthGetList(context, url, "list", Score.class);
    }
}

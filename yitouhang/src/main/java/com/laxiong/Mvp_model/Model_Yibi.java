package com.laxiong.Mvp_model;

import android.content.Context;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.StringUtils;
import com.loopj.android.http.RequestParams;

/**
 * Created by xiejin on 2016/4/29.
 * Types Model_Yibi.java
 */
public class Model_Yibi<T> extends Model_Basic {
    /**
     *
     * @param params
     * @param context
     * @param listener
     */
    public void loadYibiInput(int id,RequestParams params, Context context, OnLoadBasicListener<Score> listener) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori) || listener == null)
            return;
        setListener(listener);
        aureqByGet(InterfaceInfo.SCORE_URL+id, context, params, "list", Score.class);

    }

    /**
     *
     * @param params
     * @param context
     * @param listener
     */
    public void loadYibiOutput(int id,RequestParams params, Context context, OnLoadBasicListener<Score> listener) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori) || listener == null)
            return;
        setListener(listener);
        aureqByGet(InterfaceInfo.SCORE_URL+id, context, params, "list", Score.class);
    }
}

package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.Model_adapter;
import com.laxiong.Mvp_model.OnLoadBasicListener;
import com.laxiong.Mvp_model.Renmai;
import com.laxiong.Mvp_view.IViewBasic;
import com.laxiong.Mvp_view.IView_Renmai;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiejin on 2016/5/4.
 * Types Renmai_Presenter.java
 */
public class Renmai_Presenter implements OnLoadBasicListener<Renmai> {
    private IView_Renmai iviewrm;
    private IViewBasic<Renmai> iviewbasic;
    private Model_adapter<Renmai> madapter;

    public Renmai_Presenter(IView_Renmai iviewrm) {
        this.iviewrm = iviewrm;
    }

    public Renmai_Presenter(IViewBasic<Renmai> iviewbasic) {
        this.iviewbasic = iviewbasic;
        this.madapter = new Model_adapter<Renmai>();
    }

    @Override
    public void loadOnSuccess(List<Renmai> list) {
        iviewbasic.loadListSuc(list);
    }

    @Override
    public void loadOnFailure(String msg) {
        iviewbasic.loadListFail(msg);
    }

    public void loadRmDetail(int page, int pagesize, String type, Context context) {
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("page", page);
        params.put("pageSize", pagesize);
        madapter.loadListGet(InterfaceInfo.FRIENDS_URL + "/" + user.getId(), context, params, this, Renmai.class);
    }

    public void loadRmNum(Context context) {
        String authroi = CommonReq.getAuthori(context);
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return;
        }
        if (StringUtils.isBlank(authroi))
            return;
        HttpUtil.get(InterfaceInfo.RMNUM_URL + "/" + user.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewrm.loadRmNumSuc(response.getInt("one"), response.getInt("two"));
                        } else {
                            iviewrm.loadRmNumFail(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewrm.loadRmNumFail(e.toString());
                    }
                } else {
                    iviewrm.loadRmNumFail("无响应");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewrm.loadRmNumFail(responseString);
            }
        }, authroi);
    }
}

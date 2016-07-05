package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.laxiong.Basic.Callback;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewTMall;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.entity.Product;
import com.laxiong.entity.TMall_Ad;
import com.loopj.android.network.AsyncHttpResponseHandler;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.carfriend.mistCF.R;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiejin on 2016/4/17.
 * Types TMall_Presenter.java
 */
public class TMall_Presenter {
    private IViewTMall ivewtmall;

    public TMall_Presenter(IViewTMall ivewtmall) {
        this.ivewtmall = ivewtmall;
    }

    public void reqLoadPageData(final Context context) {
        HttpUtil2.get(InterfaceInfo.SHOP_URL, new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        String productlist = response.getJSONArray("product").toString();
                        String adlist = response.getJSONArray("list").toString();
                        List<Product> plist = JSONUtils.parseArray(productlist, Product.class);
                        List<TMall_Ad> alist = JSONUtils.parseArray(adlist, TMall_Ad.class);
                        ivewtmall.fillVPData(alist);
                        ivewtmall.fillPListData(plist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                if (response != null) {
//                    try {
//                        String productlist = response.getJSONArray("product").toString();
//                        String adlist = response.getJSONArray("list").toString();
//                        List<Product> plist = JSONUtils.parseArray(productlist, Product.class);
//                        List<TMall_Ad> alist = JSONUtils.parseArray(adlist, TMall_Ad.class);
//                        ivewtmall.fillVPData(alist);
//                        ivewtmall.fillPListData(plist);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
        });
    }
}

package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewTMall;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.entity.Product;
import com.laxiong.entity.TMall_Ad;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.gongshidai.mistGSD.R;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        HttpUtil.get(InterfaceInfo.SHOP_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    public static void reqLoadImageView(String url, final ImageView iv) {
        HttpUtil.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody != null) {
                    if (responseBody == null) {
                        setImageFailure(R.drawable.gongshi_banner_mr, iv);
                    } else {
                        Bitmap bm = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                        if (bm == null)
                            setImageFailure(R.drawable.gongshi_banner_mr, iv);
                        else
                            setImageSuccess(bm, iv);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                setImageFailure(R.drawable.gongshi_banner_mr, iv);
            }
        });
    }
    public static void setImageFailure(int id, ImageView iv) {
        iv.setImageResource(id);
    }

    public static void setImageSuccess(Bitmap bm, ImageView iv) {
        iv.setImageBitmap(bm);
    }

    public PagerAdapter getPageAdapter(final List<ImageView> ivlist) {
        if (ivlist == null || ivlist.size() == 0)
            return null;
        return new PagerAdapter() {
            @Override
            public int getCount() {
                return ivlist.size();
//                return Integer.MAX_VALUE;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv=ivlist.get(position);
                container.addView(iv);
                return iv;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(ivlist.get(position));
            }
        };
    }
}

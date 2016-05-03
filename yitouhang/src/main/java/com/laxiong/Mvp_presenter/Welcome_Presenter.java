package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.laxiong.Mvp_model.Model_Welcome;
import com.laxiong.Mvp_model.WelcImg;
import com.laxiong.Mvp_view.IViewMessage;
import com.laxiong.Mvp_view.IViewWelcome;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.yitouhang.R;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by xiejin on 2016/5/3.
 * Types Welcome_Presenter.java
 */
public class Welcome_Presenter implements Model_Welcome.OnLoadWelcomeListener {
    private IViewWelcome iviewwel;
    private Model_Welcome mWel;

    public Welcome_Presenter(IViewWelcome iviewwel) {
        this.iviewwel = iviewwel;
        mWel = new Model_Welcome();
    }

    public void loadImage(String url, Context context, final ImageView iv) {
        HttpUtil.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody != null) {
                    if (responseBody == null) {
                        setImageFailure(R.drawable.ic_launcher, iv);
                    } else {
                        Bitmap bm = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                        if (bm == null)
                            setImageFailure(R.drawable.ic_launcher, iv);
                        else
                            setImageSuccess(bm, iv);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                setImageFailure(R.drawable.ic_launcher, iv);
            }
        });
    }

    public  void setImageFailure(int id, ImageView iv) {
        iv.setImageResource(id);
    }

    public  void setImageSuccess(Bitmap bm, ImageView iv) {
        iv.setImageBitmap(bm);
    }

    public void loadWelcomeData(Context context) {
        mWel.loadWelList(context, this);
    }

    @Override
    public void loadWelcomeSuc(WelcImg list) {
        iviewwel.loadListSuc(list);
    }

    @Override
    public void loadOnFailure(String msg) {
        iviewwel.loadListFail(msg);
    }
}

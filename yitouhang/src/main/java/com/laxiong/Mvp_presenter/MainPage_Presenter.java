package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.Model_Basic;
import com.laxiong.Mvp_model.OnLoadBasicListener;
import com.laxiong.Mvp_view.IViewBasic;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.Banner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xiejin on 2016/5/13.
 * Types MainPage_Presenter.java
 */
public class MainPage_Presenter implements OnLoadBasicListener<Banner> {
    private IViewBasic<Banner> iViewBasic;
    private Model_Basic<Banner> mbasic;
    private SharedPreferences sp;

    public MainPage_Presenter(IViewBasic<Banner> iViewBasic) {
        this.iViewBasic = iViewBasic;
        mbasic = new Model_Basic<Banner>();
        mbasic.setListener(this);
    }

    @Override
    public void loadOnSuccess(List<Banner> list) {
        if(sp!=null){
            SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
            sp.edit().putString(SpUtils.LOGIN_KEY,sf.format(new Date())).commit();
        }
        iViewBasic.loadListSuc(list);
    }

    @Override
    public void loadOnFailure(String msg) {
        iViewBasic.loadListFail(msg);
    }

    public void loadPageAd(Context context) {
        sp = SpUtils.getSp(context);
        String date = sp.getString(SpUtils.LOGIN_KEY, "");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String today = sf.format(new Date());
        if (StringUtils.isBlank(date) || !today.equals(date)) {//加载首页广告
            mbasic.reqCommonBackByGet(InterfaceInfo.FIRSTAD_URL, context, null, "list", Banner.class);
        } else {//不加载
            return;
        }
    }
}

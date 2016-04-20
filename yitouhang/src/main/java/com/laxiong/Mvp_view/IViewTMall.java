package com.laxiong.Mvp_view;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.laxiong.entity.Product;
import com.laxiong.entity.Profit;
import com.laxiong.entity.TMall_Ad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiejin on 2016/4/17.
 * Types IViewTMall.java
 */
public interface IViewTMall {
    public void fillVPData(List<TMall_Ad> alist);
    public void fillPListData(List<Product> plist);
}

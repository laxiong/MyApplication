package com.laxiong.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by xiejin on 2016/5/3.
 * Types CmViewPagerAdapter.java
 */
public class CmViewPagerAdapter extends PagerAdapter{
    private List<ImageView> listimg;
    public CmViewPagerAdapter(List<ImageView> list){
        this.listimg=list;
    }
    @Override
    public int getCount() {
        return listimg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=listimg.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view=listimg.get(position);
        container.removeView(view);
    }
}

package com.laxiong.Mvp_presenter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.laxiong.Mvp_view.IViewTMall;

import java.util.ArrayList;

/**
 * Created by xiejin on 2016/4/17.
 * Types TMall_Presenter.java
 */
public class TMall_Presenter {
    private IViewTMall ivewtmall;
    public TMall_Presenter(IViewTMall ivewtmall){
        this.ivewtmall=ivewtmall;
    }
    public void reqLoadPagerAdaper(){
        //异步请求
        ivewtmall.loadPageAdapter(new ArrayList<ImageView>());
    }
    public PagerAdapter getPageAdapter(final ArrayList<ImageView> ivlist){
        if(ivlist==null||ivlist.size()==0)
            return null;
        return new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE>>2;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                position %= ivlist.size();
                if (position<0){
                    position = ivlist.size()+position;
                }
                ImageView view = ivlist.get(position);
                ViewParent vp =view.getParent();
                if (vp!=null){
                    ViewGroup parent = (ViewGroup)vp;
                    parent.removeView(view);
                }
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        };
    }
}

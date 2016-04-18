package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.laxiong.Activity.WelCenterActivity;
import com.laxiong.Adapter.RedPaper;
import com.laxiong.Mvp_view.IViewWelcenter;
import com.laxiong.yitouhang.R;

import java.util.ArrayList;

/**
 * Created by xiejin on 2016/4/14.
 * Types WelCenter_Presenter.java
 */
public class WelCenter_Presenter {
    private IViewWelcenter iviewcenter;
    private boolean isbottom;

    public WelCenter_Presenter(IViewWelcenter iviewcenter) {
        this.iviewcenter = iviewcenter;
    }

    public ArrayList<RedPaper> reqRedPaperList(int pagenum, boolean isused) {
        ArrayList<RedPaper> listdata = new ArrayList<RedPaper>();
        listdata.add(new RedPaper("注册红包", "2016-10-30", "没有什么名次", isused ? RedPaper.UsetypeEnum.USED.getVal() : RedPaper.UsetypeEnum.UNUSED.getVal(), 10));
        listdata.add(new RedPaper("呵呵红包", "2016-10-30", "没有什么名次", isused ? RedPaper.UsetypeEnum.USED.getVal() : RedPaper.UsetypeEnum.UNUSED.getVal(), 50));
        listdata.add(new RedPaper("注册红包", "2016-10-30", "没有什么名次", isused ? RedPaper.UsetypeEnum.USED.getVal() : RedPaper.UsetypeEnum.UNUSED.getVal(), 100));
        listdata.add(new RedPaper("注册红包", "2016-10-30", "没有什么名次", isused ? RedPaper.UsetypeEnum.USED.getVal() : RedPaper.UsetypeEnum.UNUSED.getVal(), 200));
        listdata.addAll(listdata);
        iviewcenter.setMaxPage(2);
        return listdata;
    }

    private void loadEmptyView(Context context) {
        TextView emptyview = new TextView(context);
        emptyview.setGravity(Gravity.CENTER);
        emptyview.setText("暂时没有数据");
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        emptyview.setLayoutParams(params);
        iviewcenter.setEmptyView(emptyview);
    }

    public void initUnusedFootView(Context context) {
        if (iviewcenter.getMaxPage() == 0) {
            loadEmptyView(context);
            return;
        }
        if (iviewcenter.getMaxPage() > iviewcenter.getPageNow()) {
            loadmoreview(false, context);
        } else {
            lookExpire(context);
        }
    }

    public AbsListView.OnScrollListener getScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && isbottom) {
                    iviewcenter.setBottomTipVisibily(true);
                    isbottom = false;
                } else
                    iviewcenter.setBottomTipVisibily(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount != 0 && firstVisibleItem + visibleItemCount == totalItemCount)
                    isbottom = true;
                else
                    isbottom = false;
            }
        };
    }

    public void initUsedFootView(Context context) {
        if (iviewcenter.getMaxPage() == 0) {
            loadEmptyView(context);
            return;
        }
        if (iviewcenter.getMaxPage() > iviewcenter.getPageNow()) {
            loadmoreview(true, context);
        } else {
            showNoMoreFoot(context);
        }
    }

    public void showNoMoreFoot(Context context) {
        View footview_nomore = LayoutInflater.from(context).inflate(R.layout.footview_nomore, null);
        iviewcenter.addFootView(footview_nomore);
    }

    private void lookExpire(final Context context) {
        View footview_nomore_used = LayoutInflater.from(context).inflate(R.layout.footview_nomoreused, null);
        final TextView tv_expire = (TextView) footview_nomore_used.findViewById(R.id.tv_clickmore);
        tv_expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WelCenterActivity.class);
                intent.putExtra("used", true);
                context.startActivity(intent);
            }
        });
        iviewcenter.addFootView(footview_nomore_used);
    }

    private void loadmoreview(final boolean isused, Context context) {
        View footview_more = LayoutInflater.from(context).inflate(R.layout.footview_loadmore, null);
        TextView tv = (TextView) footview_more.findViewById(R.id.tv_loadmore);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iviewcenter.addList(false, isused);
            }
        });
        iviewcenter.addFootView(footview_more);
    }
}

package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.laxiong.Activity.WelCenterActivity;
import com.laxiong.Adapter.RedPaper;
import com.laxiong.Mvp_model.Model_RedPaper;
import com.laxiong.Mvp_view.IViewWelcenter;
import com.gongshidai.mistGSD.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiejin on 2016/4/14.
 * Types WelCenter_Presenter.java
 */
public class WelCenter_Presenter implements Model_RedPaper.OnLoadPaperListener {
    private IViewWelcenter iviewcenter;
    private boolean isbottom;
    private Model_RedPaper mpaper;
    private List<RedPaper> list;
    private Context context;
    private static final int PAGECOUNT = 10;

    public WelCenter_Presenter(IViewWelcenter iviewcenter) {
        this.iviewcenter = iviewcenter;
        mpaper = new Model_RedPaper();
    }

    public void loadListData(int pagenum, boolean isused, Context context) {
        if (pagenum == 1) {
            mpaper.loadPaperList(isused, context, this);
        } else {
            int start = (pagenum - 1) * 10;
            int end = iviewcenter.getMaxPage() == pagenum ? list.size() : pagenum * 10;
            iviewcenter.addList(false, isused, list.subList(start, end));
        }

    }

    public void loadAll(Context context) {
        this.context = context;
        mpaper.loadAllList(context, this);
    }

    @Override
    public void onSuccess(List<RedPaper> list, boolean isused, boolean isAll) {
        if (isAll) {
            if (list != null && list.size() > 0) {
                iviewcenter.loadAll(list);
            } else {
                loadEmptyView();
            }
            return;
        }
        this.list = list;
        if (list.size() == 0) {
            iviewcenter.addList(true, isused, new ArrayList<RedPaper>());
        } else {
            iviewcenter.setMaxPage((list.size() / (PAGECOUNT + 1)) + 1);
            iviewcenter.addList(true, isused, list.subList(0, list.size() > 10 ? 10 : list.size()));
        }
    }

    @Override
    public void onFailure(String msg) {
        iviewcenter.reqListFailure(msg);
    }

    private void loadEmptyView() {
        iviewcenter.setEmptyView();
    }

    public void initUnusedFootView(Context context) {
        if (iviewcenter.getMaxPage() == 0) {
            loadEmptyView();
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
            loadEmptyView();
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

    private void loadmoreview(final boolean isused, final Context context) {
        View footview_more = LayoutInflater.from(context).inflate(R.layout.footview_loadmore, null);
        TextView tv = (TextView) footview_more.findViewById(R.id.tv_loadmore);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iviewcenter.setPageNow(iviewcenter.getPageNow() + 1);
                loadListData(iviewcenter.getPageNow(), isused, context);
            }
        });
        iviewcenter.addFootView(footview_more);
    }
}

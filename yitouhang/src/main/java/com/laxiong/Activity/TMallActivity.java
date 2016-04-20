package com.laxiong.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.laxiong.Adapter.PaperYuan;
import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Mvp_presenter.TMall_Presenter;
import com.laxiong.Mvp_presenter.UserCount_Presenter;
import com.laxiong.Mvp_view.IViewCount;
import com.laxiong.Mvp_view.IViewTMall;
import com.laxiong.Utils.DensityUtils;
import com.laxiong.View.CommonActionBar;
import com.laxiong.View.CustomGridView;
import com.laxiong.entity.Product;
import com.laxiong.entity.TMall_Ad;
import com.laxiong.entity.User;
import com.laxiong.yitouhang.R;

import java.util.ArrayList;
import java.util.List;

public class TMallActivity extends BaseActivity implements View.OnClickListener, IViewTMall, IViewCount {
    private CustomGridView gv_list;
    private ScrollView sl;
    private TMall_Presenter presenter;
    private UserCount_Presenter presenterCount;
    private RelativeLayout rl_yibi, rl_rule;
    private ViewPager vp_ad;
    private List<Product> plist;
    private List<ImageView> alist;
    private CommonActionBar actionbar;
    private LinearLayout ll_dot;
    private int lastposition = 0;
    private int nowposition = 0;
    private PagerAdapter adapter;
    private Handler handler = new Handler();
    private TextView tv_yibi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmall);
        initView();
        initData();
        initListener();
    }

    @Override
    public void getCountMsgSuc() {
        User user = YiTouApplication.getInstance().getUser();
        tv_yibi.setText(user.getScore() + "");
    }

    @Override
    public void getCountMsgFai() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void initView() {
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
        sl = (ScrollView) findViewById(R.id.sl);
        rl_yibi = (RelativeLayout) findViewById(R.id.rl_yibi);
        rl_rule = (RelativeLayout) findViewById(R.id.rl_rule);
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        ll_dot = (LinearLayout) findViewById(R.id.ll_dot);
        gv_list = (CustomGridView) findViewById(R.id.gv_list);
        tv_yibi = (TextView) findViewById(R.id.tv_yibi);
    }

    private void initData() {
        presenterCount=new UserCount_Presenter(this);
        User user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            presenterCount.reqUserCountMsg(this);
        } else {
            getCountMsgSuc();
        }
        alist = new ArrayList<ImageView>();
        presenter = new TMall_Presenter(this);
        sl.smoothScrollTo(0, 0);
        presenter.reqLoadPageData(this);
    }

    private void showDelayed() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vp_ad.setCurrentItem((++nowposition) % alist.size());
                showDelayed();
            }
        }, 2000);
    }

    //填充产品列表(壹币兑换列表)
    @Override
    public void fillPListData(List<Product> plist) {
        this.plist = plist;
        gv_list.setAdapter(new ReuseAdapter<Product>(TMallActivity.this, plist, R.layout.item_execrdpaper) {
            @Override
            public void convert(ViewHolder viewholder, final Product item) {
                viewholder.setTextView(R.id.tv_price, item.getTitle());
                viewholder.setTextView(R.id.tv_num, (int) (item.getPay()) + "");
                presenter.reqLoadImageView(item.getImg(), ((ImageView) viewholder.getView(R.id.ivpic)));
                TextView tv_btn_exc = viewholder.getView(R.id.tv_btn_exc);
                tv_btn_exc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TMallActivity.this, ExChangeActivity.class);
                        int num= (int)(item.getPay() / 100);
                        intent.putExtra("type",num);
                        intent.putExtra("id",item.getId());
                        intent.putExtra("url",item.getImg());
                        TMallActivity.this.startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCountMsgSuc();
    }

    //填充viewpager广告栏图片
    @Override
    public void fillVPData(List<TMall_Ad> list) {
        alist = initImageList(list);
        if (alist != null && alist.size() > 0) {
            adapter = presenter.getPageAdapter(alist);
            if (adapter != null)
                vp_ad.setAdapter(adapter);
        }
        showDelayed();
    }

    //赋值imagelist数据并填充dot
    public List<ImageView> initImageList(List<TMall_Ad> list) {
        if (list.size() == 0)
            return null;
        for (int i = 0; i < list.size(); i++) {
            final TMall_Ad item = list.get(i);
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            presenter.reqLoadImageView(item.getImageurl(), iv);
            alist.add(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TMallActivity.this, WebViewActivity.class);
                    intent.putExtra("url", item.getHref());
                    startActivity(intent);
                }
            });
            TextView tv = new TextView(this);
            setAdItem(tv, i == 0 ? true : false);
            ll_dot.addView(tv);
        }
        return alist;
    }

    //设置dot状态
    private void setAdItem(TextView tv, boolean isSelect) {
        tv.setBackgroundResource(R.drawable.shape_rec_border);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(this, isSelect ? 20 : 10), DensityUtils.dp2px(this, 5));
        params.setMargins(5, 0, 0, 0);
        tv.setLayoutParams(params);
    }

    private void initListener() {
        actionbar.setBackListener(this);
        rl_yibi.setOnClickListener(this);
        rl_rule.setOnClickListener(this);
        vp_ad.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (lastposition != position)
                    setAdItem((TextView) ll_dot.getChildAt(lastposition), false);
                setAdItem((TextView) ll_dot.getChildAt(position), true);
                lastposition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_yibi:
                intent = new Intent(TMallActivity.this, MyYiBiActivity.class);
                break;
            case R.id.rl_rule:
                intent = new Intent(TMallActivity.this, RpExplainActivity.class);
                break;
        }
        TMallActivity.this.startActivity(intent);
    }
}

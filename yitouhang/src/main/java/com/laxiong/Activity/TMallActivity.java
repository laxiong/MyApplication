package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gongshidai.mistGSD.R;
import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Mvp_presenter.TMall_Presenter;
import com.laxiong.Mvp_presenter.UserCount_Presenter;
import com.laxiong.Mvp_view.IViewCount;
import com.laxiong.Mvp_view.IViewTMall;
import com.laxiong.Utils.DensityUtils;
import com.laxiong.View.CommonActionBar;
import com.laxiong.View.CustomFlipper;
import com.laxiong.View.CustomGridView;
import com.laxiong.entity.Product;
import com.laxiong.entity.ShareInfo;
import com.laxiong.entity.TMall_Ad;
import com.laxiong.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TMallActivity extends BaseActivity implements View.OnClickListener,
        IViewTMall, IViewCount {
    private CustomGridView gv_list;
    private ScrollView sl;
    private TMall_Presenter presenter;
    private UserCount_Presenter presenterCount;
    private RelativeLayout rl_yibi, rl_rule, rl_order;
    private List<Product> plist;
    private List<TMall_Ad> adlist;
    private List<ImageView> alist;
    private CommonActionBar actionbar;
    private LinearLayout ll_dot;
    private TextView tv_yibi;
    private CustomFlipper vf_ad;
    private static final int AD_INTERVAL = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmall);
        initView();
        initData();
        initListener();
        initFlipper();
    }

    private void initFlipper() {
        vf_ad.setFlipInterval(AD_INTERVAL);
        vf_ad.startFlipping();
        vf_ad.setListener(new CustomFlipper.InterFlipperAd() {
            @Override
            public void changePoint(int position, boolean flag) {
                setAdItem((TextView) ll_dot.getChildAt(position), flag);
            }

            @Override
            public int getListSize() {
                return alist.size();
            }

            @Override
            public void onItemClick(int position) {
                TMall_Ad item = adlist.get(position);
                Intent intent = new Intent(TMallActivity.this, WebViewActivity.class);
                Bundle bundle=new Bundle();
                User user=YiTouApplication.getInstance().getUser();
                String id=item.getHref()+"?user_id="+(user==null?"":user.getId());
                intent.putExtra("url",item.getHref()+"?id="+(user==null?"":user.getId()));
                bundle.putSerializable("banner",new ShareInfo(item.getTitle(),item.getContent(),item.getShareimageurl(),id));
                intent.putExtra("needshare",true);
                intent.putExtra("title",item.getTitle());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
        vf_ad = (CustomFlipper) findViewById(R.id.vf_ad);
        sl = (ScrollView) findViewById(R.id.sl);
        rl_yibi = (RelativeLayout) findViewById(R.id.rl_yibi);
        rl_rule = (RelativeLayout) findViewById(R.id.rl_rule);
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        ll_dot = (LinearLayout) findViewById(R.id.ll_dot);
        gv_list = (CustomGridView) findViewById(R.id.gv_list);
        tv_yibi = (TextView) findViewById(R.id.tv_yibi);
        rl_order = (RelativeLayout) findViewById(R.id.rl_order);
    }

    private void initData() {
        presenterCount = new UserCount_Presenter(this);
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

    //填充产品列表(壹币兑换列表)
    @Override
    public void fillPListData(List<Product> plist) {
        this.plist = plist;
        gv_list.setAdapter(new ReuseAdapter<Product>(TMallActivity.this, plist, R.layout.item_execrdpaper) {
            @Override
            public void convert(ViewHolder viewholder, final Product item) {
                viewholder.setText(R.id.tv_price, item.getTitle());
                viewholder.setText(R.id.tv_num, (int) (item.getPay()) + "");
                viewholder.setImageResource(R.id.ivpic,R.drawable.gongshi_mr);
//                presenter.reqLoadImageView(item.getImg(), ((ImageView) viewholder.getView(R.id.ivpic)));
                Glide.with(TMallActivity.this).load(item.getImg()).placeholder(R.drawable.gongshi_mr).into((ImageView) viewholder.getView(R.id.ivpic));
                TextView tv_btn_exc = viewholder.getView(R.id.tv_btn_exc);
                tv_btn_exc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TMallActivity.this, ExChangeActivity.class);
                        int num = (int) (item.getPay() / 100);
                        intent.putExtra("type", num);
                        intent.putExtra("id", item.getId());
                        intent.putExtra("url", item.getInn_img());
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
            for (ImageView iv : alist) {
                vf_ad.addView(iv);
            }
        }
    }

    //赋值imagelist数据并填充dot
    public List<ImageView> initImageList(List<TMall_Ad> list) {
        this.adlist = list;
        if (list.size() == 0)
            return null;
        for (int i = 0; i < list.size(); i++) {
            final TMall_Ad item = list.get(i);
            ImageView iv = new ImageView(this);
            iv.setImageResource(R.drawable.gongshi_banner_mr);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            presenter.reqLoadImageView(item.getImageurl(), iv);
            Glide.with(TMallActivity.this).load(item.getImageurl()).placeholder(R.drawable.gongshi_banner_mr).into(iv);
            alist.add(iv);
            TextView tv = new TextView(this);
            setAdItem(tv, i == 0 ? true : false);
            ll_dot.addView(tv);
        }
        return alist;
    }

    //设置dot状态
    private void setAdItem(TextView tv, boolean isSelect) {
        tv.setBackgroundResource(R.drawable.shape_point);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(this, isSelect ? 15 : 5), DensityUtils.dp2px(this, 5));
        params.setMargins(5, 0, 0, 0);
        tv.setLayoutParams(params);
    }

    private void initListener() {
        actionbar.setBackListener(this);
        rl_yibi.setOnClickListener(this);
        rl_rule.setOnClickListener(this);
        rl_order.setOnClickListener(this);
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
            case R.id.rl_order:
                intent = new Intent(TMallActivity.this, MyOrderActivity.class);
        }
        TMallActivity.this.startActivity(intent);
    }
}

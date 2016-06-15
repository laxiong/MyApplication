package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gongshidai.mistGSD.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Constants;
import com.laxiong.Common.Settings;
import com.laxiong.Fragment.FinancingFragment;
import com.laxiong.Fragment.FristPagerFragment;
import com.laxiong.Fragment.MySelfFragment;
import com.laxiong.Fragment.VipFinancingFragment;
import com.laxiong.Mvp_model.UpdateInfo;
import com.laxiong.Mvp_presenter.MainPage_Presenter;
import com.laxiong.Mvp_view.IViewMain;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.CommonUtils;
import com.laxiong.Utils.DialogUtils;
import com.laxiong.Utils.NotificationUtil;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.laxiong.View.PayPop;
import com.laxiong.entity.Banner;
import com.laxiong.entity.ShareInfo;
import com.laxiong.entity.User;
import com.laxiong.service.DownService;

import java.util.List;

public class MainActivity extends BaseActivity implements OnClickListener, IViewMain {
    /****
     * 主页
     */
    private RelativeLayout mFristPager, mFinancing, mMyself;  // Bottom three Button Layout
    private MainPage_Presenter presenter;
    private LinearLayout ll_wrap;
    private TextView mFristPager_tv, mFinancing_tv, mMyself_tv;
    private ImageView mFristPager_icon, mFinancing_icon, mMyself_icon;
    private PayPop dialog;
    private User mUser;
    private FristPagerFragment mFristPagerFragment = null;
    private FinancingFragment mFinancingFragment = null;
    private MySelfFragment mMySelfFragment = null;

    private FragmentManager mFragmentManager = null;
    private ImageView iv_read,iv_noread;
    private TextView mHead_title, mHead_left_select_textview;  // head Title TextView
    private FrameLayout mPersonSetting;
    private RelativeLayout mHeadLayout;
    private UpdateReceiver receiver;
    private Fragment saveFragment;
    private boolean isLogin = false; // 判断是否登录了


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFragmentManager=getFragmentManager();
        if(savedInstanceState!=null){
            mFristPagerFragment= (FristPagerFragment) mFragmentManager.findFragmentByTag("firstpage");
            mFinancingFragment= (FinancingFragment) mFragmentManager.findFragmentByTag("finace");
            mMySelfFragment= (MySelfFragment) mFragmentManager.findFragmentByTag("myself");
        }
        super.onCreate(savedInstanceState);

        WindowManager wm = this.getWindowManager();
        Settings.DISPLAY_HEIGHT = wm.getDefaultDisplay().getHeight();
        Settings.DISPLAY_WIDTH = wm.getDefaultDisplay().getWidth();
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (CommonUtils.isServiceRunning(this, "DownService")) {
            stopService(new Intent(this, DownService.class));
            NotificationUtil.cancelNoti(this, 100);
        }
    }

    @Override
    public void registerUpdateReceiver(View dialog) {
        receiver = new UpdateReceiver(dialog);
        registerReceiver(receiver, new IntentFilter("com.update.action"));
    }

    class UpdateReceiver extends BroadcastReceiver {
        private View v;

        public UpdateReceiver(View v) {
            this.v = v;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (v == null)
                return;
            int progress = intent.getIntExtra("progress", 0);
            String ns = intent.getStringExtra("nowsize");
            String ts = intent.getStringExtra("totalsize");
            TextView nowsize = (TextView) v.findViewById(R.id.loadingTask_progress);
            TextView totalsize = (TextView) v.findViewById(R.id.loadingTask_dimen);
            ProgressBar pgbar = (ProgressBar) v.findViewById(R.id.loadingTask_progressBar);
            nowsize.setText(ns);
            totalsize.setText(ts);
            pgbar.setProgress(progress);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        iv_read.setVisibility(Constants.isRead ? View.VISIBLE : View.GONE);
        iv_noread.setVisibility(Constants.isRead?View.GONE:View.VISIBLE);
        boolean flag = ValifyUtil.judgeInit(this);
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        if(saveFragment!=null&&mTransaction!=null){
            mTransaction.show(saveFragment).commit();
        }
        if (flag)
            mUser = YiTouApplication.getInstance().getUser();
    }

    @Override
    public void loadListSuc(List<Banner> list) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_firstpage, null);
        ImageView iv_ad = (ImageView) view.findViewById(R.id.iv_ad);
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        final Banner item = list.get(0);
        DialogUtils.bgalpha(MainActivity.this, 0.3f);
        iv_ad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = YiTouApplication.getInstance().getUser();
                String id = user == null ? "" : user.getId() + "";
                Bundle bundle = new Bundle();
                bundle.putSerializable("banner", new ShareInfo(item.getTitle(), item.getContent(), item.getShareimageurl(), item.getHref() + "?user_id=" + id));
                startActivity(new Intent(MainActivity.this, WebViewActivity.class)
                        .putExtras(bundle).putExtra("needshare", true).putExtra("url",
                                item.getHref() + "?id=" + id));
            }
        });
        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.bgalpha(MainActivity.this, 1.0f);
                dialog.dismiss();
                dialog = null;
            }
        });
        if (list == null || list.size() == 0)
            return;
        Glide.with(this).load(item.getImageurl()).placeholder(getResources().getDrawable(R.drawable.gongshi_mr)).into(iv_ad);
        DisplayMetrics metrix = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrix);
        int width = 3 * metrix.widthPixels / 4;
        int height = 2 * metrix.heightPixels / 3;
        dialog = new PayPop(view, width, height, this);
        dialog.showAtLocation(ll_wrap, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void loadUpdateInfo(UpdateInfo info) {
        if (info == null)
            return;
        if (info.getStatus() == 3) {//强制更新
            presenter.showForceDialog(this, info, ll_wrap);
        } else if (info.getStatus() == 2) {//建议更新
            presenter.showRecDialog(this, info, ll_wrap);
        } else {
            presenter.loadPageAd(this);//广告
        }

    }

    @Override
    public void loadListFail(String msg) {
        ToastUtil.customAlert(this, msg);
        presenter.loadPageAd(this);
    }
    @SuppressLint("NewApi")
    private void initData() {
        iv_read.setVisibility(Constants.isRead?View.GONE:View.VISIBLE);
        iv_noread.setVisibility(Constants.isRead?View.VISIBLE:View.GONE);
        presenter = new MainPage_Presenter(this);
        presenter.checkUpdate(this);
        mFragmentManager = this.getFragmentManager();
//		FragmentTransaction  mTransaction = mFragmentManager.beginTransaction();

        mFristPager.setOnClickListener(this);
        mFinancing.setOnClickListener(this);
        mMyself.setOnClickListener(this);

        initFristFragment();  //初始化第一张碎片
        boolean flag = getIntent().getBooleanExtra("jumptoinvest", false);
        if (flag) {
            mFinancing.performClick();
        }
    }

    private void initView() {
        iv_read= (ImageView) findViewById(R.id.iv_read);
        iv_noread= (ImageView) findViewById(R.id.iv_noread);
        mFristPager = (RelativeLayout) findViewById(R.id.fristpager);
        mFinancing = (RelativeLayout) findViewById(R.id.financing);
        mMyself = (RelativeLayout) findViewById(R.id.myself);

        mPersonSetting = (FrameLayout) findViewById(R.id.head_myself_details);
        mHead_left_select_textview = (TextView) findViewById(R.id.head_select_right_textview);
        mHead_title = (TextView) findViewById(R.id.head_title);

        mFristPager_icon = (ImageView) findViewById(R.id.fristpager_icon);
        mFristPager_tv = (TextView) findViewById(R.id.fristpager_tv);
        mFinancing_icon = (ImageView) findViewById(R.id.financing_icon);
        mFinancing_tv = (TextView) findViewById(R.id.financingr_tv);
        mMyself_icon = (ImageView) findViewById(R.id.myself_icon);
        mMyself_tv = (TextView) findViewById(R.id.myself_tv);
        ll_wrap = (LinearLayout) findViewById(R.id.ll_wrap);

        // head
        mHeadLayout = (RelativeLayout) findViewById(R.id.main_head_layout);

        mPersonSetting.setOnClickListener(listen);  //personal setting onClick
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        hideFragment(mTransaction);
        switch (view.getId()) {
            case R.id.financing:
                setButtomBackground(1);
                if (mFinancingFragment == null) {        // 理财页面
                    mFinancingFragment = new FinancingFragment();
                    mTransaction.add(R.id.setContent, mFinancingFragment,"finace");
                } else {
                    mTransaction.show(mFinancingFragment);
                }
                saveFragment=mFinancingFragment;
                break;
            case R.id.fristpager:
                setButtomBackground(0);
                if (mFristPagerFragment == null) {     // 首页页面
                    mFristPagerFragment = new FristPagerFragment();
                    mTransaction.add(R.id.setContent, mFristPagerFragment,"firstpage");
                } else {
                    mTransaction.show(mFristPagerFragment);
                }
                saveFragment=mFristPagerFragment;
                break;
            case R.id.myself:
                //TODO 判断是否  已经登录了
//				if(isLogin){
                if (!ValifyUtil.judgeLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    setButtomBackground(2);
                    if (mMySelfFragment == null) {         // 资产页面
                        mMySelfFragment = new MySelfFragment();
                        mTransaction.add(R.id.setContent, mMySelfFragment,"myself");
                    } else {
                        mTransaction.show(mMySelfFragment);
                    }
//				}else{
//					startActivity(new Intent(MainActivity.this,
//							RegistActivity.class));
//					
//					//TODO 之后直接显示到首页
//					setButtomBackground(0);
//					if(mFristPagerFragment == null){     // 首页页面
//						mFristPagerFragment = new FristPagerFragment();
//						mTransaction.add(R.id.setContent, mFristPagerFragment);
//					}else{
//						mTransaction.show(mFristPagerFragment);
//					}
//					
//				}
                }
                break;

        }
        mTransaction.commit();
    }

    @SuppressLint("NewApi")
    private void hideFragment(FragmentTransaction mTransaction) {
        if (mFristPagerFragment != null) {
            mTransaction.hide(mFristPagerFragment);
        }
        if (mFinancingFragment != null) {
            mTransaction.hide(mFinancingFragment);
        }
        if (mMySelfFragment != null) {
            mTransaction.hide(mMySelfFragment);
        }
        if (mVipFragment != null) {
            mTransaction.hide(mVipFragment);
        }
    }

    private void setButtomBackground(int index) {
        setInitBackground();

        //TODO 点击切换时切换的颜色和图片Buttom
        switch (index) {
            case 0:  // fristpager
                //TODO
//                mHead_gongshilicai.setText("(原共识理财)");
                mHead_left_select_textview.setVisibility(View.GONE);
                mHead_title.setVisibility(View.VISIBLE);
                mHead_title.setText(getResources().getString(R.string.appname));
                mHead_title.setTextColor(Color.parseColor("#ffffff"));
                mHeadLayout.setBackgroundColor(Color.parseColor("#EE4E42"));  // 红色

                mFristPager_icon.setImageResource(R.drawable.shouye_xz);
                mFristPager_tv.setTextColor(Color.parseColor("#FFEE4E42"));

                break;
            case 1:  // financing
                mHead_left_select_textview.setVisibility(View.VISIBLE);
                mHead_left_select_textview.setText("VIP");                //  show VIP
                mHead_left_select_textview.setTextColor(Color.parseColor("#ffffff"));
                mHead_title.setVisibility(View.VISIBLE);
                mHead_title.setText("理财");
                mHead_title.setTextColor(Color.parseColor("#ffffff"));
                mHead_left_select_textview.setOnClickListener(listen);
                mHeadLayout.setBackgroundColor(Color.parseColor("#EE4E42"));  // 红色

                mFinancing_icon.setImageResource(R.drawable.licai_xz);
                mFinancing_tv.setTextColor(Color.parseColor("#FFEE4E42"));

                break;
            case 2:  // myself
                mHead_left_select_textview.setVisibility(View.VISIBLE);
                mHead_left_select_textview.setText("日历账单");
                mHead_left_select_textview.setTextColor(Color.parseColor("#ffffff"));
                mHead_title.setVisibility(View.VISIBLE);
                mHead_title.setText("资产");
                mHead_title.setTextColor(Color.parseColor("#ffffff"));
                mHead_left_select_textview.setOnClickListener(listen);
                mHeadLayout.setBackgroundColor(Color.parseColor("#EE4E42"));  // 红色

                mMyself_icon.setImageResource(R.drawable.zichan_xz);
                mMyself_tv.setTextColor(Color.parseColor("#FFEE4E42"));

                break;

        }
    }

    // set to initbackground
    private void setInitBackground() {
        mFristPager_icon.setImageResource(R.drawable.shouye_mr);
        mFristPager_tv.setTextColor(Color.parseColor("#A6A6A6"));
        mFinancing_icon.setImageResource(R.drawable.licai_mr);
        mFinancing_tv.setTextColor(Color.parseColor("#A6A6A6"));
        mMyself_icon.setImageResource(R.drawable.zichan_mr);
        mMyself_tv.setTextColor(Color.parseColor("#A6A6A6"));
    }

    @SuppressLint("NewApi")
    private void initFristFragment() {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mFristPagerFragment = new FristPagerFragment();
        mTransaction.add(R.id.setContent, mFristPagerFragment,"firstpage");
        saveFragment=mFristPagerFragment;
        mTransaction.commit();
        setButtomBackground(0);
    }

    OnClickListener listen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head_myself_details:
                    if (ValifyUtil.judgeLogin()) {
                        startActivity(new Intent(MainActivity.this,
                                CountSettingActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }

                    break;
                case R.id.head_select_right_textview:
                    if (mHead_left_select_textview == null)
                        return;
                    if (mHead_left_select_textview.getText().toString().equals("VIP")) {
                        User mUser = YiTouApplication.getInstance().getUser();
                        if (mUser != null) {
//                            if (mUser.is_vip()){   // 是VIP
                            vipAndFinance(2);
                            financingToVipEachOther(2);
//                            }else { // 显示怎么成为VIP
//                                showToBeVipMenthod();
//                            }
                        } else {
                            Toast.makeText(MainActivity.this, "请完成登录", Toast.LENGTH_SHORT).show();
                        }

                    } else if (mHead_left_select_textview.getText().toString().equals("理财")) {

                        vipAndFinance(1);
                        financingToVipEachOther(1);

                    } else if (mHead_left_select_textview.getText().toString().equals("日历账单")) {
                        startActivity(new Intent(MainActivity.this,
//                                CalanderCountActivity.class));
//                                CalanderHorizontalActivity.class));
                                BillCalendarActivity.class));
                    } else {
                        return;
                    }
                    break;
            }
        }
    };

    private VipFinancingFragment mVipFragment;

    //change each other financing and vip
    private void financingToVipEachOther(int tag) {
        switch (tag) {
            case 1:    //理财
                mHead_title.setText("理财");
                mHead_left_select_textview.setText("VIP");
                break;
            case 2:     //vip
                mHead_title.setText("VIP");
                mHead_left_select_textview.setText("理财");
                break;
        }
    }

    //碎片切换
    @SuppressLint("NewApi")
    private void vipAndFinance(int tag) {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        hideFragment(mTransaction);
        switch (tag) {
            case 1:  //理财
                if (mFinancingFragment == null) {        // 理财页面
                    mFinancingFragment = new FinancingFragment();
                    mTransaction.add(R.id.setContent, mFinancingFragment);
                } else {
                    mTransaction.show(mFinancingFragment);
                }

                break;
            case 2:  //vip
                if (mVipFragment == null) {
                    mVipFragment = new VipFinancingFragment();
                    mTransaction.add(R.id.setContent, mVipFragment);
                } else {
                    mTransaction.show(mVipFragment);
                }

                break;
        }
        mTransaction.commit();
    }

    //显示怎么变成VIP的方法
    PopupWindow mVipWinds;
    View mshowV;

    private void showToBeVipMenthod() {
        mshowV = LayoutInflater.from(this).inflate(R.layout.notice_show_vip_popwindow, null);
        mVipWinds = new PopupWindow(mshowV, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        mVipWinds.setTouchable(true);
        mVipWinds.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mVipWinds.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mVipWinds.showAtLocation(mshowV, Gravity.BOTTOM, 0, 0);
    }


}

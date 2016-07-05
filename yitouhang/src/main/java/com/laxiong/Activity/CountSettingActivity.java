package com.laxiong.Activity;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carfriend.mistCF.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.Login_Presenter;
import com.laxiong.Mvp_presenter.UserCount_Presenter;
import com.laxiong.Mvp_view.IViewBasicObj;
import com.laxiong.Mvp_view.IViewCount;
import com.laxiong.Utils.DialogUtils;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.PayPop;
import com.laxiong.entity.User;
import com.loopj.android.network.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.UUID;

import cn.udesk.UdeskSDKManager;

public class CountSettingActivity extends BaseActivity implements OnClickListener, IViewBasicObj<User> {
    /****
     * 账户设置
     */
    private RelativeLayout mCount, rl_test, mPswdControl, mConnectKefu, mMessage, rl_version, mMyBankCard, mSafeProtect, mAboutUs, mGuanData;
    private FrameLayout mBack;
    private View v_read;
    private Login_Presenter presenter;
    private TextView tv_username, tv_version, tv_unread, tv_dstatus;
    private User user;
    private PayPop dialog;

    private String UDESK_DOMAIN = "cheyou.udesk.cn";
    private String UDESK_SECRETKEY = "8a50f1153c1ae8cb0ee258787effb264";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_setting_layout);
        getBankInfo();
        initView();
        initData();
        if (user != null)
            showVip();
        UdeskSDKManager.getInstance().initApiKey(CountSettingActivity.this,UDESK_DOMAIN,UDESK_SECRETKEY);  // 云平台客服
        UdeskSDKManager.getInstance().setUserInfo(CountSettingActivity.this, UUID.randomUUID().toString(),null);// 设置用户信息

    }

    @Override
    public void loadObjSuc(User obj) {
        user = obj;
        if (user != null) {
            tv_username.setText(isBlank(user.getNickname()) ? user.getNamed() : user.getNickname());
        }
    }

    @Override
    public void loadObjFail(String msg) {

    }

    private void initData() {
        user = YiTouApplication.getInstance().getUser();
        presenter = new Login_Presenter(this);
        presenter.reqUserMsg(this);
        mCount.setOnClickListener(this);
        mPswdControl.setOnClickListener(this);
        mConnectKefu.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mMessage.setOnClickListener(this);
        rl_version.setOnClickListener(this);
        mMyBankCard.setOnClickListener(this);
        mSafeProtect.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mGuanData.setOnClickListener(this);
        rl_test.setOnClickListener(this);
        try {
            tv_version.setText("V-" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        judgeAccess();
        resetMsgState();

    }

    private void resetMsgState() {
        if (Constants.isRead) {
            v_read.setVisibility(View.GONE);
            tv_unread.setText("消息已读");
        } else {
            v_read.setVisibility(View.VISIBLE);
            tv_unread.setText("有消息未读");
        }
    }

    private ImageView mNameLine;
    private ImageView mBankAssow;
    private TextView mBankNum;
    private TextView mBankName;
    private RelativeLayout mRl_NoBindCard;
    private View mLine;
    private ImageView mImgLogo;

    // 显示VIP用户的金色的
    private void showVip() {
        boolean isVip = user.is_vip();
        if (user.getBankcount() != 0) {
            mRl_NoBindCard.setVisibility(View.GONE);
            mMyBankCard.setVisibility(View.VISIBLE);
        } else {
            mRl_NoBindCard.setVisibility(View.VISIBLE);
            mMyBankCard.setVisibility(View.GONE);
            mRl_NoBindCard.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!user.is_idc()) {
                        startActivity(new Intent(CountSettingActivity.this,
                                TrueNameActivity1.class));
                    } else if (!user.isPay_pwd()) {
                        startActivity(new Intent(CountSettingActivity.this,
                                TrueNameActivity2.class));
                    } else if (user.getBankcount() == 0) {
                        startActivity(new Intent(CountSettingActivity.this,
                                TrueNameActivity3.class));
                    }
                }
            });
        }

        if (isVip) {
            mNameLine.setImageResource(R.drawable.img_countseting_icon_vip_line);
            mBankAssow.setImageResource(R.drawable.img_bank_vip_arrow);
            tv_username.setTextColor(Color.parseColor("#FFFFDFAA"));
            mBankNum.setTextColor(Color.parseColor("#FFFFDFAA"));
            mBankName.setTextColor(Color.parseColor("#FFFFDFAA"));
            mLine.setBackgroundColor(Color.parseColor("#FFFFDFAA"));
            mImgLogo.setVisibility(View.VISIBLE);
        } else {
            mNameLine.setImageResource(R.drawable.img_countseting_icon_line);
            mBankAssow.setImageResource(R.drawable.img_bank_arrow);
            tv_username.setTextColor(Color.parseColor("#ffffff"));
            mBankNum.setTextColor(Color.parseColor("#ffffff"));
            mBankName.setTextColor(Color.parseColor("#ffffff"));
            mLine.setBackgroundColor(Color.parseColor("#ffffff"));
            mImgLogo.setVisibility(View.INVISIBLE);
        }
    }

    //获取成功 设置用户信息
    @Override
    protected void onRestart() {
        super.onRestart();
        judgeAccess();
        if (user != null) {
            showVip();
            tv_username.setText(isBlank(user.getNickname()) ? user.getNamed() : user.getNickname());
        }
    }

    private void judgeAccess() {
        user = YiTouApplication.getInstance().getUser();
        if (user != null) {
            tv_dstatus.setText(user.getAssess());
            rl_test.setEnabled("未评估".equals(user.getAssess()) ? true : false);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public boolean isBlank(String msg) {
        if (msg.trim() == null || "".equals(msg.trim()))
            return true;
        return false;
    }

    private void initView() {

        mNameLine = (ImageView) findViewById(R.id.name_line);
        mBankAssow = (ImageView) findViewById(R.id.bank_arrow);
        mBankName = (TextView) findViewById(R.id.tv_bankname);
        mBankNum = (TextView) findViewById(R.id.tv_banknum);
        mRl_NoBindCard = (RelativeLayout) findViewById(R.id.no_bandcard);
        mLine = findViewById(R.id.lines);
        mImgLogo = (ImageView) findViewById(R.id.vip_logo);

        mCount = (RelativeLayout) findViewById(R.id.count_setting);
        mPswdControl = (RelativeLayout) findViewById(R.id.pswdControl);
        mConnectKefu = (RelativeLayout) findViewById(R.id.connectKefu);
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        mMessage = (RelativeLayout) findViewById(R.id.message);
        tv_username = (TextView) findViewById(R.id.tv_username);
        TextView mText = (TextView) findViewById(R.id.title);
        tv_version = (TextView) findViewById(R.id.tv_version);
        rl_version = (RelativeLayout) findViewById(R.id.rl_version);
        tv_unread = (TextView) findViewById(R.id.tv_unread);
        mMyBankCard = (RelativeLayout) findViewById(R.id.mybankcard);
        rl_test = (RelativeLayout) findViewById(R.id.rl_test);
        tv_dstatus = (TextView) findViewById(R.id.tv_dstatus);
        v_read = findViewById(R.id.v_read);
        mText.setText("账户");
        mSafeProtect = (RelativeLayout) findViewById(R.id.rel_safeprotect); //安全保障
        mAboutUs = (RelativeLayout) findViewById(R.id.rel_aboutus);//关于我们
        mGuanData = (RelativeLayout) findViewById(R.id.guandata); //官方数据
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.count_setting:
                startActivity(new Intent(this, PersonalSettingActivity.class));
                break;
            case R.id.pswdControl:            /**密码管理 */
                startActivity(new Intent(this,
                        PswdConturalActivity.class));
                break;
            case R.id.connectKefu:            /**联系客服 */
                showConnectKefuSelectType();
                break;

            case R.id.back_layout:
                this.finish();
                break;

            case R.id.message:
                startActivity(new Intent(this,
                        MessageActivity.class));
                if (!Constants.isRead) {
                    Constants.isRead = true;
                    resetMsgState();
                }
                break;
            case R.id.rl_version:
                Intent intent = new Intent(CountSettingActivity.this, VersionManageActivity.class);
//                Intent intent = new Intent(CountSettingActivity.this, WebViewActivity.class);
//                intent.putExtra("url", InterfaceInfo.UPDATE_URL);
                startActivity(intent);
                break;
            case R.id.mybankcard:
                startActivity(new Intent(this,
                        MyBandBankCardActivity.class));
                break;
            case R.id.rel_safeprotect:    /**安全保障**/

                startActivity(new Intent(CountSettingActivity.this, WebViewActivity.class).
                        putExtra("url", "https://licai.gongshidai.com/wap/public/ytbank/yt.safe.html").
                        putExtra("title", "安全保障"));
                break;
            case R.id.rel_aboutus:    /**关于我们**/

                startActivity(new Intent(CountSettingActivity.this, WebViewActivity.class).
                        putExtra("url", "https://licai.gongshidai.com/wap/public/ytbank/yt.aboutus.html").
                        putExtra("title", "关于我们"));
                break;
            case R.id.guandata:     /**官方数据**/

                startActivity(new Intent(CountSettingActivity.this, WebViewActivity.class).
                        putExtra("url", "https://licai.gongshidai.com/wap/public/ytbank/yt.data.html ").
                        putExtra("title", "官方数据"));
                break;
            case R.id.rl_test:
                User user = YiTouApplication.getInstance().getUser();
                Intent intent2 = new Intent(this, WebViewActivity.class);
                intent2.putExtra("url", InterfaceInfo.EVALUATE_URL + "?id=" + user.getId());
                intent2.putExtra("title", "风险评估");
                startActivity(intent2);
                break;
        }

    }

    //显示联系客服的方法
    private PopupWindow mPopWindows;
    private View KefuSelectView;

    private void showConnectKefuSelectType() {

        KefuSelectView = LayoutInflater.from(this).inflate(R.layout.kefu_popwindow, null);

        TextView onLineBtn = (TextView) KefuSelectView.findViewById(R.id.onlinekefu);
        RelativeLayout kefuTelBtn = (RelativeLayout) KefuSelectView.findViewById(R.id.kefutel);
        TextView contcelBtn = (TextView) KefuSelectView.findViewById(R.id.concel_kefu);

        onLineBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                connectKeFu();
                if (mPopWindows != null && mPopWindows.isShowing()) {
                    mPopWindows.dismiss();
                    mPopWindows = null;
                }
            }
        });

        kefuTelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                takePhoneNum();
                if (mPopWindows != null && mPopWindows.isShowing()) {
                    mPopWindows.dismiss();
                    mPopWindows = null;
                }
            }
        });

        contcelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (mPopWindows != null && mPopWindows.isShowing()) {
                    mPopWindows.dismiss();
                    mPopWindows = null;
                }
            }
        });

        mPopWindows = new PopupWindow(KefuSelectView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        mPopWindows.setTouchable(true);
        mPopWindows.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mPopWindows.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mPopWindows.showAtLocation(KefuSelectView, Gravity.BOTTOM, 0, 0);
    }

    // 打客服电话的
    private void takePhoneNum() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4001515805"));
        this.startActivity(intent);
    }

    // ConnectWithKefu
    // 联系客服
    private void connectKeFu() {
        UdeskSDKManager.getInstance().toLanuchChatAcitvity(CountSettingActivity.this); //开始聊天

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //获取卡号
    private void getBankInfo() {
        HttpUtil.get(InterfaceInfo.BASE_URL + "/bank", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            Log.i("wk", "BANKinfo" + response);
                            mBankName.setText(response.getString("name") + "(尾号" + response.getInt("snumber") + ")");
                            mBankNum.setText(response.getString("cardnum"));
                        } else {
                            Toast.makeText(CountSettingActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception E) {
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(CountSettingActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        }, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
                .getUserLogin().getToken()));
    }


}

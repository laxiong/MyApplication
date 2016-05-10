package com.laxiong.Activity;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appkefu.lib.interfaces.KFAPIs;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.utils.KFLog;
import com.appkefu.smack.util.StringUtils;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.UserCount_Presenter;
import com.laxiong.Mvp_view.IViewCount;
import com.laxiong.entity.User;
import com.laxiong.yitouhang.R;

public class CountSettingActivity extends BaseActivity implements OnClickListener, IViewCount {
    /****
     * 账户设置
     */
    private RelativeLayout mCount, rl_test, mPswdControl, mConnectKefu, mMessage, rl_version, mMyBankCard;
    private FrameLayout mBack;
    private View v_read;
    private UserCount_Presenter presenter;
    private TextView tv_username, tv_version, tv_unread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_setting_layout);
        initView();
        initData();
        KFAPIs.visitorLogin(this); //微客服平台的事件
    }

    private void initData() {
        presenter = new UserCount_Presenter(this);
        presenter.reqUserCountMsg(this);
        mCount.setOnClickListener(this);
        mPswdControl.setOnClickListener(this);
        mConnectKefu.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mMessage.setOnClickListener(this);
        rl_version.setOnClickListener(this);
        mMyBankCard.setOnClickListener(this);
        rl_test.setOnClickListener(this);
        try {
            tv_version.setText("V-" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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

    //获取成功 设置用户信息
    @Override
    public void getCountMsgSuc() {
        User user = YiTouApplication.getInstance().getUser();
        if (user != null)
            tv_username.setText(isBlank(user.getNickname()) ? user.getNamed() : user.getNickname());
    }

    public boolean isBlank(String msg) {
        if (msg.trim() == null || "".equals(msg.trim()))
            return true;
        return false;
    }

    @Override
    public void getCountMsgFai() {

    }

    private void initView() {
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
        v_read = findViewById(R.id.v_read);
        mText.setText("账户");
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
                Intent intent = new Intent(CountSettingActivity.this, WebViewActivity.class);
                intent.putExtra("url", InterfaceInfo.UPDATE_URL);
                startActivity(intent);
                break;
            case R.id.mybankcard:
                startActivity(new Intent(this,
                        MyBandBankCardActivity.class));
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
//                Toast.makeText(CountSettingActivity.this, "在线客服", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(CountSettingActivity.this, "客服电话：400-0888-888", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:10086"));
        this.startActivity(intent);
    }

    // ConnectWithKefu
    // 联系客服
    private void connectKeFu() {
        startChat();
    }

    /**
     * 监听：连接状态、即时通讯消息、客服在线状态 (微客服)
     */
    private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 监听：连接状态
            if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED)) // 监听链接状态
            {
                updateStatus(intent.getIntExtra("new_state", 0));
            }
            // 监听：即时通讯消息
            else if (action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED)) // 监听消息
            {
                // 消息内容
                String body = intent.getStringExtra("body");
                // 消息来自于
                String from = StringUtils.parseName(intent.getStringExtra("from"));
                KFLog.d("消息来自于:" + from + " 消息内容:" + body);
            }
            // 客服工作组在线状态
            else if (action.equals(KFMainService.ACTION_XMPP_WORKGROUP_ONLINESTATUS)) {
                String fromWorkgroupName = intent.getStringExtra("from");

                String onlineStatus = intent.getStringExtra("onlinestatus");

                KFLog.d("客服工作组:" + fromWorkgroupName + " 在线状态:" + onlineStatus);// online：在线；offline:
            }

        }
    };

    // 根据监听到的连接变化情况更新界面显示
    private void updateStatus(int status) {

        switch (status) {
            case KFXmppManager.CONNECTED:    //连接成功
                // mTitle.setText("微客服");
                // 查询客服工作组在线状态，返回结果在BroadcastReceiver中返回
                KFAPIs.checkKeFuIsOnlineAsync("YTHang"  //注意：此处应该填写 工作组 名称，具体参见第一步
                        , this);

                break;
            case KFXmppManager.DISCONNECTED:  //连接失败
                // mTitle.setText("微客服(未连接)");
                break;
            case KFXmppManager.CONNECTING:   //正在连接中...
                // mTitle.setText("微客服(登录中...)");
                break;
            case KFXmppManager.DISCONNECTING:
                // mTitle.setText("微客服(登出中...)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
                // mTitle.setText("微客服(等待中)");
                break;
            default:
                throw new IllegalStateException();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        //监听网络连接变化情况
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //监听消息
        intentFilter.addAction(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED);
        //工作组在线状态
        intentFilter.addAction(KFMainService.ACTION_XMPP_WORKGROUP_ONLINESTATUS);
        registerReceiver(mXmppreceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mXmppreceiver);
    }

    /**
     * 1.咨询人工客服 (在线 微客服)
     */
    private void startChat() {
        // Bitmap kefuAvatarBitmap =
        // BitmapFactory.decodeResource(getResources(), R.drawable.kefu);
        // Bitmap userAvatarBitmap =
        // BitmapFactory.decodeResource(getResources(), R.drawable.user);
        KFAPIs.startChat(CountSettingActivity.this, "wangkingyitouhang", // 1.
                // 客服工作组ID(请务必保证大小写一致)，请在管理后台分配
                "客服妹妹", // 2. 会话界面标题，可自定义
                null, // 3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
                // 如果不想发送此信息，可以将此信息设置为""或者null
                false, // 4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
                // 请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
                // 显示:true, 不显示:false
                5, // 5. 默认显示消息数量
                null, // 6. 修改默认客服头像，如果不想修改默认头像，设置此参数为null
                null, // 7. 修改默认用户头像, 如果不想修改默认头像，设置此参数为null
                false, // 8. 默认机器人应答
                false, // 9. 是否强制用户在关闭会话的时候 进行“满意度”评价， true:是， false:否
                null);
    }
}

package com.laxiong.Activity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
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
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.UserCount_Presenter;
import com.laxiong.Mvp_view.IViewCount;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.User;
import com.laxiong.yitouhang.R;

public class CountSettingActivity extends BaseActivity implements OnClickListener, IViewCount {
    /****
     * 账户设置
     */
    private RelativeLayout mCount, mPswdControl, mConnectKefu, mMessage, rl_version;
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
            tv_username.setText(StringUtils.isBlank(user.getNickname()) ? user.getNamed() : user.getNickname());
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
                if (!Constants.isRead) {
                    Constants.isRead = true;
                    resetMsgState();
                }
                startActivity(new Intent(this,
                        MessageActivity.class));
                break;
            case R.id.rl_version:
                Intent intent = new Intent(CountSettingActivity.this, WebViewActivity.class);
                intent.putExtra("url", InterfaceInfo.UPDATE_URL);
                startActivity(intent);
                break;
        }

    }

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
                Toast.makeText(CountSettingActivity.this, "在线客服", Toast.LENGTH_SHORT).show();
            }
        });

        kefuTelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(CountSettingActivity.this, "客服电话：400-0888-888", Toast.LENGTH_SHORT).show();
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


}

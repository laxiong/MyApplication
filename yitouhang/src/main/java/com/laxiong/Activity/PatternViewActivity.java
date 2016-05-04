package com.laxiong.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Mvp_presenter.UserCount_Presenter;
import com.laxiong.Mvp_view.IViewCount;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.UserLogin;
import com.laxiong.fund.widget.GestureContentView;
import com.laxiong.fund.widget.GestureDrawline.GestureCallBack;
import com.laxiong.yitouhang.R;

public class PatternViewActivity extends BaseActivity implements OnClickListener, IViewCount {
    /***
     * 设置手势密码
     */

    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * 手机号码
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    /**
     * 意图
     */

    private RelativeLayout mTopLayout;
    private TextView mTextTitle;
    private TextView mTextCancel;
    private ImageView mImgUserLogo;
    private TextView mTextPhoneNumber;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextForget;
    private TextView mTextOther;
    private String mParamPhoneNumber;
    private long mExitTime = 0;
    private int mParamIntentCode;
    private String gestruepswd;  // 手势密码
    private UserCount_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patternview);
        initView();
        initData();
        setUpListeners();
    }

    @Override
    public void getCountMsgSuc() {
        startActivity(new Intent(PatternViewActivity.this,
                MainActivity.class));
        PatternViewActivity.this.finish();
    }

    @Override
    public void getCountMsgFai() {
        Intent intent = new Intent(this, ChangeCountActivity.class);
        startActivity(intent);
        PatternViewActivity.this.finish();
    }

    private void initData() {
        presenter = new UserCount_Presenter(this);
        // 初始化一个显示各个点的viewGroup

        // TODO 获取登录的手势密码,在启动页里  第一次打开app进入ModifyGestrueActivity这个类，设置初始手势密码   之后的在这里校验
        gestruepswd = getSharedPreferences(Common.sharedPrefName, Context.MODE_PRIVATE).getString("patternstring", "");


        mGestureContentView = new GestureContentView(this, true, "12369",
                new GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {


                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
                        Toast.makeText(PatternViewActivity.this, "密码正确", Toast.LENGTH_LONG).show();
                        presenter.reqUserCountMsg(PatternViewActivity.this);
                    }

                    @Override
                    public void checkedFail() {
                        mGestureContentView.clearDrawlineState(1300L);
                        mTextTip.setVisibility(View.VISIBLE);
                        mTextTip.setText(Html
                                .fromHtml("<font color='#c70c1e'>密码错误</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(PatternViewActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                    }
                });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
        initUserLogin();
    }

    private void initUserLogin() {
        SharedPreferences sp = SpUtils.getSp(this);
        String userlogin = SpUtils.getStrValue(sp, SpUtils.USERLOGIN_KEY);
        if (!StringUtils.isBlank(userlogin)) {
            UserLogin user = JSONUtils.parseObject(userlogin, UserLogin.class);
            YiTouApplication.getInstance().setUserLogin(user);
        }
        String phonenum = SpUtils.getStrValue(sp, SpUtils.USER_KEY);
        String pwd = SpUtils.getStrValue(sp,
                SpUtils.GESTURE_KEY);
        if (StringUtils.isBlank(pwd) || StringUtils.isBlank(phonenum)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return;
        } else {
            if (!StringUtils.isBlank(phonenum))
                mTextPhoneNumber.setText(StringUtils.getProtectedMobile(phonenum));
        }
    }

    private void initView() {
        mParamPhoneNumber = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
        mParamIntentCode = getIntent().getIntExtra(PARAM_INTENT_CODE, 0);

        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
        mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
        mTextOther = (TextView) findViewById(R.id.text_other_account);

    }

    private void setUpListeners() {

        mTextForget.setOnClickListener(this);
        mTextOther.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_forget_gesture:

                startActivity(new Intent(PatternViewActivity.this,
                        ModifyGestureActivity.class));

                break;
            case R.id.text_other_account:

                startActivity(new Intent(PatternViewActivity.this,
                        ChangeCountActivity.class));

                break;
            default:
                break;
        }
    }

}

package com.laxiong.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carfriend.mistCF.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Mvp_presenter.Exit_Presenter;
import com.laxiong.Mvp_view.IViewExit;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.View.PayPop;
import com.laxiong.entity.UserLogin;
import com.laxiong.fund.widget.GestureContentView;
import com.laxiong.fund.widget.GestureDrawline.GestureCallBack;
public class PatternViewActivity extends BaseActivity implements OnClickListener,IViewExit {
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
    private PayPop dialog;
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
    private int times;
    private static final int TIMES_ERROR = 5;
    private Exit_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patternview);
        initView();
        initData();
        setUpListeners();
    }

    @Override
    public void logoutfailed(String msg) {

    }

    @Override
    public void logoutsuccess() {
        YiTouApplication.getInstance().setUserLogin(null);
        YiTouApplication.getInstance().setUser(null);
        SharedPreferences sp = SpUtils.getSp(this);
        SpUtils.saveStrValue(sp, SpUtils.USERLOGIN_KEY, "");
        SpUtils.saveStrValue(sp, SpUtils.USER_KEY, "");
        SpUtils.saveStrValue(sp, SpUtils.GESTURE_KEY, "");
        Toast.makeText(this, "请重新登录", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ChangeCountActivity.class);
        startActivity(intent);
        finish();
    }

    private void initData() {
        // 初始化一个显示各个点的viewGroup
//        gestruepswd = getSharedPreferences(Common.sharedPrefName, Context.MODE_PRIVATE).getString("patternstring", "");
        presenter = new Exit_Presenter(this);
        gestruepswd = SpUtils.getSp(this).getString(SpUtils.GESTURE_KEY, "");
        mGestureContentView = new GestureContentView(this, true, gestruepswd,
                new GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {


                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
                        Toast.makeText(PatternViewActivity.this, "密码正确", Toast.LENGTH_LONG).show();
                        CommonReq.recordLogin(PatternViewActivity.this);
                        startActivity(new Intent(PatternViewActivity.this,
                                MainActivity.class));
                        PatternViewActivity.this.finish();
                    }

                    @Override
                    public void checkedFail() {
                        times++;
                        if (times >= TIMES_ERROR) {
                            presenter.exit(PatternViewActivity.this);
                            return;
                        }
                        mGestureContentView.clearDrawlineState(1300L);
                        mTextTip.setVisibility(View.VISIBLE);
                        mTextTip.setText(Html
                                .fromHtml("<font color='#c70c1e'>密码错误,还有" + (TIMES_ERROR - times) + "次机会</font>"));
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
                startActivity(new Intent(PatternViewActivity.this,ChangeCountActivity.class));
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

package com.laxiong.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Basic.OnSingleClickListener;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.LoadUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;
import com.carfriend.mistCF.R;

import org.apache.http.Header;
import org.json.JSONObject;

public class RegistActivity extends BaseActivity implements OnClickListener {
    /***
     * 注册
     */
    private TextView mLoginBtn, mRegistBtn, mGetCode;
    private FrameLayout mBack;
    private ImageView mToggleBtn, mShowPswd;
    private EditText mPhoneEd, mPswdEd, mCodeEd;
    private Dialog mLoadDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_layout);
        initView();
        initData();
    }

    private void initData() {

        mLoginBtn.setOnClickListener(this);
        mRegistBtn.setOnClickListener(new OnSingleClickListener(this) {
            @Override
            public void onSingleClick(View v) {
                String mobile0 = mPhoneEd.getText().toString().replaceAll(" ", "");
                String pswd = mPswdEd.getText().toString().replace(" ", "");
                String code = mCodeEd.getText().toString().replace(" ", "");
                if (Common.inputContentNotNull(mobile0) && Common.inputContentNotNull(pswd) && Common.inputContentNotNull(code)) {
                    if (ValifyUtil.valifyPhoneNum(mobile0)) {
                        if (Common.inputPswdCount(pswd)) {
                            if (isRead) {
                                doRegist();
                            } else {
                                Toast.makeText(RegistActivity.this, "请选中壹投行注册协议", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ToastUtil.customAlert(RegistActivity.this, "密码为6~20位数字与字母混合,不包含非法字符和空格");
                        }
                    } else {
                        Toast.makeText(RegistActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistActivity.this, "手机号码或密码,验证码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBack.setOnClickListener(this);
        mToggleBtn.setOnClickListener(this);

        mPhoneEd.addTextChangedListener(watcher);
        mPswdEd.addTextChangedListener(watcher);
        mCodeEd.addTextChangedListener(watcher);

        mGetCode.setOnClickListener(this);
        mShowPswd.setOnClickListener(this);
        readProcotol();
    }

    private void initView() {

        mLoginBtn = (TextView) findViewById(R.id.loginBtn);
        mRegistBtn = (TextView) findViewById(R.id.registBtn);
        mBack = (FrameLayout) findViewById(R.id.backlayout);
        mToggleBtn = (ImageView) findViewById(R.id.toggle_img);
        mShowPswd = (ImageView) findViewById(R.id.img_showpswd);

        mCodeEd = (EditText) findViewById(R.id.regist_code);
//		mInviteCodeEd = (EditText)findViewById(R.id.regist_invite_code); // 邀请码
        mPhoneEd = (EditText) findViewById(R.id.regist_phone);
        mPswdEd = (EditText) findViewById(R.id.regist_pswd);
        mGetCode = (TextView) findViewById(R.id.getcode);
        mPswdEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    ValifyUtil.toastResult(RegistActivity.this, mPswdEd.getText().toString().trim());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                startActivity(new Intent(RegistActivity.this,
                        LoginActivity.class));
                break;
            case R.id.backlayout:
                this.finish();
                break;
            case R.id.toggle_img:
                readProcotol();
                break;
            case R.id.getcode:   // 验证码
                String mobile = mPhoneEd.getText().toString().replaceAll(" ", "");
                if (Common.inputContentNotNull(mobile)) {
                    if (ValifyUtil.valifyPhoneNum(mobile)) {
                        getCode();
                    } else {
                        Toast.makeText(RegistActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.img_showpswd:
                showPassWord();
                break;
        }
    }

    // 阅读协议,判断协议按钮是否为选中
    private boolean isRead = false;

    private void readProcotol() {
        if (!isRead) { // 是阅读的
            mToggleBtn.setImageResource(R.drawable.img_read);
            isRead = true;
        } else {  // 没有阅读
            mToggleBtn.setImageResource(R.drawable.img_no_read);
            isRead = false;
        }
        valify();
    }

    // show password
    private boolean isShowed = false;

    private void showPassWord() {
        if (isShowed) { // 隐藏
            mShowPswd.setImageResource(R.drawable.img_eye_close);
            mPswdEd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isShowed = false;
        } else {        //显示
            mShowPswd.setImageResource(R.drawable.img_eye_open);
            mPswdEd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isShowed = true;
        }
        valify();
    }

    // 获取验证码  code
    int count;
    boolean stopThread;

    private void getCode() {
        RequestParams params = new RequestParams();
        params.put("type", "reg");
        params.put("phone", mPhoneEd.getText().toString().replaceAll(" ", ""));

        HttpUtil.post(InterfaceInfo.CODE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            mGetCode.setClickable(false);
                            Toast.makeText(RegistActivity.this, "成功获取验证码", Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.getString("msg") != null) {
                                Toast.makeText(RegistActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                                stopThread = true;
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                stopThread = true;
                Toast.makeText(RegistActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
            }

        }, null);

        // 倒计时
        count = 59;
        stopThread = false;
        // timer
        new Thread(new Runnable() {
            public void run() {
                while (!stopThread && count > 0) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mGetCode.setText(count + "秒");
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    count--;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        mGetCode.setText("获取验证码");
                        mGetCode.setClickable(true);
                    }
                });
            }
        }).start();
    }
    //注册  Regist
    private void doRegist() {
        mLoadDialog = LoadUtils.createbuildDialog(this,"正在注册...");
        if (mLoadDialog!=null){
            mLoadDialog.show();
        }

        RequestParams params = new RequestParams();
        params.put("phone", mPhoneEd.getText().toString().replaceAll(" ", ""));
        params.put("pwd", mPswdEd.getText().toString().replace(" ", ""));
        params.put("code", Integer.valueOf(mCodeEd.getText().toString().replace(" ", "")));
//		String InviteCode = isInviteCode();
//		params.put("invite_id", InviteCode);

        HttpUtil.post(InterfaceInfo.USER_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            if(mLoadDialog!=null&&mLoadDialog.isShowing()){
                                mLoadDialog.dismiss();
                                mLoadDialog = null ;
                            }
                            ToastUtil.customAlert(RegistActivity.this,"注册成功");
                            savaUseInfo();
                            startActivity(new Intent(RegistActivity.this, ChangeCountActivity.class));
                            RegistActivity.this.finish();
                        } else {
                            if (response.getString("msg") != null) {
                                if(mLoadDialog!=null&&mLoadDialog.isShowing()){
                                    mLoadDialog.dismiss();
                                    mLoadDialog = null ;
                                }
                                ToastUtil.customAlert(RegistActivity.this, response.getString("msg"));
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                if(mLoadDialog!=null&&mLoadDialog.isShowing()){
                    mLoadDialog.dismiss();
                    mLoadDialog = null ;
                }
                ToastUtil.customAlert(RegistActivity.this, "注册失败");
            }

        }, true);

    }
    // 邀请码的处理
//	private String isInviteCode(){
//		if(mInviteCodeEd!=null){
//			String InviteCode = mInviteCodeEd.getText().toString().replace(" ", "");
//			if(Common.inputContentNotNull(InviteCode)){
//				return InviteCode;
//			}
//			return null ;
//		}else{
//			return null ;
//		}
//	}
    // EditText 是否输入了

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            valify();
        }

    };

    private void valify() {
        if (!TextUtils.isEmpty(mPhoneEd.getText().toString()) && !TextUtils.isEmpty(mPswdEd.getText().toString())
                && !TextUtils.isEmpty(mCodeEd.getText().toString()) && isRead) {
            if (Common.inputPswdCount(mPswdEd.getText().toString().trim())) {
                mRegistBtn.setEnabled(true);
                mRegistBtn.setBackgroundResource(R.drawable.button_change_bg_border);
            } else {
                mRegistBtn.setEnabled(false);
                mRegistBtn.setBackgroundResource(R.drawable.button_grey_corner_border);
            }
        } else {
            mRegistBtn.setEnabled(false);
            mRegistBtn.setBackgroundResource(R.drawable.button_grey_corner_border);
        }
    }

    //保存注册用户信息
    private void savaUseInfo() {
        SharedPreferences prefer = getSharedPreferences("RegistUseInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefer.edit();
        editor.putString("useCount", mPhoneEd.getText().toString().replaceAll(" ", ""));
        editor.commit();
    }


}

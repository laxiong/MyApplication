package com.laxiong.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongshidai.mistGSD.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.Utils.ValifyUtil;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;
import org.apache.http.Header;
import org.json.JSONObject;

public class TrueNameActivity1 extends BaseActivity implements OnClickListener {
    /***
     * 实名认证第一步
     */
    private TextView mNextPage, getCode, mPhone;
    private FrameLayout mBack;
    private ImageView toggleRead;
    private EditText mIDcard, mName, mCode;//身份证 姓名  手机号  验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_name1);
        initView();
        initData();
    }

    private void initData() {
        mNextPage.setOnClickListener(this);
        mBack.setOnClickListener(this);
        toggleRead.setOnClickListener(this);
        getCode.setOnClickListener(this);

        mIDcard.addTextChangedListener(watcher);
        mName.addTextChangedListener(watcher);
        mCode.addTextChangedListener(watcher);
        readProtocol();

//		SharedPreferences prefer = getSharedPreferences("RegistUseInfo",MODE_PRIVATE);
//		String phone = prefer.getString("useCount", "438");
        SharedPreferences sp = SpUtils.getSp(this);
        String phone = sp.getString(SpUtils.USER_KEY, "");
        if (phone != null) {
            mPhone.setText(phone);
        } else {
            startActivityForResult(new Intent(this, LoginActivity.class).putExtra("isBack", true), 0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mNextPage = (TextView) findViewById(R.id.nextpager);
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        toggleRead = (ImageView) findViewById(R.id.toggle);
        TextView mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("实名认证");

        mCode = (EditText) findViewById(R.id.trueCode);
        mName = (EditText) findViewById(R.id.trueName);
        mIDcard = (EditText) findViewById(R.id.trueIdcard);
        mPhone = (TextView) findViewById(R.id.truePhone);
        getCode = (TextView) findViewById(R.id.getcode);


    }

    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.nextpager:
                String code = mCode.getText().toString().trim();
                String name = mName.getText().toString().trim();
                String idcard = mIDcard.getText().toString().trim();

                if (Common.inputContentNotNull(code) && Common.inputContentNotNull(name) && Common.inputContentNotNull(idcard)
                        ) {
                    if (isRead) {
                        if (!ValifyUtil.valifyName(TrueNameActivity1.this,name)) {
//                            ToastUtil.customAlert(TrueNameActivity1.this, "姓名不能包含非法字符");
                        } else if (!ValifyUtil.valifyIdenti(idcard)) {
                            ToastUtil.customAlert(TrueNameActivity1.this, "身份证号不合法");
                        } else {
                            getNet();
                        }
                    } else {
                        Toast.makeText(TrueNameActivity1.this, "请选中以下协议", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TrueNameActivity1.this, "输入内容不能有空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back_layout:
                this.finish();
                break;

            case R.id.toggle:
                readProtocol();
                break;
            case R.id.getcode:
                getCode();
        }
    }

    /***
     * 阅读协议
     */
    boolean isRead = false;

    private void readProtocol() {
        if (!isRead) {
            toggleRead.setImageResource(R.drawable.img_read);
            isRead = true;
        } else {
            toggleRead.setImageResource(R.drawable.img_no_read);
            isRead = false;
        }
        valify();
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            valify();
        }
    };

    private void valify() {
        if (!TextUtils.isEmpty(mName.getText().toString())&&ValifyUtil.valifyName(this, mName.getText().toString()) && !TextUtils.isEmpty(mIDcard.getText().toString())
                &&ValifyUtil.valifyIdenti(mIDcard.getText().toString())&&!TextUtils.isEmpty(mCode.getText().toString()) && isRead) {
            mNextPage.setEnabled(true);
            mNextPage.setBackgroundResource(R.drawable.button_change_bg_border);
        } else {
            mNextPage.setEnabled(false);
            mNextPage.setBackgroundResource(R.drawable.button_grey_corner_border);
        }
    }

    int count;
    boolean stopThread;

    // 获取验证码
    private void getCode() {
        RequestParams params = new RequestParams();
        params.put("type", "real");
        HttpUtil.post(InterfaceInfo.CODE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        Log.i("URL", "code码：=" + response.getInt("code"));
                        if (response.getInt("code") == 0) {
                            Toast.makeText(TrueNameActivity1.this, "成功获取验证码", Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.getString("msg") != null) {
                                Toast.makeText(TrueNameActivity1.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TrueNameActivity1.this, "发送失败", Toast.LENGTH_SHORT).show();
            }

        }, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
                YiTouApplication.getInstance().getUserLogin().getToken()));// 授权

        // 倒计时
        count = 59;
        stopThread = false;
        // timer
        new Thread(new Runnable() {
            public void run() {
                while (!stopThread && count > 0) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            getCode.setText(count + "秒");
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
                        getCode.setText("获取验证码");
                        getCode.setClickable(true);
                    }
                });
            }
        }).start();
    }

    // 点击下一步的反回数据到后台
    private void getNet() {
        RequestParams params = new RequestParams();
        params.put("type", "reals");
        params.put("realname", mName.getText().toString().trim());
        params.put("idc", mIDcard.getText().toString().trim());
        params.put("code", mCode.getText().toString().trim());

        HttpUtil.put(InterfaceInfo.USER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            Toast.makeText(TrueNameActivity1.this, "认证第一步成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(TrueNameActivity1.this,
                                    TrueNameActivity2.class));
                            CommonReq.reqUserMsg(TrueNameActivity1.this);
                            YiTouApplication.getInstance().getUser().setIs_idc(true);
                            TrueNameActivity1.this.finish();
                        } else if (response.getInt("code") == 401) {
                            CommonReq.showReLoginDialog(TrueNameActivity1.this);
                        } else
                            Toast.makeText(TrueNameActivity1.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(TrueNameActivity1.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        }, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(),
                YiTouApplication.getInstance().getUserLogin().getToken()));// 授权
    }


}

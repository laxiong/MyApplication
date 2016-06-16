package com.laxiong.Activity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Utils.DialogUtils;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.PasswordInputView;
import com.laxiong.View.PayPop;
import com.gongshidai.mistGSD.R;
import com.laxiong.entity.User;

import java.util.jar.Attributes;

public class PswdConturalActivity extends BaseActivity implements OnClickListener {
    /***
     * 密码管理
     */
    private RelativeLayout mChangeLoginPswd, mChangePayPswd, mResetPayPswd, mChangeGesturePswd;
    private FrameLayout mBack;
    private PayPop dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pswd_contural);
        initView();
        initData();
    }

    private void initView() {
        mChangeLoginPswd = (RelativeLayout) findViewById(R.id.changeloginpswd);
        mChangePayPswd = (RelativeLayout) findViewById(R.id.changepaypswd);
        mResetPayPswd = (RelativeLayout) findViewById(R.id.resetpaypswd);
        mChangeGesturePswd = (RelativeLayout) findViewById(R.id.changegesturepswd);
        mBack = (FrameLayout) findViewById(R.id.back_layout);

        TextView mText = (TextView) findViewById(R.id.title);
        mText.setText("密码管理");
    }

    private void initData() {
        mChangeLoginPswd.setOnClickListener(this);
        mChangePayPswd.setOnClickListener(this);
        mResetPayPswd.setOnClickListener(this);
        mChangeGesturePswd.setOnClickListener(this);
        mBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.changeloginpswd:    /**登录密码**/
                startActivity(new Intent(PswdConturalActivity.this,
                        ChangeLoginPswdActivity.class));
                break;
            case R.id.changepaypswd:   /**修改支付密码**/
                showChangePayPswd(V);
                break;
            case R.id.resetpaypswd:  /**重置支付密码**/
                startActivity(new Intent(PswdConturalActivity.this,
                        ResetPayPwdActivity.class));
                break;
            case R.id.changegesturepswd: /**修改手势密码**/

                Intent intent = new Intent(PswdConturalActivity.this,
                        ModifyGestureActivity.class);
                intent.putExtra("setting",true);
                startActivityForResult(intent, 1001);

                break;

            case R.id.back_layout:
                this.finish();
                break;
        }
    }

    private void showChangePayPswd(View parent) {
        View v = LayoutInflater.from(this).inflate(R.layout.layout_commoninputdialog, null);
        final EditText et_pass = (EditText) v.findViewById(R.id.et_pass);
        DialogUtils.bgalpha(PswdConturalActivity.this, 0.3f);
        dialog = new PayPop(v, this, "请输入壹投行支付密码", "用于修改支付密码", new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.bgalpha(PswdConturalActivity.this, 1.0f);
                //TODO  输完密码后跳转的页面,携带参数
                Intent intent = new Intent(PswdConturalActivity.this,
                        ChangePayPwdActivity.class);
                intent.putExtra("pwd", et_pass.getText().toString());
                startActivity(intent);
                PswdConturalActivity.this.finish();
                dialog.dismiss();
            }
        });
        DisplayMetrics metrix=new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrix);
        int y=(int)(1.0f*metrix.heightPixels/5);
        dialog.showAtLocation(parent, Gravity.TOP, 0,y);
    }

    // 成功修改手势密码 后
    private PopupWindow mSuccessModifyWinds;
    private View mSuccessView;

    private void successModifyGesture() {

        mSuccessView = LayoutInflater.from(PswdConturalActivity.this).inflate(R.layout.success_modifygestrue_popwindow, null);

        TextView mAuthentication = (TextView) mSuccessView.findViewById(R.id.setid);
        TextView mIknow = (TextView) mSuccessView.findViewById(R.id.iknow);

        mAuthentication.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                User user = YiTouApplication.getInstance().getUser();
                String phone = SpUtils.getStrValue(SpUtils.getSp(PswdConturalActivity.this), SpUtils.USER_KEY);
                Intent intent = null;
                if (user == null && !StringUtils.isBlank(phone)) {
                    intent = new Intent(PswdConturalActivity.this, LoginActivity.class);
                    startActivity(intent);
                    PswdConturalActivity.this.finish();
                    return;
                } else if (user == null && StringUtils.isBlank(phone)) {
                    intent = new Intent(PswdConturalActivity.this, ChangeCountActivity.class);
                    startActivity(intent);
                    PswdConturalActivity.this.finish();
                    return;
                }
                if (!user.is_idc()) {
                    startActivity(new Intent(PswdConturalActivity.this,
                            TrueNameActivity1.class));
                } else if (!user.isPay_pwd()) {
                    startActivity(new Intent(PswdConturalActivity.this, TrueNameActivity2.class));
                } else if (user.getBankcount() == 0) {
                    startActivity(new Intent(PswdConturalActivity.this, TrueNameActivity3.class));
                } else {
                    ToastUtil.customAlert(PswdConturalActivity.this, "已认证!");
                }
                if (mSuccessModifyWinds != null && mSuccessModifyWinds.isShowing()) {
                    mSuccessModifyWinds.dismiss();
                    mSuccessModifyWinds = null;
                }
            }
        });

        mIknow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSuccessModifyWinds != null && mSuccessModifyWinds.isShowing()) {
                    mSuccessModifyWinds.dismiss();
                    mSuccessModifyWinds = null;
                }
            }
        });

        mSuccessModifyWinds = new PopupWindow(mSuccessView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        mSuccessModifyWinds.setTouchable(true);
        mSuccessModifyWinds.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mSuccessModifyWinds.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mSuccessModifyWinds.showAtLocation(mSuccessView, Gravity.BOTTOM, 0, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (resultCode == 1001) {
            successModifyGesture();
        }
    }


}

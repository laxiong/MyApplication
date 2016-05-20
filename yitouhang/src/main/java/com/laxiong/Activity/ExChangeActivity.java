package com.laxiong.Activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.BackListener;
import com.laxiong.Mvp_presenter.Exchange_Presenter;
import com.laxiong.Mvp_view.IViewExchange;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.DialogUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;
import com.laxiong.View.PayPop;
import com.laxiong.entity.User;
import com.gongshidai.mistGSD.R;

/**
 * @params tv_ecnum - +之间的数字
 * @params tv_value 1元 兑换的红包
 * @params tv_total 所需要的壹币数量
 */
public class ExChangeActivity extends BaseActivity implements View.OnClickListener, IViewExchange {
    private static final String TAG = "ExChangeActivity";
    private int yinum = 0;//壹币的数量
    private int yuan = 1;//要兑换的人民币
    private int num = 1;//要兑换的个数
    private int type = 0;//要兑换的壹币的类型1元5元10元
    private int id = -1;
    private String url;
    private PayPop dialog;
    private TextView tv_exchange, tv_ecnum, tv_value, tv_total, tv_deduct, tv_plus, tv_jiesuannum;
    private CommonActionBar actionbar;
    private User user;
    private ImageView ivpic;
    private Exchange_Presenter presenter;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ex_change);
        init();
    }

    @Override
    public void exchangeSuc() {
        ToastUtil.customAlert(this, "兑换成功");
        user.setScore(user.getScore() - yuan * 100);
    }

    @Override
    public void exchangeFail(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    private void init() {
        ivpic = (ImageView) findViewById(R.id.ivpic);
        tv_ecnum = (TextView) findViewById(R.id.tv_ecnum);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_exchange = (TextView) findViewById(R.id.tv_exchange);
        tv_value = (TextView) findViewById(R.id.tv_value);
        actionbar = (CommonActionBar) findViewById(R.id.actionbar);
        tv_deduct = (TextView) findViewById(R.id.tv_deduct);
        tv_plus = (TextView) findViewById(R.id.tv_plus);
        tv_jiesuannum = (TextView) findViewById(R.id.tv_jiesuannum);
        yuan = Integer.parseInt(tv_ecnum.getText().toString());
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        id = intent.getIntExtra("id", -1);
        url = intent.getStringExtra("url");
        if (intent != null && type != 0) {
            yuan = type;
        }
        if (type == 0 || id == -1) {
            Intent back = new Intent(this, TMallActivity.class);
            startActivity(back);
        }
        initValue();
        initListener();
    }

    private void initValue() {
        presenter = new Exchange_Presenter(this);
        if (!StringUtils.isBlank(url))
            Glide.with(this).load(url).placeholder(R.drawable.gongshi_banner_mr).into(ivpic);
        user = YiTouApplication.getInstance().getUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            yinum = user.getScore();
        }
        boolean flag = yuan * 100 <= yinum;
        tv_value.setText(type + "元");
        tv_total.setText(yuan * 100 + "");
        tv_jiesuannum.setText(yuan * 100 + "");
        tv_exchange.setEnabled(flag ? true : false);
        tv_exchange.setBackgroundResource(flag ? R.drawable.shape_redbtn_border : R.drawable.shape_greybtn_border);
        tv_exchange.setText(flag ? R.string.exchange_btn_im : R.string.exchange_btn_noenum);
    }

    private void initListener() {
        actionbar.setBackListener(this);
        tv_exchange.setOnClickListener(this);
        tv_deduct.setOnClickListener(this);
        tv_plus.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean valifyPwd() {
        if (dialog != null) {
            String pwd = dialog.getExcTextPwd();
            return !StringUtils.isBlank(pwd);
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exchange:
                DialogUtils.bgalpha(ExChangeActivity.this, 0.3f);
                dialog = new PayPop(ExChangeActivity.this, yuan, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.bgalpha(ExChangeActivity.this, 1.0f);
                        if (valifyPwd()) {
                            presenter.exchange(ExChangeActivity.this, tv_ecnum.getText().toString()
                                    , id, dialog.getExcTextPwd());
                        }
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ExChangeActivity.this, ResetPayPwdActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
                dialog.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.tv_deduct:
                if (num > 1) {
                    yuan -= type;
                    --num;
                }
                tv_ecnum.setText(num + "");
                tv_deduct.setTextColor(getResources().getColor(num == 1 ? R.color.shen_grey : R.color.maintext_grey));
                initValue();
                break;
            case R.id.tv_plus:
                yuan += type;
                tv_ecnum.setText(++num + "");
                initValue();
                break;
        }
    }
}

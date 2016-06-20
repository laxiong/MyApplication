package com.laxiong.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongshidai.mistGSD.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_presenter.Share_Presenter;
import com.laxiong.Mvp_view.IViewBasicObj;
import com.laxiong.Utils.DialogUtils;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.OpenAccount;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.VerticalNumberProgressBar;
import com.laxiong.entity.ShareInfo;
import com.laxiong.entity.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.UMShareAPI;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;

public class GuXiBaoActivity extends BaseActivity implements OnClickListener, IViewBasicObj<ShareInfo> {
    /****
     * 固息宝
     */
    private Share_Presenter presenter;
    private LinearLayout ll_wrap;
    private RelativeLayout mLayout_progressbar ,mRemarkLayout ,mSafeProtect,rl_investrec;
    private View mRemarkLine ;
    private TextView mProgressNum ,mShareBtn , mBuyBtn ,mFinanceLimit,mMinTou ,mTextDes,mPrecentTg;
    private VerticalNumberProgressBar mProgressBar ;
    private FrameLayout mBack ;
    private ImageView mJiSuanQi ;
    private int mId;
    private int ttnum ;
    private int limitDay ;
    private double bird ;
    private View V_line ;
    // 百分比 等加载的内容
    private TextView mPrecent, mAddPrecent,
            mRemark1, mRemark2, mLastEran,
            mAddOther, mGxbTitle, mYdDec,
            mGetCashDec,mAprDec , mLastDate ,mFirstDate;

    private boolean isVip ; // 是从VIP理财点击过来的否
    private  User mUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_guxibao);
        mUser = YiTouApplication.getInstance().getUser();
        initView();
        initData();

        isVip = getIntent().getBooleanExtra("isVip",false);
            if (isVip){
                setVipTextColor();
            }else {
                setTextColor();
            }
        getNetWork();
    }

    private void initData() {
        mShareBtn.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mJiSuanQi.setOnClickListener(this);
        mBuyBtn.setOnClickListener(this);
        mSafeProtect.setOnClickListener(this);
        rl_investrec.setOnClickListener(this);
        mId = getIntent().getIntExtra("id", -1);
        ttnum = getIntent().getIntExtra("ttnum", -1);
        limitDay = getIntent().getIntExtra("limitday", -1);
        bird = getIntent().getDoubleExtra("bird",-1);
        if (bird == 1.0){
            if (mUser!=null&&!mUser.is_bird()){
                mBuyBtn.setText("仅限新用户购买");
            }else {
                mBuyBtn.setText("立即购买");
            }
        }else {
            mBuyBtn.setText("立即购买");
        }

        presenter = new Share_Presenter(this);
    }

    @Override
    public void loadObjSuc(ShareInfo obj) {
        if (obj == null) {
            ToastUtil.customAlert(this, "未获取到分享数据");
        } else {
            DialogUtils.getInstance(GuXiBaoActivity.this).alertShareDialog(obj, ll_wrap);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void loadObjFail(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getNetWork();
    }

    private void initView() {
        mLayout_progressbar = (RelativeLayout) findViewById(R.id.progressbar_layout);
        mProgressNum = (TextView) findViewById(R.id.numText);
        mProgressBar = (VerticalNumberProgressBar) findViewById(R.id.numberbar);
        mJiSuanQi = (ImageView) findViewById(R.id.jisuanqi);
        mBuyBtn = (TextView) findViewById(R.id.buying);
        mBack = (FrameLayout) findViewById(R.id.backlayout);
        mShareBtn = (TextView) findViewById(R.id.share);
        mTextDes =(TextView)findViewById(R.id.text2);
        mLastDate =(TextView)findViewById(R.id.last_date); //赎回时间
        mFirstDate = (TextView)findViewById(R.id.first_date); // 起息时间
        rl_investrec= (RelativeLayout) findViewById(R.id.rl_investrec);
        mRemarkLine =findViewById(R.id.remark_line);
        mRemarkLayout =(RelativeLayout)findViewById(R.id.remark_layout);

        mPrecent =(TextView)findViewById(R.id.tv2);  // 年化
        mPrecentTg =(TextView)findViewById(R.id.precent_tg);// %
        mAddPrecent =(TextView)findViewById(R.id.addprecent);
        mFinanceLimit =(TextView)findViewById(R.id.getcash);			//理财周期
        mMinTou =(TextView)findViewById(R.id.yesterdayprofit); //起投金额
        mRemark1 =(TextView)findViewById(R.id.remark1);
        mRemark2 =(TextView)findViewById(R.id.remark2);
        mLastEran =(TextView)findViewById(R.id.text1);
        mAddOther =(TextView)findViewById(R.id.add_profit);
        mGxbTitle =(TextView)findViewById(R.id.gxb_title);
        mSafeProtect =(RelativeLayout)findViewById(R.id.safeprotect); //安全保障
        ll_wrap = (LinearLayout) findViewById(R.id.ll_wrap);

        mYdDec = (TextView)findViewById(R.id.tv3);
        mGetCashDec =(TextView)findViewById(R.id.tv4);
        V_line= findViewById(R.id.v_line);
        mAprDec = (TextView)findViewById(R.id.tv1);

    }

    private String mProjectName ="固息宝";
    private String mAmountMoney ;
    private double mBuyPrecent ;  // 总年化收益率
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                presenter.loadShareData(this);
                break;
            case R.id.rl_investrec:
                startActivity(new Intent(GuXiBaoActivity.this,WebViewActivity.class).putExtra("title","投资记录").putExtra("url",
                        "https://licai.gongshidai.com/wap/public/cylc/touzi.html"));
                break;
            case R.id.backlayout:
                this.finish();
                break;
            case R.id.jisuanqi:
                showJiSuanQi();
                break;
            case R.id.buying:
                if (mUser!=null){
                    if (mUser.getBankcount() >=1){
                        if (bird != 1) {
                            startActivity(new Intent(GuXiBaoActivity.this,
                                    BuyingActivity.class).
                                    putExtra("projectStr", mProjectName).
                                    putExtra("amountStr", mAmountMoney).
                                    putExtra("id", mId).
                                    putExtra("mBuyPrecent", mBuyPrecent).
                                    putExtra("limitday", limitDay));
                        }
                    }else {
                        OpenAccount.getInstance().goToCreateCountNum(GuXiBaoActivity.this);
                    }
                }else {
                    startActivity(new Intent(GuXiBaoActivity.this,
                            LoginActivity.class));
                }
                break;
        }
    }


    // set progress textview height
    private void setProgressNumHeight(float f) {
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        mLayout_progressbar.measure(widthMeasureSpec, heightMeasureSpec);
        mProgressNum.measure(widthMeasureSpec, heightMeasureSpec);

        mProgressBar.setProgress(f);
        int layoutHeight = mLayout_progressbar.getMeasuredHeight();
        int progressTextHeight = mProgressNum.getMeasuredHeight();

        // 设置进度的运行高度大于  TextView本身默认的高度时
        if (((mProgressBar.getProgress() / 100) * layoutHeight) > progressTextHeight) {
            LayoutParams tvlp = mProgressNum.getLayoutParams();
            tvlp.height = (int) ((mProgressBar.getProgress() / 100) * layoutHeight);
            mProgressNum.setLayoutParams(tvlp);
        }
        mProgressNum.setText(mProgressBar.getProgress() + "%");
    }

    /**
     * 显示计算器的PopupWindow
     */
    private PopupWindow mPopWindJi;
    private View mJiSuanView;

    private void showJiSuanQi() {
        mJiSuanView = LayoutInflater.from(this).inflate(R.layout.jisuanqi_popwindow, null);
        final EditText mMoney = (EditText) mJiSuanView.findViewById(R.id.toumoney);
        final EditText mDays = (EditText) mJiSuanView.findViewById(R.id.touday);
        final TextView mComfix = (TextView) mJiSuanView.findViewById(R.id.comfit);
        ImageView imgConcel = (ImageView) mJiSuanView.findViewById(R.id.imgs_concel);
        // 投资金额
        mMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                String str = mMoney.getText().toString().trim();
                //TODO  TextView 的计算结果显示
                if(mDays!=null&&!mDays.getText().toString().trim().equals("")&&mDays.getText().toString().length()!=0){
                    if (str!=null&&!str.equals("")&&str.length()!=0) {
                        int tM = Integer.parseInt(str);
                        int tD = Integer.parseInt(mDays.getText().toString().trim());
                        // 保留小数点三位
                        NumberFormat mFormat = NumberFormat.getNumberInstance();
                        mFormat.setMaximumFractionDigits(3);
                        String comfixNum = mFormat.format(backComfix(tM, tD, mBuyPrecent));

                        mComfix.setText(comfixNum);
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                String str = mMoney.getText().toString().trim();
                try{
                    if(str!= null)
                        Integer.parseInt(str);
                }catch(Exception e){
                    Toast.makeText(GuXiBaoActivity.this, "输入整数", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 投资天数
        mDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String str = mDays.getText().toString().trim();
                try{
                    if(str!= null)
                        Integer.parseInt(str);
                }catch(Exception e){
                    Toast.makeText(GuXiBaoActivity.this, "输入整数", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                String str = mDays.getText().toString().trim();
                //TODO  TextView 的计算结果显示
                if(mMoney!=null&&!mMoney.getText().toString().trim().equals("")&&mMoney.getText().toString().length()!=0){
                    if (str!=null&&!str.equals("")&&str.length()!=0) {
                        int tD = Integer.parseInt(str);
                        int tM = Integer.parseInt(mMoney.getText().toString().trim());
                        // 保留小数点三位
                        NumberFormat mFormat = NumberFormat.getNumberInstance();
                        mFormat.setMaximumFractionDigits(3);
                        String comfixNum = mFormat.format(backComfix(tM, tD, mBuyPrecent));

                        mComfix.setText(comfixNum);
                    }
                }
            }
        });
        // 取消按钮
        imgConcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(mPopWindJi!=null&&mPopWindJi.isShowing()){
                    mPopWindJi.dismiss();
                    mPopWindJi = null ;
                }
            }
        });
        mPopWindJi = new PopupWindow(mJiSuanView,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
        mPopWindJi.setTouchable(true);
        mPopWindJi.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mPopWindJi.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        mPopWindJi.showAtLocation(mJiSuanView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 计算器的算法
     * money:本金
     * day：日期
     * lu：利率  7.2%
     */
    private double backComfix(float money,float day, double lu){
        double backMoney = money*(lu/100)*(day/365)+money;
        return backMoney ;
    }

    // 设置数据
    private void getNetWork(){
        RequestParams params = new RequestParams();
        params.put("p",1);
        if (ttnum!=-1)
            params.put("limit",ttnum);
        if (mId!=-1)
            params.put("id",mId);
        HttpUtil.get(InterfaceInfo.PRODUCT_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response!=null){
                    try {
                        if (response.getInt("code")==0){
                            Log.i("WK","所有的对象："+response);

                            mProjectName = response.getString("title");
                            mGxbTitle.setText(mProjectName);
                            updataUi(response);
                            double percent = response.getDouble("percent");
                            setProgressNumHeight((float)percent);
                        }else {
                            Toast.makeText(GuXiBaoActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception E){
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(GuXiBaoActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updataUi(JSONObject response){
        if (response!=null){
            try{
                double num = response.getDouble("apr");
                if (isInterge(num)){
                    String aprStr = String.valueOf(num);
                    String[] arr = aprStr.split("[.]");
                    String zhengshu = arr[0];
                    mPrecent.setText(zhengshu);
                }else {
                    mPrecent.setText(String.valueOf(num));
                }

                if (isVip){
                    mAddPrecent.setText("+"+String.valueOf(response.getDouble("accum"))+"%");
                    mAddOther.setText(String.valueOf(response.getDouble("accum"))+"%");
                    // vip
                    mTextDes.setText("VIP用户，平台奖励");

                    mBuyPrecent = num+response.getDouble("accum");  //VIP的年化总收益利率

                }else if (response.getInt("bird")==1){
                    mAddPrecent.setText("+"+String.valueOf(response.getDouble("birdapr"))+"%");
                    mAddOther.setText(String.valueOf(response.getDouble("birdapr"))+"%");
                    //新手标
                    mTextDes.setText("新客专享，平台奖励");

                    mBuyPrecent = num+response.getDouble("birdapr");  //新手标的年化总收益利率

                }else if (response.getDouble("present")!=0.0){
                    mAddPrecent.setText("+"+String.valueOf(response.getDouble("present"))+"%");
                    mAddOther.setText(String.valueOf(response.getDouble("present"))+"%");
                    //平台赠送
                    mTextDes.setText("，平台奖励");

                    mBuyPrecent = num+response.getDouble("present");  //平台奖励的年化总收益利率
                }

                mAmountMoney = String.valueOf(response.getInt("amount"));
                mLastEran.setText(mAmountMoney);

                mLastDate.setText(response.getString("date"));
                //保存日期的时间
                SharedPreferences gxb_date = getSharedPreferences("GXB_DATE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = gxb_date.edit();
                editor.putString("gxb_date",response.getString("date"));
                editor.commit();

                mFirstDate.setText(response.getString("rule"));

                mMinTou.setText(String.valueOf(response.getInt("min")));
                mFinanceLimit.setText(String.valueOf(response.getInt("limit")));

                Log.i("WK", "====1========：" + response.getString("title"));

                JSONArray details = response.getJSONArray("details");
                if (details.length()>0){
                    mRemarkLine.setVisibility(View.VISIBLE);
                    mRemarkLayout.setVisibility(View.VISIBLE);
                    mRemark1.setText(details.getString(0));
                    mRemark2.setText(details.getString(1));
                }else {
                    mRemarkLine.setVisibility(View.GONE);
                    mRemarkLayout.setVisibility(View.GONE);
                }
                mProjectName = response.getString("title");
                mGxbTitle.setText(mProjectName);

            }catch (Exception E){
            }
        }
    }
    //判断是否为整数
    private boolean isInterge(double num){
        if(num%1==0){
            return  true;
        }else{
            return false;
        }
    }
    //mPrecentTg mPrecent  mAddPrecent mMinTou mFinanceLimit mYdDec mGetCashDec V_line mAprDec
    // vip用户的金色字体
    private void setVipTextColor(){
        mPrecent.setTextColor(Color.parseColor("#FFFFDFAA"));
        mPrecentTg.setTextColor(Color.parseColor("#FFFFDFAA"));
        mAddPrecent.setTextColor(Color.parseColor("#FFFFDFAA"));
        mMinTou.setTextColor(Color.parseColor("#FFFFDFAA"));
        mFinanceLimit.setTextColor(Color.parseColor("#FFFFDFAA"));
        mYdDec.setTextColor(Color.parseColor("#FFFFDFAA"));
        mGetCashDec.setTextColor(Color.parseColor("#FFFFDFAA"));
        mAprDec.setTextColor(Color.parseColor("#FFFFDFAA"));
        V_line.setBackgroundColor(Color.parseColor("#FFFFDFAA"));
//        mShareBtn.setTextColor(Color.parseColor("#FFE2A42A"));
//        mGxbTitle.setTextColor(Color.parseColor("#FFE2A42A"));
    }

    // 普通用户的普通字体
    private void  setTextColor(){
        mPrecent.setTextColor(Color.parseColor("#ffffff"));
        mPrecentTg.setTextColor(Color.parseColor("#ffffff"));
        mAddPrecent.setTextColor(Color.parseColor("#ffffff"));
        mMinTou.setTextColor(Color.parseColor("#ffffff"));
        mFinanceLimit.setTextColor(Color.parseColor("#ffffff"));
        mYdDec.setTextColor(Color.parseColor("#ffffff"));
        mGetCashDec.setTextColor(Color.parseColor("#ffffff"));
        mAprDec.setTextColor(Color.parseColor("#ffffff"));
        V_line.setBackgroundColor(Color.parseColor("#ffffff"));
//        mShareBtn.setTextColor(Color.parseColor("#ffffff"));
//        mGxbTitle.setTextColor(Color.parseColor("#ffffff"));
    }

}

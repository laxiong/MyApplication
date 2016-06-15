package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gongshidai.mistGSD.R;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Mvp_presenter.Share_Presenter;
import com.laxiong.Mvp_view.IViewBasicObj;
import com.laxiong.Utils.DialogUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;
import com.laxiong.entity.ShareInfo;
import com.laxiong.entity.User;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

public class WebViewActivity extends BaseActivity implements IViewBasicObj<ShareInfo> {
    /****
     * html 页面  协议 活动 等
     */
    private WebView mWebView;
    private String urls;
    private String titleStr;
    private boolean needshare;
    private CommonActionBar actionBar;
    private Share_Presenter presenter;
    private String titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initData();
        setlisten();
    }

    //用于h5接受风险评估的结果
    public void onSuccess(String str) {
        User user = YiTouApplication.getInstance().getUser();
        if (user != null) {
            user.setAssess(str);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void loadObjSuc(ShareInfo obj) {
        if (obj == null) {
            ToastUtil.customAlert(this, "未获取到分享数据");
        } else {
            DialogUtils.getInstance(WebViewActivity.this).alertShareDialog(obj, mWebView);
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

    @SuppressLint({"NewApi", "SetJavaScriptEnabled"})
    private void initData() {
        presenter = new Share_Presenter(this);
        mWebView = (WebView) findViewById(R.id.webviews);

        actionBar = (CommonActionBar) findViewById(R.id.actionbar);
        Intent intent = getIntent();
        urls = intent.getStringExtra("url");
        titleStr = intent.getStringExtra("title");
        needshare = intent.getBooleanExtra("needshare", false);
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                actionBar.setTitle(title);
                titles=title;
            }

        };
        mWebView.setWebChromeClient(wvcc);
        if (!TextUtils.isEmpty(titleStr))
            actionBar.setTitle(titleStr);
//		mWebView.loadUrl(url); //WebView加载web资源
//		//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//		mWebView.setWebViewClient(new WebViewClient(){
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//				view.loadUrl(url);
//				return true;
//			}
//		});

        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        /*
         * 4.0-4.2 防范攻击者植入js
		 */
        mWebView.removeJavascriptInterface("searchBoxJavaBredge_");
        mWebView.setWebChromeClient(new WebChromeClient() {
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.startsWith("gongshi2://")) {
//                    WebViewActivity.this.finish();
//                }
                String str = "gongshi1://";
                if (url.startsWith(str)) {
                    String host = url.substring(url.length() - 1, url.length());
                    int index = Integer.valueOf(host);
                    jumpActivity(index);
                }
                mWebView.loadUrl(url);
                return true;
            }
        });
//        int x=urls.lastIndexOf("?");
//        String eventid=urls.substring(0,x==-1?urls.length()-1:x);
        MobclickAgent.onEvent(this,"h5_"+(TextUtils.isEmpty(titleStr)?titles:titleStr));
        mWebView.loadUrl(urls);
    }

    private void setlisten() {
        actionBar.setBackListener(this);
        if (!needshare) return;
        actionBar.addClickTextRight(this, "分享", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                ShareInfo banner = (ShareInfo) bundle.getSerializable("banner");
                if (banner != null) {
                    loadObjSuc(banner);
                } else {
                    presenter.loadShareData(WebViewActivity.this);
                }
            }
        });
    }

    //  根据Host不同跳转的页面不同
    private void jumpActivity(int index) {
        switch (index) {
            case 0:         //关闭当前页面
                this.finish();

                break;
            case 1:          //首页
                startActivity(new Intent(this,
                        MainActivity.class));// 启动到SingTask模式
                this.finish();

                break;
            case 2:          // 理财产品页
                Toast.makeText(this, "理财产品页", Toast.LENGTH_SHORT).show();

                break;
            case 3:          //个人账户页
                startActivity(new Intent(this,
                        PersonalSettingActivity.class));
                this.finish();

                break;
            case 4:         //活动页
                Toast.makeText(this, "活动页", Toast.LENGTH_SHORT).show();

                break;
            case 5:         //红包页
                startActivity(new Intent(this,
                        WelCenterActivity.class));
                this.finish();

                break;
            case 6:         //人脉邀请页
                Toast.makeText(this, "人脉邀请页", Toast.LENGTH_SHORT).show();

                break;
            case 7:         //积分商城页
                startActivity(new Intent(this,
                        TMallActivity.class));
                this.finish();

                break;
            case 8:         //调用分享按钮
                Bundle bundle = getIntent().getExtras();
                ShareInfo banner = (ShareInfo) bundle.getSerializable("banner");
                if (banner != null) {
                    loadObjSuc(banner);
                } else {
                    presenter.loadShareData(WebViewActivity.this);
                }

                break;
            case 9:          //注册页面
                startActivity(new Intent(this,
                        RegistActivity.class));

                break;
        }
    }

}

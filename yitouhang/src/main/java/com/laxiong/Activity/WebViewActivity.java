package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.laxiong.Application.YiTouApplication;
import com.laxiong.Mvp_presenter.Share_Presenter;
import com.laxiong.Mvp_view.IViewBasicObj;
import com.laxiong.Utils.DialogUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CommonActionBar;
import com.laxiong.entity.ShareInfo;
import com.laxiong.entity.User;
import com.umeng.socialize.UMShareAPI;
import com.gongshidai.mistGSD.R;

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
    //h5回调分享
    public void onShare() {
        ShareInfo obj = null;
        DialogUtils.getInstance(this).alertShareDialog(obj, mWebView);
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
            }

        };
        mWebView.setWebChromeClient(wvcc);
        if (titleStr != null)
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
                if (url.startsWith("gongshi2://")) {
                    WebViewActivity.this.finish();
                }
                mWebView.loadUrl(url);
                return true;
            }
        });
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

}

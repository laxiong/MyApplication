package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laxiong.Utils.StringUtils;
import com.laxiong.yitouhang.R;

public class WebViewActivity extends BaseActivity implements View.OnClickListener{
	/****
	 * html 页面  协议 活动 等
	 */
	private WebView mWebView ;
	private FrameLayout back_layout;
	private String urls ;  //链接
	private String mTitle ;
	private TextView mTitleTv ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		initData();

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back_layout:
				WebViewActivity.this.finish();
				break;
		}
	}
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void initData() {
		back_layout= (FrameLayout) findViewById(R.id.back_layout);
		back_layout.setOnClickListener(this);
		mWebView = (WebView)findViewById(R.id.webviews);
		mTitleTv =(TextView)findViewById(R.id.title);
		Intent intent = getIntent();
		if(intent!=null&& !StringUtils.isBlank(intent.getStringExtra("url")))
			urls=intent.getStringExtra("url");
		if(intent!=null&&!StringUtils.isBlank(intent.getStringExtra("title"))) {
			mTitle = intent.getStringExtra("title");
			mTitleTv.setText(mTitle);
		}
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
		
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("gongshi1://")){
					startActivity(new Intent(WebViewActivity.this,
							GuXiBaoActivity.class));
					WebViewActivity.this.finish();
				}
				mWebView.loadUrl(url);
				return true ;
			}
		});
		
		mWebView.loadUrl(urls);
		
	}
	
	

}

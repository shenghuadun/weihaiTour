package com.dtssAnWeihai.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebviewActivity extends BaseActionBarActivity {
	private WebView webView;
	private TextView web_title;
	private String weburl;
	private String title;
	private String status;
	
	public static Intent getIntent(Context context, String url, String title)
	{
		Intent intent = new Intent(context, WebviewActivity.class);
		intent.putExtra("weburl", url);
		intent.putExtra("title", title);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return intent;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		webView = (WebView) findViewById(R.id.webview);

		Intent intent = getIntent();
		weburl = intent.getExtras().getString("weburl");
		title = intent.getExtras().getString("title");
		status = intent.getExtras().getString("status");
		setTitle(title);

		final String mimeType = "text/html";
		final String encoding = "utf-8";

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setLoadWithOverviewMode(true);
		
		webView.loadUrl(weburl);
		// 加载数据
		webView.setWebViewClient(new WebViewClient() {
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				hideLoading();
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				showLoading();
			}
		});
		
		
		//begin:为定位添加的设置
		webSettings.setGeolocationEnabled(true);  
		//启用数据库  
		webSettings.setDatabaseEnabled(true);    
		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath(); 
		//设置定位的数据库路径  
		webSettings.setGeolocationDatabasePath(dir);   
		webSettings.setDomStorageEnabled(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin, Callback callback)
			{
				callback.invoke(origin, true, false);  
			    super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		 });
		//end:为定位添加的设置
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

package de.dd8pz.mywiki;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;

public class viewWiki {
	private Context context;
	private WebView webView;
	private mySettings MySettings;
	private ProgressDialog progressDialog;
	
	public viewWiki(Context cont,WebView View,mySettings Settings) {
		context=cont;
		webView=View;
		MySettings=Settings;
		webView.setWebViewClient(new WebViewClient() {
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView wView,String url) {
				if (MySettings.TestURL(url)) {
					Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
					context.startActivity(intent);
					return true;
				}
				webView.loadUrl(url);
				return true;
			}
			@Override
			public void onPageFinished(WebView wView,String url) {
				if (progressDialog!=null) {
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
				progressDialog=null;
				MySettings.setHistory(url);
			}
			@Override
			public void onPageStarted(WebView wView,String url,Bitmap favicon) {
				super.onPageStarted(wView, url, favicon);
				if (progressDialog==null || progressDialog.isShowing()) {
					progressDialog=new ProgressDialog(context);
					progressDialog.setTitle(R.string.Loading);
					progressDialog.setCancelable(true);
					progressDialog.setMax(100);
					progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
						@Override
						public void onCancel(DialogInterface dialog) {
							webView.stopLoading();
						}
					});
					progressDialog.show();
				}
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView wView,int newProgress) {
				if (progressDialog!=null) {
					if (progressDialog.isShowing()) {
						progressDialog.setProgress(newProgress);
					}
				}
			}
		});
	}
	public void LoginSite(String User,String Passwd) {
		/* TUDO Login to Site */
	}
	@SuppressLint("SetJavaScriptEnabled")
	public void LoadPage(String Url) {
		LoginSite(MySettings.getUser(),MySettings.getPasswd());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(Url);
	}
}

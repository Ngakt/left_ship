package com.example.huoda.cctv.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by chaisson on 2018/1/29.
 */

public class DefineWebClient extends WebViewClient {
    private static final String TAG = DefineWebClient.class.getSimpleName();
    long t = 0;
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    //设置加载前的函数
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        //System.out.println("开始加载了");
        //beginLoading.setText("开始加载了");
        t = System.currentTimeMillis();
    }

    //设置结束加载函数
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //endLoading.setText("结束加载了");
        Log.i(TAG,"WebView加载耗时："+(System.currentTimeMillis() - t));
    }
}

package com.example.huoda.cctv.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.huoda.cctv.R;
import com.example.huoda.cctv.callback.BarcodeCallBack;
import com.example.huoda.cctv.util.HandleMainActivityResult;
import com.example.huoda.cctv.util.PermissionHandler;
import com.example.huoda.cctv.util.WebSettingsUtil;
import com.example.huoda.cctv.webview.ActivityResultConst;
import com.example.huoda.cctv.webview.DefineWebChromeClient;
import com.example.huoda.cctv.webview.DefineWebClient;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private WebView webView;
    private ValueCallback<Uri> mUploadMessage;             //android 低版本表单数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;   //android 7.0+表单数据信息
    private Uri imageUri;
    private long exitTime = 0;
    private TextView beginLoading,endLoading,loading,mtitle;
    private ProgressBar pg1;
    //在进行Intent跳转时没有办法指定REQUEST_CODE时，通过此extra指定获取，onActivityResult使用后，及时清除
    public Map<String,Object> extra = new HashMap<>();

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = this.findViewById(R.id.webView);
        //beginLoading = (TextView) findViewById(R.id.text_beginLoading);
        //endLoading = (TextView) findViewById(R.id.text_endLoading);
        //loading = (TextView) findViewById(R.id.text_Loading);
        //mtitle = (TextView) findViewById(R.id.title);
        //pg1=(ProgressBar) findViewById(R.id.progressBar1);
        //设置WebView的属性
        WebSettingsUtil.doSetting(webView);
        //设置不用系统浏览器打开,直接显示在当前Webview
        webView.setWebViewClient(new DefineWebClient());
        //设置WebChromeClient类
        webView.setWebChromeClient(new DefineWebChromeClient(this));
        //增加二维码扫描的JS回调
        webView.addJavascriptInterface(new BarcodeCallBack(MainActivity.this), "barcodeCallBack");
        //检查照相机和存储权限
        PermissionHandler.checkPermissionForCameraAndWriteStorage(this);

        //android 7.0系统解决拍照的问题
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();

        //web环境访问
        //webView.loadUrl("http://10.40.100.107:8080/tnms/pages/ra/zznodejsjk/login.html");
        //本地HMTL5方式访问
        webView.loadUrl("file:///android_asset/demo/MyIndex.html");
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            Log.i(TAG,"----------------"+webView.getUrl());
            if(webView.getUrl().indexOf("/resource/EngineeringMeasurementChild.html") != -1){
                //注：/resource/EngineeringMeasurementChild.html页面是扫二维码之后的跳转页面，goBack会退到扫码页面，所以goBack(-2) 退2步
                webView.goBackOrForward(-2);
                return true;
            }  else {
                if(webView.getUrl().indexOf("/index.html") < 0){
                    //默认index.html为登陆后首页，不能再退
                    webView.goBack();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //1.webView.canGoBack()判断网页是否能后退,可以则goback()
    //2.如果不可以连续点击两次退出App,否则弹出提示Toast
    @Override
    public void onBackPressed() {
        if (webView.canGoBack() && webView.getUrl().indexOf("/index.html") < 0) {
            webView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //自定义Activity返回结果码
        Integer extraRequestCode = (Integer) extra.get(ActivityResultConst.REQUEST_CODE_KEY);
        if(extraRequestCode == null || extraRequestCode == 0){
            //如果没有自定义返回结果码，则使用onActivityResult的参数
            extraRequestCode = requestCode;
        }
        //根据不同的返回码，执行不同的结果处理(一般指回调操作)
        if (extraRequestCode == ActivityResultConst.BARCODE_RESULTCODE) {
            HandleMainActivityResult.onBarcodeResult(this, requestCode, resultCode, data);
        }
        if (extraRequestCode == ActivityResultConst.CAMERA_RESULTCODE) {
            HandleMainActivityResult.onCameraResult(this,requestCode,resultCode,data);
        }
        extra.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HandleMainActivityResult.onRequestPermissionsResult(this,requestCode,permissions,grantResults);

    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    public WebView getWebView() {
        return webView;
    }


    public ValueCallback<Uri> getmUploadMessage() {
        return mUploadMessage;
    }

    public void setmUploadMessage(ValueCallback<Uri> mUploadMessage) {
        this.mUploadMessage = mUploadMessage;
    }

    public ValueCallback<Uri[]> getmUploadCallbackAboveL() {
        return mUploadCallbackAboveL;
    }

    public void setmUploadCallbackAboveL(ValueCallback<Uri[]> mUploadCallbackAboveL) {
        this.mUploadCallbackAboveL = mUploadCallbackAboveL;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}

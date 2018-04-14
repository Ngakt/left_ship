package com.example.huoda.cctv.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.huoda.cctv.activity.MainActivity;
import com.example.huoda.cctv.util.PhotoUtil;


/**
 * Created by chaisson on 2018/1/29.
 */

public class DefineWebChromeClient extends WebChromeClient {

    private static final String TAG = DefineWebChromeClient.class.getSimpleName();

    private MainActivity mainActivity;

    public DefineWebChromeClient(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    //获取网站标题
    @Override
    public void onReceivedTitle(WebView view, String title) {
        //System.out.println("标题在这里");
        //mtitle.setText(title);
    }

    //获取加载进度
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if(newProgress==100){
            //pg1.setVisibility(View.GONE);//加载完网页进度条消失
        } else{
            //pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
            //pg1.setProgress(newProgress);//设置进度值
        }
    }
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        mainActivity.setmUploadCallbackAboveL(filePathCallback);
        try {
            PhotoUtil.take(mainActivity);
        } catch (SecurityException e){
            Log.e(TAG,e.getMessage(),e);
            if (e.getMessage() != null && e.getMessage().indexOf("Permission Denial") != -1) {
                String tipTitle = "缺少权限";
                if (e.getMessage().indexOf("android.permission.WRITE_EXTERNAL_STORAGE") != -1) {
                    tipTitle = "缺少存储权限";
                } else if(e.getMessage().indexOf("android.permission.CAMERA") != -1){
                    tipTitle = "缺少相机权限";
                }
                new AlertDialog.Builder(mainActivity)
                        .setTitle("温馨提示")
                        .setMessage(tipTitle + "，可在-应用设置-权限管理-中，手动开启")
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do Nothing
                            }
                        }).setCancelable(false).show();
            }
        }
        return true;
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        mainActivity.setmUploadMessage(uploadMsg);
        PhotoUtil.take(mainActivity);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mainActivity.setmUploadMessage(uploadMsg);
        PhotoUtil.take(mainActivity);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mainActivity.setmUploadMessage(uploadMsg);
        PhotoUtil.take(mainActivity);
    }
}

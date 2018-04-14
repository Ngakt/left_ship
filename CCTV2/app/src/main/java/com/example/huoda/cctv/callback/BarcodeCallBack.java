package com.example.huoda.cctv.callback;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.huoda.cctv.activity.MainActivity;
import com.example.huoda.cctv.activity.ScanActivity;
import com.example.huoda.cctv.webview.ActivityResultConst;
import com.google.zxing.integration.android.IntentIntegrator;


/**
 * Created by Chaisson on 2018/1/24.
 */

public class BarcodeCallBack {
    private Context context;
    public BarcodeCallBack(Context context) {
        this.context = context;
    }

    //将显示Toast和对话框的方法暴露给JS脚本调用
    @JavascriptInterface
    public void showToast(String name) {
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void showDialog() {
        MainActivity activity = (MainActivity)context;
        IntentIntegrator integrator = new IntentIntegrator(activity);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setPrompt("请扫描二维码"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
        activity.extra.put(ActivityResultConst.REQUEST_CODE_KEY,ActivityResultConst.BARCODE_RESULTCODE);
        /*new AlertDialog.Builder(context)
                .setTitle("联系人列表")
                .setItems(new String[]{"基神", "B神", "曹神", "街神", "翔神"}, null)
                .setPositiveButton("确定", null).create().show();*/

    }
}

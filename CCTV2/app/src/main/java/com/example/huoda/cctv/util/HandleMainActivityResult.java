package com.example.huoda.cctv.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.huoda.cctv.activity.MainActivity;
import com.example.huoda.cctv.webview.ActivityResultConst;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by chaisson on 2018/1/29.
 */

public class HandleMainActivityResult {

    private static final String TAG = HandleMainActivityResult.class.getSimpleName();

    public static void onBarcodeResult(MainActivity activity, int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            Log.e("HYN", ""+result);
            if(resultCode == Activity.RESULT_CANCELED){
                //Toast.makeText(activity, "已取消扫码", Toast.LENGTH_LONG).show();
                activity.getWebView().goBack();
            } else {
                Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                activity.getWebView().loadUrl("javascript:showBarcodeResult('"+result+"')");//调用页面的js方法，用以在页面显示扫码结果
            }
        }
    }

    public static void onCameraResult(MainActivity activity, int requestCode, int resultCode, Intent data) {
            updatePhotos(activity);

            if (null == activity.getmUploadMessage() && null == activity.getmUploadCallbackAboveL()) return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            if (activity.getmUploadCallbackAboveL() != null) {
                onActivityResultAboveL(activity, requestCode, resultCode, data);
            } else if (activity.getmUploadMessage() != null) {
                Log.e("result", result + "");
                if (result == null) {
                    //低版本，暂时不做压缩处理
                    activity.getmUploadMessage().onReceiveValue(activity.getImageUri());
                    activity.setmUploadMessage(null);
                    Log.e("imageUri", activity.getImageUri() + "");
                } else {
                    //压缩
                    result = PhotoUtil.doCompressImageForActivityResult(activity, data, null);
                    activity.getmUploadMessage().onReceiveValue(result);
                    activity.setmUploadMessage(null);
                }
            }
    }

    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void onActivityResultAboveL(MainActivity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode != ActivityResultConst.CAMERA_RESULTCODE
                || activity.getmUploadCallbackAboveL() == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{activity.getImageUri()};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            results = PhotoUtil.doCompressImageForActivityResult(activity,results);
            activity.getmUploadCallbackAboveL().onReceiveValue(results);
            activity.setmUploadCallbackAboveL(null);
        } else {
            results = new Uri[]{activity.getImageUri()};
            results = PhotoUtil.doCompressImageForActivityResult(activity,results);
            activity.getmUploadCallbackAboveL().onReceiveValue(results);
            activity.setmUploadCallbackAboveL(null);
        }
        return;
    }

    private static void updatePhotos(MainActivity activity) {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(activity.getImageUri());
        activity.sendBroadcast(intent);
        //已赋写权限，可写比可读，不用复用check权限
//        int permission = ActivityCompat.checkSelfPermission(activity,
//                Manifest.permission.READ_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            String[] PERMISSIONS_STORAGE = {
//                    Manifest.permission.READ_EXTERNAL_STORAGE};
//
//            final int REQUEST_EXTERNAL_STORAGE = 1;
//            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE);
//        }
    }

    //权限回调方法（默认知道某个requestCode赋值几个权限，数组大小为几）
    public static void onRequestPermissionsResult(final MainActivity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ActivityResultConst.CODE_FOR_WRITE_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean isShow = true;
                Log.e(TAG, "grantResults.length:"+grantResults.length);
                String tipTitle = "权限不可用";
                if(grantResults.length == 2){
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                        tipTitle = "相机和存储权限不可用";
                    } else if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                        tipTitle = "相机权限不可用";
                    } else if(grantResults[1] != PackageManager.PERMISSION_GRANTED){
                        tipTitle = "存储权限不可用";
                    } else {
                        //权限已经全部赋值成功
                        isShow = false;
                    }
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean completeForbidden1 = activity.shouldShowRequestPermissionRationale(permissions[0]);
                    boolean completeForbidden2 = activity.shouldShowRequestPermissionRationale(permissions[1]);
                    Log.e(TAG, permissions[0] + " isCompleteForbidden: " + completeForbidden1 + "；" + permissions[1] + " isCompleteForbidden: " + completeForbidden2);
                    if(!completeForbidden1 || !completeForbidden2){
                        //用户点击了禁止后不再询问，建议直接提示并退出
                        Toast.makeText(activity.getApplicationContext(), "以后可在-应用设置-权限管理-中，手动开启权限", Toast.LENGTH_SHORT).show();
                    } else {
                        if (isShow) {
                            new AlertDialog.Builder(activity)
                                    .setTitle(tipTitle)
                                    .setMessage("由于手机助手需要拍照上传和扫描二维码功能，请开启权限；\n否则，您将无法正常使用")
                                    .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            PermissionHandler.checkPermissionForCameraAndWriteStorage(activity);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Toast.makeText(activity.getApplicationContext(), "以后可在-应用设置-权限-中，手动开启权限", Toast.LENGTH_SHORT).show();
                                        }
                                    }).setCancelable(false).show();
                        }
                    }
                } else {
                    PermissionHandler.checkPermissionForCameraAndWriteStorage(activity);
                }
            }
        }
    }
}

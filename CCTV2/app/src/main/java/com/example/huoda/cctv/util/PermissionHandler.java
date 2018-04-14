package com.example.huoda.cctv.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import com.example.huoda.cctv.activity.MainActivity;
import com.example.huoda.cctv.webview.ActivityResultConst;


/**
 * Created by chaisson on 2018/1/29.
 */

public class PermissionHandler {

    public static void checkPermissionForCameraAndWriteStorage(MainActivity mainActivity){
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = mainActivity.checkSelfPermission(Manifest.permission.CAMERA);
            int j = mainActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED || j != PackageManager.PERMISSION_GRANTED ) {
                // 如果没有授予该权限，就去提示用户请求
                String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                mainActivity.requestPermissions(PERMISSIONS_STORAGE, ActivityResultConst.CODE_FOR_WRITE_PERMISSION);
            }
        }
    }
}

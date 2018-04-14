package com.example.huoda.img_url;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA};
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions,
                    1);
//        SmartImageView siv = (SmartImageView) findViewById(R.id.siv);
//                siv.setImageUrl("http://192.168.1.130:8080/WebServer/img/a.jpg", R.drawable.ic_launcher_foreground);
        //siv.setImageUrl("", R.drawable.ic_launcher_foreground);
        imageView=(ImageView) findViewById(R.id.imageView);
    }


    public void onClick(View view) {
        ImageUtils.showImagePickDialog(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                if(resultCode == RESULT_CANCELED) {
                    return;
                }
                Uri imageUri = data.getData();

                System.out.println(imageUri);
                //这里得到图片后做相应操作

               String path = UriToPathUtil.getImageAbsolutePath(this,imageUri);
               System.out.println(path);
               // Bitmap bitmap2 = data.getParcelableExtra(path);
                Bitmap bitmap2 = BitmapFactory.decodeFile(path);
                System.out.println(bitmap2);
                this.imageView.setImageBitmap(bitmap2);

                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                if(resultCode == RESULT_CANCELED) {
                    ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
                } else {
                    Uri imageUriCamera = ImageUtils.imageUriFromCamera;
                    System.out.println(imageUriCamera);
//                    crop(imageUriCamera);
                    //这里得到图片后做相应操作
                    String path2 = UriToPathUtil.getImageAbsolutePath(this,imageUriCamera);
                    System.out.println(path2);
                    // Bitmap bitmap2 = data.getParcelableExtra(path);
                    Bitmap bitmap = BitmapFactory.decodeFile(path2);
                    System.out.println(bitmap);
                    this.imageView.setImageBitmap(bitmap);
                }

                break;
            default:
                break;
        }
    }

    /*
    * 剪切图片
    */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        intent.putExtra("outputFormat", "PNG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }





}
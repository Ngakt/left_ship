package com.example.huoda.cctv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnCamera;
    private Button btnGallery;
    private ImageView imageView;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    1);}

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
        doNext2(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted

            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();
                Toast.makeText(this, "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult2(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        doNext(requestCode, grantResults);
//    }

    private void doNext2(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted

            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();
                Toast.makeText(this, "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

//    public void onclick(View view)
//    {
//        ImageUtils.showImagePickDialog(this);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap bitmap=CameraGallaryUtil.getBitmapFromCG(this,requestCode,resultCode,data);
//        imageView.setImageBitmap(bitmap);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.btnCamera:
//                // 利用系统自带的相机应用:拍照
//                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null
//                // 如果此处指定，则后来的data为null
//                // 只有指定路径才能获取原图
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, CameraGallaryUtil.fileUri);
//                startActivityForResult(intent, CameraGallaryUtil.PHOTO_REQUEST_TAKEPHOTO);
//                break;
//            case R.id.btnGallery:
//                intent = new Intent(Intent.ACTION_PICK, null);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                System.out.println(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//             startActivityForResult(intent, CameraGallaryUtil.PHOTO_REQUEST_GALLERY);
//                break;
//        }
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }


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

                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                if(resultCode == RESULT_CANCELED) {
                    ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
                } else {
                    Uri imageUriCamera = ImageUtils.imageUriFromCamera;
System.out.println(imageUriCamera);
                    //这里得到图片后做相应操作

                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        ImageUtils.showImagePickDialog(this);
    }
}

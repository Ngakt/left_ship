package com.example.huoda.cctv.util;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;


import com.example.huoda.cctv.activity.MainActivity;
import com.example.huoda.cctv.webview.ActivityResultConst;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by chaisson on 2018/1/29.
 */

public class PhotoUtil {
    private static final String TAG = PhotoUtil.class.getSimpleName();

    public static void take(MainActivity mainActivity) {
        Log.i(TAG,""+ Environment.getExternalStorageDirectory().getPath());
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        File file = new File(filePath + fileName);
        mainActivity.setImageUri(Uri.fromFile(file));

//        //调用照相机和浏览图片库代码
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mainActivity.getImageUri());
//
//        Intent Photo = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
//
//        mainActivity.startActivityForResult(chooserIntent, 6);

        //调用系统相机，此代码在android 7.0以上有问题，需要在onCreate方法加入StrictMode.VmPolicy.Builder解决办法
//        Intent intentCamera = new Intent();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//        }
//        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        //将拍照结果保存至photo_file的Uri中，不保留在相册中
//        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intentCamera, FILECHOOSER_RESULTCODE);

        //兼容android 7.0+版本的照相机调用代码
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {
             /*获取当前系统的android版本号*/
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            Log.e(TAG,"currentapiVersion====>"+currentapiVersion);
            if (currentapiVersion<24){
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mainActivity.getImageUri());
                mainActivity.startActivityForResult(intent, ActivityResultConst.CAMERA_RESULTCODE);
            }else {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                Uri uri = mainActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                mainActivity.startActivityForResult(intent, ActivityResultConst.CAMERA_RESULTCODE);
            }
        } else {
            Toast.makeText(mainActivity, "照相机不存在", Toast.LENGTH_SHORT).show();
        }
    }

    //把Intent获取的图片压缩，并转化成Uri（当然原来数据类型也就是Uri）
    public static Uri[] doCompressImageForActivityResult(MainActivity mainActivity,Uri[] results){
        List<Uri> resultList = new ArrayList<Uri>();
        for(Uri uri : results){
            resultList.add(doCompressImageForActivityResult(mainActivity,null,uri));
        }
        return resultList.toArray(new Uri[resultList.size()]);
    }

    //把Intent获取的图片压缩，并转化成Uri（当然原来数据类型也就是Uri）
    public static Uri doCompressImageForActivityResult(MainActivity mainActivity,Intent data, Uri uri){
        String path = uri != null ? uri.getPath() : null;
        if(path == null && data != null){
            path = getAbsoluteImagePath(mainActivity,data.getData());
        }
        if(path == null){
            throw new RuntimeException("无法获取图片路径 : path is null.");
        }
        Bitmap bitmap = getImage(path);
        //转成url
        Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(
                mainActivity.getContentResolver(), bitmap, null, null));
        return imageUri;
    }

    /**
     * @desc 图片按比例大小压缩方法（根据Bitmap图片压缩）
     * @ref http://blog.csdn.net/cherry609195946/article/details/9264409
     * @param srcPath 压缩图片url
     * @return Bitmap
     */
    private static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static String getAbsoluteImagePath(MainActivity mainActivity, Uri uri) {
        // can post image
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = mainActivity.managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}

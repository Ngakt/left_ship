//package com.example.huoda.cctv;
//
///**
// * @author Admin
// * @version $Rev$
// * @des ${TODO}
// * @updateAuthor $Author$
// * @updateDes ${TODO}
// */
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.provider.MediaStore;
//import android.support.v4.content.FileProvider;
//import android.util.Log;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import me.weyye.hipermission.HiPermission;
//import me.weyye.hipermission.PermissionCallback;
//import me.weyye.hipermission.PermissionItem;
//
///***
// * 图片裁剪工具类
// */
//public class ImageCutUtil {
//    Activity context;
//    private static final String TAG = "MAIN";
//
//    public ImageCutUtil(Activity context) {
//        this.context = context;
//    }
//
//
//    //===============================================================================================================================
//
//    /***
//     * 本地图片选择器
//     * @param data
//     */
//    public void cutImageLocal(Intent data) {
//        Uri imageUri = data.getData();
//        String url_out = AppConstant.CACHE_IMAGE;
//        File file_out = new File(AppConstant.CACHE_IMAGE);
//        if (file_out.exists()) {
//            file_out.delete();
//        }
//        Uri outputUri = Uri.fromFile(new File(url_out));
//        cutImage(imageUri, outputUri);
//    }
//
//    //============================================================================================================================================
//
//    public void cutImage(String inputUrl, String output) {
//        try {
//            File file_out = new File(output);
//            if (file_out.exists()) {
//                file_out.delete();
//            }
//            file_out.createNewFile();
//            File fileinput = new File(inputUrl);
//            Uri imageUri;
//            Uri outputUri;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                imageUri = FileProvider.getUriForFile(context, AppConstant.authorities, fileinput);
//                outputUri = Uri.fromFile(new File(output));
//            } else {
//                imageUri = Uri.fromFile(fileinput);
//                outputUri = Uri.fromFile(new File(output));
//            }
//            cutImage(imageUri, outputUri);
//        } catch (Exception e) {
//        }
//    }
//
//    public void cutImage(Uri imageUri, Uri outputUri) {
//        try {
//            Intent intent = new Intent("com.android.camera.action.CROP");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            }
//            intent.setDataAndType(imageUri, "image/*");
//            intent.putExtra("crop", "true");
//            //设置宽高比例
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 2);
//            intent.putExtra("scale", true);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
//            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//            intent.putExtra("noFaceDetection", true);
//            context.startActivityForResult(intent, AppConstant.IMAGE_CUT_BACK);
//        } catch (Exception e) {
//        }
//    }
//
//
////    public void cutImage(String inputUrl, String output) {
////        try {
////            File file_out = new File(output);
////            if (file_out.exists()) {
////                file_out.delete();
////            }
////            file_out.createNewFile();
////            File fileinput = new File(inputUrl);
////            Uri imageUri;
////            Uri outputUri;
////            Intent intent = new Intent("com.android.camera.action.CROP");
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                imageUri = FileProvider.getUriForFile(context, AppConstant.authorities, fileinput);
////                outputUri = Uri.fromFile(new File(output));
////            } else {
////                imageUri = Uri.fromFile(fileinput);
////                outputUri = Uri.fromFile(new File(output));
////            }
////            intent.setDataAndType(imageUri, "image/*");
////            intent.putExtra("crop", "true");
////            //设置宽高比例
////            intent.putExtra("aspectX", 1);
////            intent.putExtra("aspectY", 2);
////            //设置裁剪图片宽高
////            // intent.putExtra("outputX", 300);
////            // intent.putExtra("outputY", 300);
////            intent.putExtra("scale", true);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
////            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
////            intent.putExtra("noFaceDetection", true);
////            context.startActivityForResult(intent, AppConstant.IMAGE_CUT_BACK);
////        } catch (Exception e) {
////        }
////    }
//
//
//    public void checkSdImagePermiss() {
//        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
//        permissions.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "SD卡读取权限", R.drawable.permission_ic_phone));
//        permissions.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "SD卡读取权限", R.drawable.permission_ic_phone));
//
//        HiPermission.create(context).title(context.getString(R.string.permission_cus_title)).permissions(
//                permissions).msg(context.getString(R.string.permission_cus_msg)).animStyle(R.style.PermissionAnimModal).style(
//                R.style.PermissionDefaultBlueStyle).checkMutiPermission(new PermissionCallback() {
//            @Override
//            public void onClose() {
//                Log.i(TAG, "用户拒绝我们的权限");
//            }
//
//            @Override
//            public void onFinish() {
//                try {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/*");
//                    context.startActivityForResult(intent, AppConstant.LOCAL_FILE_BACK);
////                    Intent intent = new Intent(context, LoImgChooiceActivity.class);
////                    context.startActivityForResult(intent, AppConstant.LOCAL_FILE_BACK);
//                } catch (Exception e) {
//                    Log.e("main", "====打开相机异常====" + e.toString());
//                }
//            }
//
//            @Override
//            public void onDeny(String permission, int position) {
//                MyToastView.getInstance().Toast(context, "您拒绝了" + permission + "权限,请手动打开");
//                Log.i(TAG, "用户禁止了权限" + permission);
//            }
//
//            @Override
//            public void onGuarantee(String permission, int position) {
//                Log.i(TAG, "onGuarantee");
//            }
//        });
//    }
//
//    /***
//     * 检查相机权限
//     */
//    public void checkCameraPermission() {
//        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
//        permissions.add(
//                new PermissionItem(Manifest.permission.CAMERA, "SD卡读取权限", R.drawable.permission_ic_phone));
//        HiPermission.create(context).title(context.getString(R.string.permission_cus_title)).permissions(
//                permissions).msg(context.getString(R.string.permission_cus_msg)).animStyle(R.style.PermissionAnimModal).style(
//                R.style.PermissionDefaultBlueStyle).checkMutiPermission(new PermissionCallback() {
//            @Override
//            public void onClose() {
//                Log.e("main", "====onClose====");
//            }
//
//            @Override
//            public void onFinish() {
//                try {
//                    takeCameraPhoto();
//                } catch (Exception e) {
//                    MyToastView.getInstance().Toast(context, e.toString());
//                    Log.e("main", "====打开相机异常====" + e.toString());
//                }
//            }
//
//            @Override
//            public void onDeny(String permission, int position) {
//                Log.e("main", "====打开相机异常====");
//                MyToastView.getInstance().Toast(context, "您拒绝了" + permission + "权限,请手动打开");
//            }
//
//            @Override
//            public void onGuarantee(String permission, int position) {
//                Log.e("main", "====onGuarantee====");
//            }
//        });
//    }
//
//    /***
//     * 拍照返回
//     */
//    public void takeCameraPhoto() {
//        try {
//            FileUtils.creatFileNotExciet();
//            File file = new File(AppConstant.cameralSavePath);
//            if (file.exists()) {
//                file.delete();
//            }
//            file.createNewFile();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //7.0执行该方法
//                Uri imageUri = FileProvider.getUriForFile(context, AppConstant.authorities, file);
//                Intent intent = new Intent();
//                //添加这一句表示对目标应用临时授权该Uri所代表的文件
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
//                context.startActivityForResult(intent, AppConstant.CAMERA_IMAGE_BACK);
//            } else {  //7.0以下手机系统执行这个方法
//                Intent getPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri uri = Uri.fromFile(file);
//                getPhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);//根据uri保存照片
//                getPhoto.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//保存照片的质量
//                context.startActivityForResult(getPhoto, AppConstant.CAMERA_IMAGE_BACK);//启动相机拍照
//            }
//        } catch (Exception e) {
//            MyToastView.getInstance().Toast(context, e.toString());
//        }
//    }
//
//}
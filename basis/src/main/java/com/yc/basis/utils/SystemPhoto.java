package com.yc.basis.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;

import com.yc.basis.entrance.MyApplication;

import java.io.File;

public class SystemPhoto {

    public static final int photoCode = 1002;
    public static final int albumCode = 1003;

    /**
     * 调用系统相册选择图片
     * Uri selectedImage = data.getData();
     */
    public static void startSystemPhoto(Activity activity, int code) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        activity.startActivityForResult(intent, code);
    }

    //去拍照
    // if (PermissionUtils.isPermission(activity, new String[]{
    //                Manifest.permission.READ_EXTERNAL_STORAGE,
    //                Manifest.permission.WRITE_EXTERNAL_STORAGE,
    //                Manifest.permission.CAMERA
    //        }, albumQxCode)) {
    public static String goPhotoAlbum(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String path = File10Util.getBasePath("photo") + "/" + System.currentTimeMillis() + ".jpg";
        File file = new File(path);
        Uri uri = FileUtilOld.fileToUri(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, albumCode);
        return path;
    }

    //去拍照
    public static String goPhotoAlbum(Activity activity, int code) {
        if (PermissionUtils.isPermission(activity, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, code)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String path = File10Util.getBasePath("image_cache") + "/" + System.currentTimeMillis() + ".jpg";
            //FileUtilOld.saveBaseBitmapPath(DataUtils.timeToData(System.currentTimeMillis(), "yyyy-MM-dd-HH:mm:ss")+".png")
            File file = new File(path);
            Uri uri = FileUtilOld.fileToUri(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, code);
            return path;
        }
        return "";
    }


    //通知相册跟新
    public static void noticePhoto(String path) {
        try {
            if (path.startsWith("content://")) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                MyApplication.context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.parse(path);
                intent.setData(uri);
                MyApplication.context.sendBroadcast(intent);
            }
            MediaScannerConnection.scanFile(MyApplication.context,
                    new String[]{path}, null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

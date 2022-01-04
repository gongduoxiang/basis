package com.yc.basis.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.yc.basis.R;
import com.yc.basis.entrance.MyApplication;
import com.yc.basis.http.BaseDownloadCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//android10的操作
public class File10Util {

    //创建文件夹
    public static boolean createFolder(String name) {
        File file = MyApplication.context.getFileStreamPath(name);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    //获取根目录
    public static String getBasePath(String name) {
        if (DataUtils.isEmpty(name))
            return MyApplication.context.getFilesDir().getAbsolutePath();
        createFolder(name);
        return MyApplication.context.getFileStreamPath(name).getAbsolutePath();
    }

    public static String getBasePath() {
        return getBasePath("");
    }

    //获取指定文件的目录文件
    public static String getFilePath(String folder, String file) {
        String path;
        if (DataUtils.isEmpty(folder)) {
            path = getBasePath() + File.separator + file;
        } else {
            createFolder(folder);
            path = getBasePath() + File.separator + folder + File.separator + file;
        }
        return path;
    }

    public static void saveFile(String folder, String file, InputStream is, BaseDownloadCallBack listener) {
        if (!DataUtils.isEmpty(folder))
            createFolder(folder);
        if (DataUtils.isEmpty(file)) {
            Toaster.toast(R.string.toast_3);
            if (listener != null) listener.failed();
            return;
        }
        File newFile = new File(getFilePath(folder, file));
        if (newFile.exists()) {
            try {
                if (newFile.length() == is.available()) {
                    if (listener != null)
                        listener.success(newFile.getAbsolutePath());
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            newFile.delete();
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    if (listener != null)
                        listener.successBack(new FileOutputStream(newFile), is, is.available(), newFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) listener.failed();
                }
            }
        }.start();
    }

    public static void saveFile(String file, InputStream is, BaseDownloadCallBack listener) {
        saveFile("", file, is, listener);
    }

    public static void saveFile(String file, InputStream is, long total, BaseDownloadCallBack listener) {
        saveFile("", file, is, listener);
    }

    public static String getFilePath(String file) {
        return getFilePath("", file);
    }

    public static String saveBitmap(String file, Bitmap bitmap) {
        if (DataUtils.isEmpty(file)) {
            Toaster.toast(R.string.toast_3);
            return "";
        }
        return saveBitmap("photo", file, bitmap);
    }

    public static String saveBitmap(String folder, String file, Bitmap bitmap) {
        if (DataUtils.isEmpty(file)) {
            Toaster.toast(R.string.toast_3);
            return "";
        }
        File newFile = new File(getFilePath(folder, file));
        try {
            //通过流将图片写入
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            MyLog.i("保存的图片路径 " + newFile.getAbsolutePath());
            return newFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //android10保存到下载目录的适配
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void saveFileDownload(String fileName, long total, InputStream is, final BaseDownloadCallBack listener) {
        ContentResolver resolver = MyApplication.context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
//         values.put(MediaStore.Downloads.VOLUME_NAME, fileName);
        //设置文件类型   getFileType
//        values.put(MediaStore.Downloads.MIME_TYPE, ".doc");
        //注意MediaStore.Downloads.RELATIVE_PATH需要targetVersion=29,
        //故该方法只可在Android10的手机上执行
        values.put(MediaStore.Downloads.RELATIVE_PATH, "Download");
        Uri external = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        final Uri insertUri = resolver.insert(external, values);
        if (insertUri != null) {
            //若生成了uri，则表示该文件添加成功
            //使用流将内容写入该uri中即可
            try {
                if (listener != null)
                    listener.successBack(MyApplication.context.getContentResolver().openOutputStream(insertUri),
                            is, total, insertUri.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if (listener != null) listener.failed();
            }
        } else {
            MyLog.e("android10 保存文件到下载目录 失败  ");
            if (listener != null) listener.failed();
        }
    }

    //保存到相册
    public static void savePhotoAlbum(String path) {
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            insert2Album(fis);
        } catch (Exception e) {
            e.printStackTrace();
            Toaster.toast("通知相册更新图片失败");
        }
    }


    //fileName为需要保存到相册的图片名
    private static void insert2Album(InputStream inputStream) {
        if (inputStream == null)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //RELATIVE_PATH 字段表示相对路径-------->(1)
            contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        } else {
            String dstPath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES
                    + File.separator + System.currentTimeMillis() + ".jpg";
            //DATA字段在Android 10.0 之后已经废弃
            contentValues.put(MediaStore.Images.ImageColumns.DATA, dstPath);
        }
        //插入相册------->(2)
        Uri uri = MyApplication.context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        //写入文件------->(3)
        write2File(uri, inputStream);
    }

    //inputStream 表示原始的文件流
    private static void write2File(Uri uri, InputStream inputStream) {
        if (uri == null || inputStream == null)
            return;
        try {
            //从Uri构造输出流
            OutputStream outputStream = MyApplication.context.getContentResolver().openOutputStream(uri);
            byte[] in = new byte[1024];
            int len = 0;
            do {
                //从输入流里读取数据
                len = inputStream.read(in);
                if (len != -1) {
                    outputStream.write(in, 0, len);
                    outputStream.flush();
                }
            } while (len != -1);

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(uri);
            MyApplication.context.sendBroadcast(intent);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.yc.basis.utils;

import android.graphics.Bitmap;
import android.os.Build;

import com.yc.basis.http.BaseDownloadCallBack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

//做android10文件适配
public class FileUtil {

    //创建文件夹
    public static boolean createFolder(String name) {
        //小于android10
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return File9Utils.createFolder(name);
        } else {
            return File10Util.createFolder(name);
        }
    }

    // 在指定文件夹下面创建文件
    public static void saveFile(String folder, String file, InputStream is, long total, BaseDownloadCallBack listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            File9Utils.saveFile(folder, file, is, total, listener);
        } else {
            File10Util.saveFile(folder, file, is,  listener);
        }
    }

    public static void saveFile(String folder, String name, InputStream is, BaseDownloadCallBack listener) {
        saveFile(folder, name, is, 0, listener);
    }

    public static void saveFile(String file, InputStream is, long total, BaseDownloadCallBack listener) {
        saveFile("", file, is, total, listener);
    }

    public static void saveFile(String file, InputStream is, BaseDownloadCallBack listener) {
        saveFile("", file, is, 0, listener);
    }

    //查询文件
    public static String findFile(String folder, String file) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return File9Utils.getFilePath(folder, file);
        } else {
            return File10Util.getFilePath(folder, file);
        }
    }

    public static String findFile(String file) {
        return findFile("", file);
    }

    //保存图片到下载目录
    public static void saveFileDownloadBitmap(String fileName, Bitmap bitmap) {
        saveFileDownloadBitmap(fileName, bitmap, null);
    }

    //保存图片到下载目录
    public static void saveFileDownloadBitmap(String fileName, Bitmap bitmap,
                                              BaseDownloadCallBack listener) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        saveFileDownload(fileName, getBitmapSize(bitmap), isBm, listener);
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    //保存文件到下载目录
    public static void saveFileDownload(String fileName, InputStream is) {
        saveFileDownload(fileName, 0, is, null);
    }

    //保存文件到下载目录
    public static void saveFileDownload(String fileName, long total, InputStream is,
                                        BaseDownloadCallBack listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            File9Utils.saveFileDownload(fileName, total, is, listener);
        } else {
            File10Util.saveFileDownload(fileName, total, is, listener);
        }
    }

}

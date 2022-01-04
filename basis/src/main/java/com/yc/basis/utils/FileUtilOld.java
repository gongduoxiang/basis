package com.yc.basis.utils;

import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.yc.basis.entrance.MyApplication;
import com.yc.basis.http.BasisInfo;

import java.io.File;

//适配android10
public class FileUtilOld {

    /**
     * file转uri
     * 7.0  filepaths 适配
     */
    public static Uri fileToUri(File file) {
        Uri uri;
        try {
            if (file == null) {
                Toaster.toast("文件不能为空");
                return Uri.parse("");
            }
            if (Build.VERSION.SDK_INT >= 23) {
                uri = FileProvider.getUriForFile(MyApplication.context, BasisInfo.getFileprovider(), file);
            } else {
                uri = Uri.fromFile(file);
            }
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse("");
    }

    /**
     * file转uri
     * 7.0  filepaths 适配
     */
    public static Uri fileToUri(String path) {
        Uri uri;
        if (path.startsWith("content://")) {
            return Uri.parse(path);
        }
        try {
            File file = new File(path);
            if (Build.VERSION.SDK_INT >= 22) {
                uri = FileProvider.getUriForFile(MyApplication.context, BasisInfo.getFileprovider(), file);
            } else {
                uri = Uri.fromFile(file);
            }
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse("");
    }

}

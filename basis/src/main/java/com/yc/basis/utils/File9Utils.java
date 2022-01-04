package com.yc.basis.utils;

import android.os.Environment;

import com.yc.basis.R;
import com.yc.basis.http.BaseDownloadCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

//小于android10的文件操作
public class File9Utils {

    //创建文件夹
    public static boolean createFolder(String name) {
        if (DataUtils.isEmpty(name) || !isCdCard()) {
//            throw new RuntimeException(MyApplication.getToast(R.string.throw_1));
            Toaster.toast(R.string.toast_2);
            return false;
        }
        String filePath = Environment.getExternalStorageDirectory() + File.separator + name;
        File file = new File(filePath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    //获取根目录
    public static String getBasePath(String name) {
        if (DataUtils.isEmpty(name))
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        return Environment.getExternalStorageDirectory() + File.separator + name;
    }

    public static String getBasePath() {
        return getBasePath("");
    }

    //获取指定文件夹下面的 文件
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

    public static String getFilePath(String file) {
        return getFilePath("", file);
    }

    public static void saveFile(String folder, String file, InputStream is, long total, BaseDownloadCallBack listener) {
        if (!DataUtils.isEmpty(folder))
            createFolder(folder);
        if (DataUtils.isEmpty(file)) {
            Toaster.toast(R.string.toast_3);
            if (listener != null) listener.failed();
            return;
        }
        File newFile = new File(getFilePath(folder, file));
        new Thread() {
            @Override
            public void run() {
                try {
                    if (listener != null)
                        listener.successBack(new FileOutputStream(newFile), is, total, newFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) listener.failed();
                }
            }
        }.start();
    }

    public static void saveFile(String file, InputStream is, BaseDownloadCallBack listener) {
        saveFile("", file, is, 0, listener);
    }

    public static void saveFile(String file, InputStream is, long total, BaseDownloadCallBack listener) {
        saveFile("", file, is, total, listener);
    }


    private static boolean isCdCard() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        Toaster.toast(R.string.toast_1);
        return false;
    }

    //Android10以下保存文件  到下载目录
    public static void saveFileDownload(String fileName, long total, InputStream is,
                                        final BaseDownloadCallBack listener) {
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        // 储存下载文件的目录
        File file = isExistDir(savePath, fileName);
        if (file == null) {
            Toaster.toast("下载文件失败,保存路径不存在");
            if (listener != null) listener.failed();
            return;
        }
        try {
            if (listener != null)
                listener.successBack(new FileOutputStream(file), is, total, file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) listener.failed();
        }
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    public static File isExistDir(String saveDir, String fileName) {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            try {
                downloadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        File file = new File(downloadFile.getAbsolutePath(), fileName);
        String end = fileName.substring(fileName.lastIndexOf("."));
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        int count = 1;
        while (file.exists()) {//如果文件已经存在  要修改文件名
            file = new File(downloadFile.getAbsolutePath(), name + "(" + count++ + ")" + end);
        }
        return file;
    }

}

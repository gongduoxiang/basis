package com.yc.basis.http;

import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class BaseDownloadCallBack<T extends OutputStream> {

    static Handler handler = OkhttpManager.handler;

    public void successBack(T fos, InputStream is, long total, final String path) {
        try {
            byte[] buf = new byte[2048];
            int len = 0;
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                if (total > 0) {
                    sum += len;
                    final int progress = (int) (sum * 1.0f / total * 100);
                    // 下载中  更新进度条
                    handler.post(() -> progress(progress));
                }
            }
            handler.post(() -> success(path));
        } catch (IOException e) {
            try {
                new File(path).delete();
            } catch (Exception e1) {
            }
            e.printStackTrace();
            failed();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }


    /**
     * 下载成功
     */
    public abstract void success(String path);

    /**
     * @param progress 下载进度
     */
    public abstract void progress(int progress);

    /**
     * 下载失败
     */
    public abstract void failed();

}

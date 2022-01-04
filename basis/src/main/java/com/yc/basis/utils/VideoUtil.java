package com.yc.basis.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.widget.ImageView;

public class VideoUtil {

    public static void setVideoPhoto(final ImageView iv, final String url) {
        if (url.indexOf("http") == 0) {//网络图片
            getHttpVideoBitmap(url, bitmap -> iv.post(() -> GlideUtil.filletPhoto(bitmap, iv, 2)));
        } else {
            getLocalVideoBitmap(url, bitmap -> iv.post(() -> {
                GlideUtil.filletPhoto(bitmap, iv, 2);
            }));
        }
    }


    /**
     * 获取网络视频第一帧
     */
    public static void getHttpVideoBitmap(final String videoUrl, final Listener listener) {
        new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    //根据url获取缩略图
                    retriever.setDataSource(videoUrl);
                    //获得第一帧图片
//                    bitmap = retriever.getFrameAtTime();
                    bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    retriever.release();
                }
                if (listener != null) listener.ok(bitmap);
            }
        }.start();
    }


    /**
     * 获取本地视频的第一帧
     *
     * @param localPath
     * @return
     */
    public static void getLocalVideoBitmap(String localPath, Listener listener) {
        new Thread() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    //根据文件路径获取缩略图
                    retriever.setDataSource(localPath);
                    //获得第一帧图片
//                    listener.ok(retriever.getFrameAtTime());
                    listener.ok(retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        retriever.release();
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
    }

    public interface Listener {
        void ok(Bitmap bitmap);
    }

}

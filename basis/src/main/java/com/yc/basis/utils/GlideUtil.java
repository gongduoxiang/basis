package com.yc.basis.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yc.basis.R;
import com.yc.basis.entrance.MyApplication;

public class GlideUtil {
    //.placeholder(R.mipmap.zwt)
    static RequestOptions options = RequestOptions.errorOf(R.drawable.error_img)
            .placeholder(R.drawable.zwt).dontAnimate().centerCrop();

    /**
     * 用户头像
     */
    public static void userPhoto(Object imageUrl, ImageView imageView) {
        RequestOptions options = RequestOptions.errorOf(R.drawable.user_default).dontAnimate().circleCrop();
        Glide.with(imageView.getContext()).load(imageUrl).apply(options)
                .into(imageView);
    }

    /**
     * 加载普通图片
     */
    public static void loadImage(Object url, ImageView iv) {
        Glide.with(iv.getContext()).load(url).thumbnail(0.5f).apply(options).into(iv);
    }

    /**
     * 加载普通图片
     */
    public static void loadImageGif(Object url, ImageView iv) {
        RequestOptions options = RequestOptions.errorOf(R.drawable.error_img).placeholder(R.drawable.zwt)
                .diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop();
        Glide.with(iv.getContext()).load(url).thumbnail(0.5f).apply(options).into(iv);
    }

    /**
     * 加载普通图片
     */
    public static void loadImage(Object url, ImageView iv, int errid) {
        Glide.with(iv.getContext()).load(url).thumbnail(0.5f).apply(options.clone().error(errid)).into(iv);
    }


    /**
     * 设置图片圆角角度
     */
    public static void filletPhoto(Object imageUrl, ImageView imageView, int number) {
        RequestOptions o = RequestOptions.bitmapTransform(new CenterCropRoundCornerTransform(DeviceUtils.dip2px(number)))
                .placeholder(R.drawable.zwt)
                .error(R.drawable.error_img).dontAnimate();
        Glide.with(imageView.getContext()).load(imageUrl).thumbnail(0.5f)
                .apply(o).into(imageView);
    }

    /**
     * 设置图片圆角角度
     */
    public static void filletPhoto(Object imageUrl, int errId, ImageView imageView, int number) {
        RequestOptions o = RequestOptions.bitmapTransform(new CenterCropRoundCornerTransform(DeviceUtils.dip2px(number))).placeholder(R.drawable.zwt)
                .error(errId).dontAnimate();
        Glide.with(imageView.getContext()).load(imageUrl).thumbnail(0.5f)
                .apply(o).into(imageView);
    }

    /**
     * 加载部分圆角
     */
    public static void filletPhoto(Object imageUrl, ImageView imageView, int number,
                                   boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        CornerTransform transformation = new CornerTransform(imageView.getContext(), DeviceUtils.dip2px(number));
        //设置为false的是需要设置圆角的
        transformation.setExceptCorner(leftTop, rightTop, leftBottom, rightBottom);
        RequestOptions o = RequestOptions.bitmapTransform(transformation).placeholder(R.drawable.zwt)
                .error(R.drawable.error_img).dontAnimate();
        Glide.with(imageView.getContext()).load(imageUrl).thumbnail(0.5f)
                .apply(o).into(imageView);
    }


    /**
     * 加载圆形图片
     */
    public static void circlePhoto(Object imageUrl, int errId, ImageView imageView) {
        RequestOptions options = RequestOptions.errorOf(errId).
                diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().circleCrop();
        Glide.with(imageView.getContext()).load(imageUrl).thumbnail(0.5f).apply(options)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     */
    public static void circlePhoto(Object imageUrl, ImageView imageView) {
        Glide.with(imageView.getContext()).load(imageUrl).thumbnail(0.5f).apply(options.circleCrop())
                .into(imageView);
    }

    /**
     * 返回bitmap   要开子线程
     */
    public static Bitmap getBitmap(String url) {
        try {
            return Glide.with(MyApplication.context)
                    .asBitmap()
                    .load(url)
                    .submit().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getBitmap(final String url, final Listener listener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (listener != null) listener.ok(Glide.with(MyApplication.context)
                            .asBitmap()
                            .load(url)
                            .submit(100, 100).get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public interface Listener {
        void ok(Bitmap bitmap);
    }

}

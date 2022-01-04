package com.yc.basis.utils;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.yc.basis.entrance.MyApplication;

public class DrawableUtil {

    public static Drawable getDrawable(int id) {
        Drawable d = MyApplication.context.getResources().getDrawable(id);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        return d;
    }


    public static void drawableLeft(TextView tv, Drawable drawable) {
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    public static void drawableLeft(TextView tv, int id) {
        tv.setCompoundDrawables(getDrawable(id), null, null, null);
    }

    public static void drawableTop(TextView tv, Drawable drawable) {
        tv.setCompoundDrawables(null, drawable, null, null);
    }

    public static void drawableTop(TextView tv, int id) {
        tv.setCompoundDrawables(null, getDrawable(id), null, null);
    }

    public static void drawableRight(TextView tv, Drawable drawable) {
        tv.setCompoundDrawables(null, null, drawable, null);
    }

    public static void drawableRight(TextView tv, int id) {
        tv.setCompoundDrawables(null, null, getDrawable(id), null);
    }

    public static void drawableBottom(TextView tv, Drawable drawable) {
        tv.setCompoundDrawables(null, null, null, drawable);
    }

    public static void drawableBottom(TextView tv, int id) {
        tv.setCompoundDrawables(null, null, null, getDrawable(id));
    }

}

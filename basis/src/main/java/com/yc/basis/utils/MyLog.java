package com.yc.basis.utils;

import android.util.Log;

public class MyLog {


    public static final int verbose_ = 1;
    public static final int debug_ = 2;
    public static final int info_ = 3;
    public static final int warn_ = 4;
    public static final int error_ = 5;
    public static final int NOTHING = 6;
    public static int LEVEL = verbose_;

    public static String TAG = "mLog";

    public static void close(){
        LEVEL=NOTHING;
    }

    public static void setLevel(int level) {
        LEVEL = level;
    }


    public static void v(String msg) {
        if (LEVEL <= verbose_) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (LEVEL <= verbose_) {
            Log.v(tag, msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL <= debug_) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= debug_) {
            Log.d(tag, msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL <= info_) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= info_) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg) {
        if (LEVEL <= warn_) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL <= warn_) {
            Log.w(tag, msg);
        }
    }

    public static void e(String msg) {
        if (LEVEL <= error_) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= error_) {
            Log.e(tag, msg);
        }
    }

}

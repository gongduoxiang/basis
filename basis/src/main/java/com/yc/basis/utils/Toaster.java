package com.yc.basis.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.basis.R;
import com.yc.basis.entrance.MyApplication;


public class Toaster {
    private static Toast sToast;
    private static int sMessageTextId;
    public static Context mContext;
    static Handler mainHandler = new Handler(Looper.getMainLooper());


    public static void init(Context context, int layoutId, int textId) {
        mContext = context;
        sToast = new Toast(context);
        try {
            sToast.setView(LinearLayout.inflate(context, layoutId, null));
        } catch (Exception e) {
        }
        sMessageTextId = textId;
    }

    public static void toast(final String title) {
        if (sToast != null && !TextUtils.isEmpty(title)) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (title.length() > 10) {
                        sToast.setDuration(Toast.LENGTH_LONG);
                    } else {
                        sToast.setDuration(Toast.LENGTH_SHORT);
                    }
                    try {
                        //已在主线程中，可以更新UI
                        ((TextView) sToast.getView().findViewById(sMessageTextId)).setText(title);
                        sToast.setGravity(Gravity.BOTTOM, 0, 250);
                        sToast.show();
                    } catch (Exception e) {
                    }
                }
            });
        } else {
            init(MyApplication.context, R.layout.toaster, android.R.id.message);
        }
    }

    public static void toast(final int id) {
        if (sToast != null && id != -1) {
            mainHandler.post(() -> {
                try {
                    String title = MyApplication.context.getString(id);
                    if (title.length() > 10) {
                        sToast.setDuration(Toast.LENGTH_LONG);
                    } else {
                        sToast.setDuration(Toast.LENGTH_SHORT);
                    }
                    //已在主线程中，可以更新UI
                    ((TextView) sToast.getView().findViewById(sMessageTextId)).setText(title);
                    sToast.setGravity(Gravity.BOTTOM, 0, 250);
                    sToast.show();
                } catch (Exception e) {
                }
            });
        } else {
            init(MyApplication.context, R.layout.toaster, android.R.id.message);
        }
    }

    public static void String(final int id) {
        if (sToast != null && id != -1) {
            mainHandler.post(() -> {
                try {
                    String title = MyApplication.context.getString(id);
                    if (title.length() > 10) {
                        sToast.setDuration(Toast.LENGTH_LONG);
                    } else {
                        sToast.setDuration(Toast.LENGTH_SHORT);
                    }
                    //已在主线程中，可以更新UI
                    ((TextView) sToast.getView().findViewById(sMessageTextId)).setText(title);
                    sToast.setGravity(Gravity.BOTTOM, 0, 250);
                    sToast.show();
                } catch (Exception e) {
                }
            });
        } else {
            init(MyApplication.context, R.layout.toaster, android.R.id.message);
        }
    }


    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkConnected() {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static void showHttp(String msg, boolean b) {
        if (!b) return;
        toast(msg);
    }

    public static void showHttp(int code, boolean b) {
        if (!b) return;
        switch (code) {
            case 0://无网络
                toast(MyApplication.getToast(R.string.toast_base_1));
                break;
            case 1://参数有问题
                toast(MyApplication.getToast(R.string.toast_base_2));
                break;
            case 2://都没有连接上
                toast(MyApplication.getToast(R.string.toast_base_3));
                break;
            case 3://这次请求返回 连接失败
                toast(MyApplication.getToast(R.string.toast_base_4));
                break;
            case 4://数据有误，解析失败
                toast(MyApplication.getToast(R.string.toast_base_5));
                break;
        }
    }

}

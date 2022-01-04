package com.yc.basis.entrance;

import android.app.Application;

import com.yc.basis.BuildConfig;
import com.yc.basis.R;
import com.yc.basis.http.response.HttpCommon;
import com.yc.basis.utils.MyLog;
import com.yc.basis.utils.Toaster;

public class MyApplication {
    public static Application context;

    public static void init(Application context1) {
        if (!BuildConfig.DEBUG)
            MyLog.close();
        context = context1;
        initData();
        HttpCommon.getAgreement(null, false);
    }

    private static void initData() {
        Toaster.init(context, R.layout.toaster, android.R.id.message);
    }

    public static String getToast(int id) {
        return context.getBaseContext().getResources().getString(id);
    }

}

package com.yc.basis.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.TextView;

import com.yc.basis.R;
import com.yc.basis.dialog.KeFuDialog;
import com.yc.basis.entity.LoginEventEntity;
import com.yc.basis.entrance.MyApplication;
import com.yc.basis.http.response.HttpCommon;
import com.yc.basis.ui.PhotoLookActivity;
import com.yc.basis.ui.WebActivity;
import com.yc.basis.utils.DeviceUtils;
import com.yc.basis.utils.SPUtils;
import com.yc.basis.utils.Toaster;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class BasisInfo {
    public static String xyUrl = "";
    public static String zcUrl = "";
    public static String about_us = "";
    public static String wxId = "wx4b16ffe72e22b3cc";
    public static String qq = "1536192681";

    public static String getFileprovider() {
        return MyApplication.context.getApplicationInfo().processName + ".fileprovider";
    }

    public static String getProcessName() {
        return MyApplication.context.getApplicationInfo().processName;
    }

    //用户协议
    public static void startXY() {
        HttpCommon.getAgreement(new BaseHttpListener() {
            @Override
            public void success(Object o) {
                Intent intent = new Intent(MyApplication.context, WebActivity.class);
                intent.putExtra("url", xyUrl);//file:///android_asset/index.html
                intent.putExtra("name", "用户协议");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.context.startActivity(intent);
            }

            @Override
            public void error(String msg) {
                Toaster.toast(msg);
            }
        }, true);
    }

    //隐私政策
    public static void startZC() {
        HttpCommon.getAgreement(new BaseHttpListener() {
            @Override
            public void success(Object o) {
                Intent intent = new Intent(MyApplication.context, WebActivity.class);
                intent.putExtra("url", zcUrl);
                intent.putExtra("name", "隐私政策");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.context.startActivity(intent);
            }

            @Override
            public void error(String msg) {
                Toaster.toast(msg);
            }
        }, true);
    }

    public static void startGY() {
        HttpCommon.getAgreement(new BaseHttpListener() {
            @Override
            public void success(Object o) {
                Intent intent = new Intent(MyApplication.context, WebActivity.class);
                intent.putExtra("url", about_us);
                intent.putExtra("name", "关于");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.context.startActivity(intent);
            }

            @Override
            public void error(String msg) {
                Toaster.toast(msg);
            }
        }, true);
    }

    public static void startKeFu(Activity activity) {
        KeFuDialog keFuDialog = new KeFuDialog(activity);
        keFuDialog.myShow();
    }

    public static void startGY(Activity activity) {
        TextView textView = new TextView(activity);
        textView.setText(activity.getString(R.string.app_name) + "版本：" + DeviceUtils.getVersionName());
        int pad = Math.round(DeviceUtils.dip2px(10));
        textView.setPadding(pad, pad, pad, pad);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("关于")
                .setView(textView);
        builder.show();
    }

    public static void startWebActivity(String url, String name) {
        Intent intent = new Intent(MyApplication.context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("name", name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.context.startActivity(intent);
    }


    //去查看图片
    public static void startPhotoLook(ArrayList<String> photos, int position) {
        Intent intent = new Intent(MyApplication.context, PhotoLookActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("photo", photos);
        intent.putExtra("position", position);
        MyApplication.context.startActivity(intent);
    }

    //去查看图片
    public static void startPhotoLook(String photos) {
        Intent intent = new Intent(MyApplication.context, PhotoLookActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("photo", photos);
        MyApplication.context.startActivity(intent);
    }


    public static void startLoginActivity() {
        SPUtils.saveToken("");
        SPUtils.saveUserId("");
        EventBus.getDefault().post(new LoginEventEntity(LoginEventEntity.login));
    }

    /**
     * Settings.ACTION_SECURITY_SETTINGS  系统设置
     * Settings.ACTION_LOCATION_SOURCE_SETTINGS  定位
     */
    public static void startSystem(String str) {
        Intent intent = new Intent(str);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.context.startActivity(intent);
    }

    public static void setWxId(String id) {
        wxId = id;
    }

    public static String getWxId() {
        return wxId;
    }


}

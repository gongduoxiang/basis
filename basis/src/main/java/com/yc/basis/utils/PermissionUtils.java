package com.yc.basis.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/9/25.
 */

public class PermissionUtils {
    public static final int code = 11010;
    public static final int STORAGE = 11011;

    /*
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
    Manifest.permission.CAMERA

     if (requestCode == PermissionUtils.code
                && PermissionUtils.verificationPermission(this, permissions, grantResults)) {
    */
    //Manifest.permission.WRITE_EXTERNAL_STORAGE
    //如果已经有这个权限就返回true   否则去申请并返回false
    public static boolean isPermission(Activity activity, String[] permission, final int code) {
        if (permission == null) return false;
        //保存需要申请的权限个数
        List list = new ArrayList<String>();
        //使用兼容库就无需判断系统版本
        // PermissionChecker.checkCallingOrSelfPermission(activity, permission)
        //ContextCompat.checkSelfPermission(activity, permission);

        for (int i = 0; i < permission.length; i++) {
            int hasWriteStoragePermission = PermissionChecker.checkCallingOrSelfPermission(activity, permission[i]);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                list.add(permission[i]);
            }
        }
        if (list.size() == 0) {//说明所有的权限都已经申请好了
            return true;

        } else {
            //没有权限，向用户请求权限
//            String[] strs1=new String[list.size()];
            ActivityCompat.requestPermissions(activity, (String[]) list.toArray(new String[list.size()]), code);
            return false;
        }
    }

    //比较权限申请了哪些
    public static boolean verificationPermission(Activity activity, String[] permissions, int[] grantResults) {
        int count = 0;//通过的权限

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                count++;
            }
        }
        if (count == permissions.length) {//全部通过
            return true;
        } else {
            boolean b = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(
                            activity, permissions[i]);
                    if (!showRequestPermission) {//只要有false的就是勾选了  不再提示按钮
                        Toaster.toast("因为用户您勾选了不再提示按钮，如需使用此功能，请前期手机-设置-权限设置开启改权限!");
                        b = false;
                    }
                }
            }
            if (b) {
                //用户不同意
                notAgainPermission();
            }
            return false;
        }
    }

    //如果用户拒绝了权限  并且不再提示
    public static void notAgainPermission() {
        Toaster.toast("用户必须授权，才能正常使用此功能");
    }

    //是否有读写权限
    public static boolean isWriteOrRead(Activity activity) {
        if (PermissionUtils.isPermission(activity, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, STORAGE)) {
            return true;
        }
        return false;
    }

}

package com.yc.basis.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.yc.basis.entrance.MyApplication;
import com.yc.basis.utils.FileUtilOld;
import com.yc.basis.utils.Toaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//系统分享功能
public class SystemShareUtils {

    public static final String PACKAGE_WECHAT = "com.tencent.mm";
    public static final String PACKAGE_MOBILE_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_QZONE = "com.qzone";
    public static final String PACKAGE_SINA = "com.sina.weibo";
    public static final Context context = MyApplication.context;

    // 判断是否安装指定app
    public static boolean isInstallApp(Context context, String app_package) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String pn = pInfo.get(i).packageName;
                if (app_package.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int count = 1;

    //输入qq号直接打开qq
    public static void startQQChat(String str) {
        try {
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(str);
            m.find();
            String qqNumber = m.group();
            if (isInstallApp(context, PACKAGE_MOBILE_QQ)) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNumber + "&version=" + count));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                count++;
            } else {
                Toaster.toast("本机未安装QQ应用");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toaster.toast("打开QQ失败");
        }
    }

    public static void shareFile(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(getType(url) + "/*");
//            intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, FileUtilOld.fileToUri(url));
        intent = Intent.createChooser(intent, "Here is the title of Select box");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private static Map<String, String> type = new HashMap<>();

    static {
        type.put(".png", "image");
        type.put(".jpg", "image");
        type.put(".jpeg", "image");
        type.put(".mp4", "video");
    }

    public static String getType(String url) {
        Set<String> keys = type.keySet();
        for (String key : keys) {
            if (url.endsWith(key)) {
                return type.get(key);
            }
        }
        return "*";
    }

}

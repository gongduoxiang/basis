package com.yc.basis.utils;

import android.app.Activity;
import android.webkit.JavascriptInterface;

//webview和h5交互
public class WebJsUtils {

    private Activity activity;
    public String thml;

    public WebJsUtils(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void close() {
        activity.finish();
    }

    @JavascriptInterface
    public String getVersion() {
        return DeviceUtils.getVersionName2();
    }


    @JavascriptInterface
    public void getSource(String str) {
        thml = str;
    }

}

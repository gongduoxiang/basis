package com.yc.basis.http;


import android.os.Handler;

import com.yc.basis.R;
import com.yc.basis.entrance.MyApplication;
import com.yc.basis.utils.DataUtils;
import com.yc.basis.utils.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 直接返回json
 * 给项目里面不是统一返回字段的处理
 */
public abstract class BaseCallbackString {
    static Handler handler = OkhttpManager.handler;

    public void successBack(final String result, final boolean b) {
        handler.post(() -> {
            try {
                JSONObject object = new JSONObject(result);
                int status = DataUtils.getInt(object, "code");
//                try {
////                    HttpFormBody.setTime(DataUtils.getLong(object, "time"));
////                } catch (Exception e) {
////                }
                if (HttpCode.ok == status) {//成功
                    success(DataUtils.getString(object, "data"));
                    return;
                } else if (HttpCode.isLogin(status)) {
                    BasisInfo.startLoginActivity();
                }
//                Toaster.showHttp(DataUtils.getString(object, "msg"), b);
                failure("", DataUtils.getString(object, "message"));
            } catch (Exception e) {
                e.printStackTrace();
                toastDefault();
            }
        });
    }

    public void failureBack(final String code, final String msg, boolean b) {
        Toaster.showHttp(msg, b);
        handler.post(() -> failure(code, msg));
    }

    public abstract void success(String result) throws JSONException;

    public abstract void failure(String code, String msg);

    public void toastDefault() {
        failure("", MyApplication.getToast(R.string.toast_data));
    }
}

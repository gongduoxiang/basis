package com.yc.basis.http;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;

import com.yc.basis.R;
import com.yc.basis.entrance.MyApplication;
import com.yc.basis.utils.File10Util;
import com.yc.basis.utils.MyLog;
import com.yc.basis.utils.SPUtils;
import com.yc.basis.utils.Toaster;
import com.yc.basis.utils.channel.WalleChannelReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpManager {
    /////////////////////////// 定义成员变量 ////////////////////
    private OkHttpClient okHttpClient;
    public static Handler handler = new Handler(Looper.getMainLooper());
    private volatile static OkhttpManager manager;

    //////////////////////////使用构造方式,完成初始化///////////////////////////
    private OkhttpManager() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }

    //////////////////////////使用单例模式,通过获取的方式拿到对象//////////////////////////////
    public static OkhttpManager getInstance() {
        if (manager == null) {
            synchronized (OkhttpManager.class) {
                if (manager == null) {
                    manager = new OkhttpManager();
                }
            }
        }
        return manager;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }


    /**
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 设置参数的
     */
    private Request getRequest(final String url, FormBody fb, Activity activity) {
        Request request = null;
        Request.Builder builder = new Request.Builder();
        String str = "";
        str += MyApplication.context.getString(R.string.myAppId);
        str += "-" + WalleChannelReader.getChannel(MyApplication.context);
        str += "-" + MyApplication.context.getString(R.string.appsecret);
        if (!TextUtils.isEmpty(SPUtils.getToken())) {
            str += "-" + SPUtils.getUserId();
            str += "-" + SPUtils.getToken();
        }
        builder.addHeader("authentication", "user " +
                Base64.encodeToString(str.getBytes(), Base64.DEFAULT).replaceAll("\n", ""));
        if (activity == null) {
            request = builder.url(url).post(fb).build();
        } else {
            request = builder.url(url).post(fb).tag(activity).build();
        }
        return request;
    }

    /**
     * -------------------   这里是普通的只返回json数据的     --------------------------
     */
    public void post(String url, FormBody fb, BaseCallbackString callBack) {
        post(url, fb, null, true, callBack);
    }

    public void post(String url, FormBody fb, boolean b, BaseCallbackString callBack) {
        post(url, fb, null, b, callBack);
    }

    /**
     * 请求指定的URL返回的结果是JSon字符串
     */
    public void post(final String url, FormBody fb, Activity activity, final boolean b, final BaseCallbackString callBack) {
        if (!Toaster.isNetworkConnected()) {
            Toaster.showHttp(0, b);
            if (callBack != null) callBack.failureBack("3", "", b);
            return;
        }
        Request request = null;
        try {
            request = getRequest(url, fb, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (request == null) {
            Toaster.showHttp(1, true);
            if (callBack != null) callBack.failureBack("2", "", b);
            return;
        }
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.d("超时url-------   " + url);
                e.printStackTrace();
                if (callBack != null)
                    callBack.failureBack("2", MyApplication.getToast(R.string.toast_base_3), b);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String str = response.body().string();
                MyLog.v("url-------   " + url + "   " + str);
                try {
                    JSONObject object = new JSONObject(str);
                    if (callBack != null)
                        callBack.successBack(str, b);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (callBack != null) callBack.failureBack("2", "服务器繁忙", b);
                }
            }
        });
    }


    /**
     * -------------------    请求指定的URL返回的结果是图片    --------------------------
     */
    public interface Func3 {
        void onResponse(InputStream is);

        void onErr(Call call, IOException e);
    }

    public void downLoadImage(String url, final Func3 callBack) {
        if (!Toaster.isNetworkConnected()) return;
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toaster.toast("网络异常,请检查网络设置");
                    }
                });
                callBack.onErr(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response != null && response.isSuccessful()) {
                    if (callBack != null) {
                        try {
                            callBack.onResponse(response.body().byteStream());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    handler.post(() -> Toaster.toast("网络异常,请检查网络设置"));
                }
            }
        });
    }

    public interface Func4 {
        void onResponse(InputStream inputStream);

        void onErr(String code);
    }

    public void downFile(String url, final BaseDownloadCallBack listener) {
        if (!Toaster.isNetworkConnected()) {
            Toaster.toast("网络异常,请检查网络设置");
            listener.failed();
            return;
        }
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toaster.toast("网络异常,请检查网络设置");
                listener.failed();
            }

            @Override
            public void onResponse(Call call, Response response) {
                String name = getNameFromUrl(url);
                if (!name.endsWith(".svg")) {
                    name += ".svg";
                }
//                try {
//                    String path = FileUtilOld.getSavePath(name);
//                    File file = new File(path);
//                    if (file.exists()) {
//                        file.delete();
//                    }
//                } catch (Exception e) {
//                }
                File10Util.saveFile("svg", name, response.body().byteStream(),  listener);
            }
        });
    }


    /*
     * 得到图片字节流 数组大小
     * */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}

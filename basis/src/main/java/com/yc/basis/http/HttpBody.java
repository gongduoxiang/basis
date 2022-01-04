package com.yc.basis.http;

import com.yc.basis.utils.DataUtils;

import okhttp3.FormBody;

public class HttpBody {
    public static long time = 0;//保存当前手机和服务器的时间差

    public static void setTime(long l) {
        time = l - System.currentTimeMillis() / 1000;
    }

    private FormBody.Builder builder;

    public HttpBody() {
        builder = new FormBody.Builder();
    }

    public void add(String name, String value) {
        if (DataUtils.isEmpty(value)) {
            builder.add(name.trim(), "");
        } else {
            builder.add(name.trim(), value);
        }
    }

    public void add(String name, int value) {
        builder.add(name.trim(), value + "");
    }

    public void add(String name, boolean value) {
        builder.add(name.trim(), value + "");
    }

    public void add(String name, double value) {
        builder.add(name.trim(), value + "");
    }

    public void add(String name, float value) {
        builder.add(name.trim(), value + "");
    }

    public void add(String name, long value) {
        builder.add(name.trim(), value + "");
    }

    public FormBody build() {
        return builder.build();
    }
}

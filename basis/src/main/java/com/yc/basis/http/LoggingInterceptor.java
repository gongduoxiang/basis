package com.yc.basis.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor {

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        return chain.proceed(builder.build());
    }
}

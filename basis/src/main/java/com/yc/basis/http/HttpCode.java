package com.yc.basis.http;

public class HttpCode {

    public static final int ok = 200;//正常
    public static final int login1 = 401;//都是需要重新登录的


    //是否需要重新登录
    public static boolean isLogin(int status) {
        if (HttpCode.login1 == status) {
            return true;
        }
        return false;
    }


}

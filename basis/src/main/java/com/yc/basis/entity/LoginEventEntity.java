package com.yc.basis.entity;

public class LoginEventEntity {

    public static final String ok = "1";//登录成功
    public static final String error = "0";//登录失败
    public static final String login = "10";//登录

    public String flag;

    public LoginEventEntity(String flag) {
        this.flag = flag;
    }

}

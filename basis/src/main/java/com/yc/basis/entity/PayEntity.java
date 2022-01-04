package com.yc.basis.entity;

public class PayEntity {

    public static final String ok = "支付成功";
    public static final String error = "支付失败";

    public String flag;

    public PayEntity(String s) {
        flag = s;
    }


}

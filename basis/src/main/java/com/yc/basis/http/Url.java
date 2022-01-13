package com.yc.basis.http;

public class Url {

    protected static final String ip2 = "http://app.yunchuan.info/api/v1/";
    public static final String pay = ip2 + "pay";//支付
    public static final String payConfig = ip2 + "get-pay-config";//vip列表
    public static final String payType = ip2 + "get-pay-type";//支付方式
    public static final String weChatLogin = ip2 + "wx-login";

    public static final String userInfo = ip2 + "user-info";
    public static final String getCustomerService = ip2 + "get-customer-service";
    public static final String updateApp = ip2 + "get-version";
    public static final String deduction = ip2 + "deduction";
    public static final String getAgreement = ip2 + "get-agreement";

    public static final String sms = "http://app.yunchuan.info/api/v1/sms";   // 获取手机验证码 参数：mobile
    public static final String mobileLogin = "http://app.yunchuan.info/api/v1/mobile-login";   // 验证码登录 参数：mobile  code


    public static final String upload = "";

}

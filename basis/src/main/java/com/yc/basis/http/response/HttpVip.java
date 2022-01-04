package com.yc.basis.http.response;

import com.yc.basis.entity.PayType;
import com.yc.basis.entity.VipEntity;
import com.yc.basis.entity.WeChatPayEntity;
import com.yc.basis.http.BaseCallbackString;
import com.yc.basis.http.BaseHttpListener;
import com.yc.basis.http.HttpBody;
import com.yc.basis.http.OkhttpManager;
import com.yc.basis.http.Url;
import com.yc.basis.utils.DataUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HttpVip {

    public interface PayTypeListener {
        void weChat(WeChatPayEntity entity);

        void aliPay(String info);

        void error();
    }

    //默认wechat 微信支付 alipay支付宝支付
    public static void pay(String money, String type, String id, PayTypeListener listener) {
        HttpBody formBody = new HttpBody();
        formBody.add("amount", money);
        formBody.add("pay_type", type);
        formBody.add("vip", id);
        OkhttpManager.getInstance().post(Url.pay, formBody.build(), new BaseCallbackString() {
            @Override
            public void success(String result) throws JSONException {
                JSONObject object = new JSONObject(result);
                if (type.equals("alipay")) {//支付宝
//                    ZFBPay.startAlipay(activity, DataUtils.getString(object, "info"));
                    if (listener != null) listener.aliPay(DataUtils.getString(object, "info"));
                    return;
                }
                WeChatPayEntity chatPayEntity = new WeChatPayEntity();
                chatPayEntity.appId = DataUtils.getString(object, "appid");
                chatPayEntity.partnerId = DataUtils.getString(object, "partnerid");
                chatPayEntity.prepayId = DataUtils.getString(object, "prepayid");
                chatPayEntity.timeStamp = DataUtils.getString(object, "timestamp");
                chatPayEntity.nonceStr = DataUtils.getString(object, "noncestr");
                chatPayEntity.packageValue = DataUtils.getString(object, "package");
                chatPayEntity.sign = DataUtils.getString(object, "sign");
//                    WeChatPay.pay(MyApplication.context, chatPayEntity);
                if (listener != null) listener.weChat(chatPayEntity);
            }

            @Override
            public void failure(String code, String msg) {
                if (listener != null) listener.error();
            }
        });
    }

    //获取支付配置
    public static void payConfig(BaseHttpListener listener) {
        OkhttpManager.getInstance().post(Url.payConfig, new HttpBody().build(), new BaseCallbackString() {
            @Override
            public void success(String result) throws JSONException {
                JSONObject object = new JSONObject(result);
                JSONArray info = object.getJSONArray("info");
                List<VipEntity> vipEntities = new ArrayList<>();
                for (int i = 0; i < info.length(); i++) {
                    JSONObject ob = info.getJSONObject(i);
                    VipEntity vipEntity = new VipEntity();
                    vipEntity.name = DataUtils.getString(ob, "name");
                    vipEntity.wxMoney = DataUtils.getString(ob, "wechat_amount");
                    vipEntity.zfbMoney = DataUtils.getString(ob, "alipay_amount");
                    vipEntity.id = DataUtils.getString(ob, "id");
                    vipEntities.add(vipEntity);
                }
                if (listener != null) listener.success(vipEntities);
            }

            @Override
            public void failure(String code, String msg) {
                if (listener != null) listener.error(msg);
            }
        });
    }

    //获取支付方式
    public static void payType(BaseHttpListener listener) {
        OkhttpManager.getInstance().post(Url.payType, new HttpBody().build(), new BaseCallbackString() {
            @Override
            public void success(String result) throws JSONException {
                PayType payType = new PayType();
                payType.isWxPay = result.contains("wechat");
                payType.isZfbPay = result.contains("alipay");
                if (listener != null) listener.success(payType);
            }

            @Override
            public void failure(String code, String msg) {
                if (listener != null) listener.error(msg);
            }
        });
    }

}

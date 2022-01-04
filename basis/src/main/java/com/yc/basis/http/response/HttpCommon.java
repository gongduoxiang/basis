package com.yc.basis.http.response;


import com.yc.basis.entity.User;
import com.yc.basis.http.BaseCallbackString;
import com.yc.basis.http.BaseHttpListener;
import com.yc.basis.http.BasisInfo;
import com.yc.basis.http.HttpBody;
import com.yc.basis.http.OkhttpManager;
import com.yc.basis.http.Url;
import com.yc.basis.utils.DataUtils;
import com.yc.basis.utils.SPUtils;
import com.yc.basis.utils.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

//应用通用的接口
public class HttpCommon {

    //协议
    public static void getAgreement(BaseHttpListener listener, boolean b) {
        if (DataUtils.isEmpty(BasisInfo.xyUrl))
            OkhttpManager.getInstance().post(Url.getAgreement, new HttpBody().build(), b, new BaseCallbackString() {
                @Override
                public void success(String result) throws JSONException {
                    JSONObject object = new JSONObject(result);
                    BasisInfo.xyUrl = DataUtils.getString(object, "user_agreement");
                    BasisInfo.zcUrl = DataUtils.getString(object, "privacy_policy");
                    BasisInfo.about_us = DataUtils.getString(object, "about_us");
                    if (listener != null) listener.success("");
                }

                @Override
                public void failure(String code, String msg) {
                    if (listener != null) listener.error(msg);
                }
            });
        else if (listener != null) listener.success("");
    }

    //获取客服信息
    public static void getCustomerService(BaseHttpListener listener) {
        OkhttpManager.getInstance().post(Url.getCustomerService, new HttpBody().build(), new BaseCallbackString() {
            @Override
            public void success(String result) throws JSONException {
                JSONObject data = new JSONObject(result);
                if (listener != null) listener.success(DataUtils.getString(data, "info"));
            }

            @Override
            public void failure(String code, String msg) {
                if (listener != null) listener.error(msg);
            }
        });
    }

    /**
     * 获取用户信息
     */
    public static void getUserInfo(final BaseHttpListener listner) {
        if (!DataUtils.isEmpty(SPUtils.getToken()))
            OkhttpManager.getInstance().post(Url.userInfo,
                    new HttpBody().build(), new BaseCallbackString() {
                        @Override
                        public void success(String result) throws JSONException {
                            JSONObject ob = new JSONObject(result);
                            User user = new User();
                            user.name = DataUtils.getString(ob, "username");
                            user.photo = DataUtils.getString(ob, "avatar");
//                            user.timeText = DataUtils.timeToData(DataUtils.getLong(ob, "vip_due_time") * 1000);
                            user.id = DataUtils.getString(ob, "id");
                            user.isVip = DataUtils.getInt(ob, "is_vip") == 1;
                            if (user.isVip) {
                                user.vipId = DataUtils.getString(ob, "level_id");
                                user.timeText = DataUtils.getString(ob, "vip_due_time_text");
                                user.time = DataUtils.getLong(ob, "vip_due_time") * 1000;
                            } else {
                                user.vipId = "";
                                user.timeText = "";
                                user.time = 0;
                            }
                            SPUtils.saveUser(user);
                            if (listner != null) listner.success(user);
                        }

                        @Override
                        public void failure(String code, String msg) {
                            Toaster.toast(msg);
                            if (listner != null) listner.error(msg);
                        }
                    });
    }

}

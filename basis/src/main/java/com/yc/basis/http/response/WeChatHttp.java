package com.yc.basis.http.response;



import com.yc.basis.entity.User;
import com.yc.basis.http.BaseCallbackString;
import com.yc.basis.http.BaseHttpListener;
import com.yc.basis.http.HttpBody;
import com.yc.basis.http.OkhttpManager;
import com.yc.basis.http.Url;
import com.yc.basis.utils.DataUtils;
import com.yc.basis.utils.SPUtils;
import com.yc.basis.utils.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

public class WeChatHttp {

    /**
     * 微信登录
     */
    public static void login(String code, final BaseHttpListener listener) {
        HttpBody body = new HttpBody();
        body.add(" code", code + "");
        OkhttpManager.getInstance().post(Url.weChatLogin,
                body.build(), new BaseCallbackString() {
                    @Override
                    public void success(String result) throws JSONException {
                        JSONObject object = new JSONObject(result);
                        String token = DataUtils.getString(object, "access_token");
                        SPUtils.saveToken(token);
                        if (DataUtils.isObject(object, "client")) {
                            JSONObject ob = object.getJSONObject("client");
                            User user = new User();
                            user.name = DataUtils.getString(ob, "username");
                            user.photo = DataUtils.getString(ob, "avatar");
                            user.id = DataUtils.getString(ob, "id");
                            user.vipId = DataUtils.getString(ob, "level_id");
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
                            SPUtils.saveUserId(DataUtils.getString(ob, "id"));
                        }
                        if (listener != null) listener.success(token);
                    }

                    @Override
                    public void failure(String code, String msg) {
                        Toaster.toast(msg);
                        if (listener != null) listener.error(msg);
                    }
                });
    }
}

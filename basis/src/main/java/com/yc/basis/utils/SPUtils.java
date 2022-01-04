package com.yc.basis.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.yc.basis.entity.User;
import com.yc.basis.entrance.MyApplication;


public class SPUtils {

    public static SharedPreferences sp = MyApplication.context.getSharedPreferences("config", Context.MODE_PRIVATE);
    public static SharedPreferences.Editor editor = sp.edit();


    public static void setPrivacy(String type) {//同意   拒绝
        editor.putString("privacy", type);
        editor.commit();
    }

    public static String getPrivacy() {//返回 true同意     false拒绝      ""就是第一次
        try {
            return sp.getString("privacy", "");
        } catch (Exception e) {
            return "";
        }
    }

    public static void setFirst() {
        editor.putBoolean("first", true);
        editor.commit();
    }

    public static boolean getFirst() {
        return sp.getBoolean("first", false);
    }

    public static void saveToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public static String getToken() {
        return sp.getString("token", "");
    }


    public static void saveUserId(Object id) {
        editor.putString("userId", String.valueOf(id));
        editor.commit();
    }

    public static String getUserId() {
        return sp.getString("userId", "");
    }

    /**
     * 保存用户的信息
     */
    public static void saveUser(User user) {
        String json = new Gson().toJson(user);
        editor.putString("userInfo", json);
        editor.commit();
    }

    /**
     * 获取用户信息
     */
    public static User getUser() {
        try {
            String str = sp.getString("userInfo", "");
            if (DataUtils.isEmpty(str)) {
                return new User();
            } else {
                return new Gson().fromJson(str,
                        User.class);//把JSON字符串转为
            }
        } catch (Exception e) {
            return new User();
        }
    }

    public static void setAgree(boolean b) {
        editor.putBoolean("Agree", b);
        editor.commit();
    }

    public static boolean getAgree() {
        return sp.getBoolean("Agree", true);
    }

    //卡通脸
    public static void setKtl() {
        editor.putBoolean("ktl", true);
        editor.apply();
    }

    public static boolean getKtl() {
        return sp.getBoolean("ktl", false);
    }

    //卡通脸
    public static void setCount() {
        editor.putBoolean("count", true);
        editor.apply();
    }

    public static boolean getCount() {
        return sp.getBoolean("count", false);
    }

    //设置是否显示广告
    public static void setAd(boolean b) {
        editor.putBoolean("Ad", b);
        editor.apply();
    }

    //获取广告设置  true关闭广告
    public static boolean getAd() {
        return sp.getBoolean("Ad", false);
    }

    //保存moveview的位置
    public static void saveMoveViewXY(int x, int y) {
        editor.putInt("MoveViewX", x);
        editor.putInt("MoveViewY", y);
        editor.apply();
    }

    public static int[] getMoveViewX() {
        int[] floats = new int[2];
        floats[0] = sp.getInt("MoveViewX", -1);
        floats[1] = sp.getInt("MoveViewY", -1);
        return floats;
    }

    public static boolean isLogin() {
        if (!DataUtils.isEmpty(getToken())) {
            return true;
        }
        return false;
    }

    public static void saveNumber(String key) {
        if (getNumber(key) <= 200) {
            editor.putInt(key + "number", getNumber(key) + 1);
            editor.apply();
        }
    }

    public static int getNumber(String key) {
        return sp.getInt(key + "number", 0);
    }

    public static boolean isVip() {
        return getUser().isVip;
//        return true;
    }

    public static void loginOut() {
        saveUserId("");
        saveToken("");
    }


}

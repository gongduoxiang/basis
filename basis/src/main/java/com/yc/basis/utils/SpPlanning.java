package com.yc.basis.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yc.basis.entrance.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class SpPlanning {

    public static SharedPreferences sp = MyApplication.context.getSharedPreferences("Planning", Context.MODE_PRIVATE);
    public static SharedPreferences.Editor editor = sp.edit();

    //添加搜索历史
    public static void addSpPlanning(List<String> str) {
        Gson gson = new Gson();
        String strJson = gson.toJson(str);
        editor.putString("Plannings", strJson);
        editor.apply();
    }


    public static List<String> getSpPlanning() {
        List<String> list = new ArrayList<>();
        String strJson = sp.getString("Plannings", null);
        if (null == strJson) {
            return list;
        }
        try {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                list.add(gson.fromJson(jsonElement, String.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void cleaner() {
        editor.putString("Plannings", "");
        editor.apply();
    }

}

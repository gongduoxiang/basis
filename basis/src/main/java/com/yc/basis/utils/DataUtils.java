package com.yc.basis.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.TextView;

import com.yc.basis.entrance.MyApplication;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {


    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    //获取指定年月的天数   公历
    public static int getDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);//注意一定要写5，不要写6！Calendar.MONTH是从0到11的！
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    //yyyy-MM-dd HH:mm:ss.SSS
    public static String timeToData(Long time) {
        return timeToData(time, "yyyy-MM-dd");
    }

    public static String timeToData(Long time, String type) {
        try {
            Date date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat(type);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static long timeToData(String time, String type) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(type);
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    //根据日期取得星期几
    public static String getWeek(Date date) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.equals("") || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取人民币的符号
     */
    public static String getYuan() {
        return Html.fromHtml("&yen").toString();
    }


    public static double getDouble(JSONObject ob, String key) {
        try {
            if (ob.isNull(key) || ("" + ob.getDouble(key)).equals("null")) {
                return 0.0;
            }
            return round(ob.getDouble(key), 2, BigDecimal.ROUND_DOWN);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static int getInt(JSONObject ob, String key) {
        try {
            if (ob.isNull(key) || ("" + ob.getInt(key)).equals("null")) {
                return -1;
            }
            return ob.getInt(key);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getString(JSONObject ob, String key) {
        try {
            if (ob.isNull(key) || ob.get(key) == null) {
                return "";
            }
            return ob.getString(key) + "";
        } catch (Exception e) {
            return "";
        }
    }

    //判断json里面这个key是否为null
    public static boolean isNull(JSONObject ob, String key) {
        try {
            if (ob.isNull(key) || ob.get(key) == null || "null".equals(ob.get(key)) || "".equals(ob.get(key))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 判断json里面的数据不是
     */
    public static boolean isObject(JSONObject ob, String key) {
        try {
            if (ob.isNull(key)) {
                return false;
            }
            if (ob.get(key).equals(null) || ob.get(key) == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean getBoolen(JSONObject ob, String key) {
        try {
            if (ob.isNull(key)) {
                return false;
            }
            return ob.getBoolean(key);
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getLong(JSONObject ob, String key) {
        try {
            if (ob.isNull(key)) {
                return 0l;
            }
            return ob.getLong(key);
        } catch (Exception e) {
            return 0l;
        }
    }

    public static int StrToInt(String str) {
        if (TextUtils.isEmpty(str)) return 0;
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 对double数据进行取精度.
     *
     * @param value        double数据.
     * @param scale        精度位数(保留的小数位数).
     * @param roundingMode 精度取值方式.
     * @return 精度计算后的数据.
     */
    public static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value + "");
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    public static long strToLong(String str) {
        if (TextUtils.isEmpty(str)) return 0;
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return 0;
        }

    }

    public static double strToDouble(String str) {
        if (TextUtils.isEmpty(str)) return 0;
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }

    }

    public static String addZero(int number) {
        if (number < 10) return "0" + number;
        return "" + number;
    }

    public static double roundDouble(double d) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

    public static double roundDouble(double d, int size) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(size, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void copy(String text) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) MyApplication.context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    public static boolean isEmpty(TextView tv) {
        if (tv == null || DataUtils.isEmpty(tv.getText().toString())) {
            return true;
        }
        return false;
    }

    public static void textMedium(TextView tv, float size) {
        //获取当前控件的画笔
        TextPaint paint = tv.getPaint();
        //设置画笔的描边宽度值
        paint.setStrokeWidth(size);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

}

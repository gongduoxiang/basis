package com.yc.basis.utils;

import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class SpannableUtils {

    public static final int type = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
    private SpannableString spannable;
    private String allTxt;

    public SpannableUtils(String allTxt) {
        this.allTxt = allTxt;
        spannable = new SpannableString(allTxt);
    }

    public SpannableString getSpannableString() {
        return spannable;
    }

    /**
     * 设置颜色
     */
    public SpannableString setTextColor(int color, String... colorTxt) {
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        for (String s : colorTxt) {
            int index = allTxt.indexOf(s);
            if (index != -1)
                spannable.setSpan(span, index, s.length() + index, type);
        }
        return spannable;
    }

    /**
     * 设置字体样式正常，粗体，斜体，粗斜体
     * android.graphics.Typeface.NORMAL
     * android.graphics.Typeface.BOLD
     * android.graphics.Typeface.ITALIC
     * android.graphics.Typeface.BOLD_ITALIC
     */
    public SpannableString setTextStyle(int state, String... txt) {
        StyleSpan span = new StyleSpan(state);
        for (String s : txt) {
            int index = allTxt.indexOf(s);
            spannable.setSpan(span, index, s.length() + index, type);
        }
        return spannable;
    }

    //ClickableSpan
    public void setClick(String txt, ClickableSpan span) {
        int index = allTxt.indexOf(txt);
        spannable.setSpan(span, index, txt.length() + index, type);
    }


    /**
     * 设置字体大小
     */
    public SpannableString setTextSize(int size, String... txt) {
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(size, true);
        for (String s : txt) {
            int index = allTxt.indexOf(s);
            spannable.setSpan(span, index, s.length() + index, type);
        }
        return spannable;
    }


    /**
     * 加中划线
     */
    public static void zhongXian(TextView view) {
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
    }

    public static void XiaXian(TextView view) {//UNDERLINE_TEXT_FLAG
        view.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
    }

    public static void bold(TextView tv) {
        //获取当前控件的画笔
        TextPaint paint = tv.getPaint();
        //设置画笔的描边宽度值
        paint.setStrokeWidth(1.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public static void noBold(TextView tv) {
        //获取当前控件的画笔
        TextPaint paint = tv.getPaint();
        //设置画笔的描边宽度值
        paint.setStrokeWidth(0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

}

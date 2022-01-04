package com.yc.basis.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EdgeEffect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class ViewPagerFixed extends ViewPager {
    EdgeEffect leftEdge, rightEdge;
    pageChangeListener pageChangeListener;
    private boolean isMove = true;

    public ViewPagerFixed(@NonNull Context context) {
        this(context, null);
    }

    public ViewPagerFixed(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        try {
            Field leftEdgeField = getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffect) leftEdgeField.get(this);
                rightEdge = (EdgeEffect) rightEdgeField.get(this);
            }
        } catch (Exception e) {
        }
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (leftEdge != null && rightEdge != null) {//去掉左右最边上的渐变色
                    leftEdge.finish();
                    rightEdge.finish();
                    leftEdge.setSize(0, 0);
                    rightEdge.setSize(0, 0);
                }
                if (pageChangeListener != null)
                    pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (pageChangeListener != null) pageChangeListener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (pageChangeListener != null) pageChangeListener.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if (isMove)
                return super.onTouchEvent(ev);
            else return false;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (isMove)
                return super.onInterceptTouchEvent(ev);
            return false;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void setMove(boolean b) {
        isMove = b;
    }

    public void addOnPageChangeListener(pageChangeListener listener) {
        pageChangeListener = listener;
    }

    public abstract static class pageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageScrollStateChanged(int state) {

        }

        public abstract void onPageSelected(int position);
    }

}

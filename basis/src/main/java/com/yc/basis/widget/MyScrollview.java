package com.yc.basis.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.widget.NestedScrollView;

import com.yc.basis.utils.DeviceUtils;


public class MyScrollview extends NestedScrollView {

    private ScrollviewListener scrollViewListener = null;
    private ScrollviewTitleListener titleListener;
    private int maxHight, titleHight;//最大临界点   标题栏的高度
    private boolean isNeedScroll = true;

    public MyScrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollview(Context context) {
        super(context);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isNeedScroll)
                    return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /*
   改方法用来处理NestedScrollView是否拦截滑动事件
    */
    private void setNeedScroll(boolean isNeedScroll) {
        this.isNeedScroll = isNeedScroll;
    }

    public void setScrollViewListener(ScrollviewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public void setTitleListener(int max, int hight, ScrollviewTitleListener listener) {
        titleListener = listener;
        maxHight = max;
        titleHight = hight;
    }

    @Override
    protected void onScrollChanged(int x, int scrollY, int oldx, int oldy) {
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, scrollY, oldx, oldy);
        }
        if (titleListener != null) {
            if (scrollY >= maxHight - titleHight) {//显示平常的title颜色
                if (scrollY > (maxHight - titleHight + DeviceUtils.dip2px(30))) {
                    isNeedScroll = false;
                    titleListener.onScrollChanged(1);
                } else {
                    if ((scrollY - maxHight - titleHight) / 100f < 0.3) {
                        titleListener.onScrollChanged(0.3f);
                    } else {
                        titleListener.onScrollChanged((scrollY - maxHight - titleHight) / 100f);
                    }
                    isNeedScroll = true;
                }
            } else {//显示 透明色
                titleListener.onScrollChanged(0);
            }
        }
    }

    public interface ScrollviewListener {//滚动监听

        void onScrollChanged(MyScrollview scrollView, int x, int y, int oldx, int oldy);
    }

    public interface ScrollviewTitleListener {//标题 切换的

        void onScrollChanged(float aple);
    }
}

package com.yc.basis.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.yc.basis.utils.DeviceUtils;
import com.yc.basis.utils.SPUtils;
import com.yc.basis.utils.Toaster;

/*
  moveView.setTopJl(0, DeviceUtils.dip2px(200));
 moveView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

 @Override public void onGlobalLayout() {
  moveView.smoothScrollTo(DeviceUtils.getWidth(), 1,
  DeviceUtils.getScreenHeight() - DeviceUtils.dip2px(200));
  moveView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
  }
  });
 */
public class MoveView extends FrameLayout {

    private boolean isMove = false;
    private int top = 200;//离屏幕差多少
    private int bottom = 400;
    private int hightC;//高度的差值  就是高度的一半

    public void setTopJl(int top, int bottom) {//设置离上面的距离
        this.top = top;
        this.bottom = bottom;
    }

    public MoveView(@NonNull Context context) {
        this(context, null);
    }

    public MoveView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int sX, sY;
    int stopX, stopY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x;
        int y;
        if (hightC <= 0) {
            hightC = getHeight() / 2;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                stopX = 0;
                stopY = 0;
                sX = (int) event.getRawX() - getWidth() / 2;
                sY = (int) event.getRawY() - hightC;
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getRawX() - getWidth() / 2;
                y = (int) event.getRawY() - hightC;
                if (Math.abs(x - sX) < 40 && Math.abs(y - sY) < 40) {
                    break;
                }
                isMove = true;
                int newX = x, newY = y;
                if (x < 0) {
                    newX = 0;
                } else if (x > DeviceUtils.getScreenWidth() - getWidth()) {
                    newX = DeviceUtils.getScreenWidth() - getWidth();
                }
                if (y < top) {
                    newY = top;
                } else if (y > DeviceUtils.getScreenHeight() - bottom - hightC) {
                    newY = DeviceUtils.getScreenHeight() - bottom - hightC;
                }
                stopX = newX;
                stopY = newY;
                setXY(newX, newY);
//                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (isMove) {
                    sX = 0;
                    sY = 0;
                    int xx = DeviceUtils.getScreenWidth() - getWidth();
                    if (stopX <= xx / 2) {//靠左边
                        smoothScrollTo(stopX, 0, stopY);
                    } else {//靠右边
                        smoothScrollTo(stopX, 1, stopY);
                    }
                    return isMove;
                }
                break;
        }
//        if (isMove)
        return true;
//        else return super.dispatchTouchEvent(event);
    }

    private void MyDispatchTouchEvent() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
            if (getParent().getParent() != null) {
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                if (getParent().getParent().getParent() != null) {
                    getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                    if (getParent().getParent().getParent().getParent() != null) {
                        getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                        if (getParent().getParent().getParent().getParent().getParent() != null) {
                            getParent().getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                            if (getParent().getParent().getParent().getParent().getParent().getParent() != null) {
                                getParent().getParent().getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                }
            }
        }
    }

    // 这个用来处理如果 在屏幕中间 缓慢滚动到左右两边，调用该方法即可
    public void smoothScrollTo(final int X, final int toX, int destY) {
        if (hightC <= 0) {
            hightC = getHeight() / 2;
        }
        if (destY > DeviceUtils.getScreenHeight() - bottom - hightC) {
            destY = DeviceUtils.getScreenHeight() - bottom - hightC;
        }
        boolean b = true;
        int newX = X;
        while (b) {
            if (toX <= 0) {
                newX -= 50;
            } else {
                newX += 50;
            }
            if (newX < 0) {
                newX = 0;
            } else if (newX > DeviceUtils.getScreenWidth() - getWidth()) {
                newX = DeviceUtils.getScreenWidth() - getWidth();
            }
            setXY(newX, destY);
//            invalidate();
            if (toX <= 0 && newX <= 0) {
                SPUtils.saveMoveViewXY(0, (int) getY());
                b = false;
                break;
            } else if (toX >= 1
                    && newX >= DeviceUtils.getScreenWidth() - getWidth()) {
                SPUtils.saveMoveViewXY(DeviceUtils.getScreenWidth() - getWidth(), (int) getY());
                b = false;
                break;
            }
        }
    }

    public void setXY(int newX, int destY) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) lp).setMargins(newX, destY, 0, 0);
        } else if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) lp).setMargins(newX, destY, 0, 0);
        } else if (getLayoutParams() instanceof LayoutParams) {
            ((LayoutParams) lp).setMargins(newX, destY, 0, 0);
        } else if (getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
            ((ConstraintLayout.LayoutParams) lp).setMargins(newX, destY, 0, 0);
        } else {
            Toaster.toast("布局类型不正确");
        }
        setLayoutParams(lp);
    }

    //设置开始位置
    public void initLocation(int left, int top) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                smoothScrollTo(left, 1, top);
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

}

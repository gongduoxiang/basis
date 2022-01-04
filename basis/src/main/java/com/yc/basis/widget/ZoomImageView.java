package com.yc.basis.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.yc.basis.utils.MyLog;

//手势缩放view
public class ZoomImageView extends AppCompatImageView implements
        View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {

    /**
     * 最大放大倍数
     */
    public final float SCALE_MAX = 2.5f;
    public final float SCALE_Min = 1f;
    //当前缩放比例
    private float initScale = 1.0f;

    /**
     * 手势检测
     */
    ScaleGestureDetector scaleGestureDetector = null;
    //放大
    Matrix scaleMatrix = new Matrix();

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);
    }

    float oldX, oldY;
    //处理缩放之后  可能会触发  平移的误操作
    boolean isMove = true;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    oldX = event.getX();
                    oldY = event.getY();
                    isMove = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1 && isMove) {
                    scaleMatrix.postTranslate(event.getX() - oldX, event.getY() - oldY);
                    oldX = event.getX();
                    oldY = event.getY();
                    setImageMatrix(scaleMatrix);
                }
        }
        return true;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        MyLog.i("scaleFactor  " + scaleFactor);
        if (getDrawable() == null)
            return true;
        isMove = false;
        if ((scaleFactor < 1 && initScale <= SCALE_Min) || (scaleFactor > 1 && initScale >= SCALE_MAX)) {
            return true;
        }
        initScale += scaleFactor - 1;
        //设置缩放比例
        scaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
        setImageMatrix(scaleMatrix);
        return true;
    }

    public void setMyMatrix(float left, float top, float size) {
        scaleMatrix = new Matrix();
        initScale = size;
        scaleMatrix.postScale(size, size);
        scaleMatrix.postTranslate(left, top);
        setImageMatrix(scaleMatrix);
    }


    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

}

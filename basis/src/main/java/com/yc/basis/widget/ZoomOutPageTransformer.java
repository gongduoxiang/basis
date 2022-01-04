package com.yc.basis.widget;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

/*设置ViewPager切换效果，即实现画廊效果
    mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    android:clipToPadding="false" 重点是这个+padding
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    public static final float MAX_SCALE = 1.0f;
    public static final float MIN_SCALE = 0.85f;

    public float one = 0;//这种模式可能有偏移量   这个值就是用来保存偏移量的
    public boolean b = false;

    @Override
    public void transformPage(View page, float position) {
        if (!b) {
            b = true;
            one = Math.abs(position);
        }
        position -= one;//减去偏移量
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;
        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);
    }

}

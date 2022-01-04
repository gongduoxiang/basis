package com.yc.basis.base;

import android.view.View;

public interface BaseClickListener extends View.OnClickListener {

    @Override
    default void onClick(View v) {
        if (v.getTag() != null) {
            long oldTime = (long) v.getTag();
            if (oldTime - System.currentTimeMillis() > 700) {
                baseClick(v);
            } else {
                v.setTag(System.currentTimeMillis());
            }
        } else {
            baseClick(v);
        }
    }

    void baseClick(View v);
}

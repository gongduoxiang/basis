package com.yc.basis.dialog;
/*
 * user gongduoxiang
 * data 2019/11/27.
 */

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.yc.basis.R;
import com.yc.basis.utils.DeviceUtils;
import com.yc.basis.utils.SPUtils;


public class Privacy2Dialog extends BaseDialog {

    private View layout, layoutClick;
    private PrivacyDialog dialog;

    public Privacy2Dialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_privacy_2);

        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams wlParams = window.getAttributes();
        if (wlParams != null) {
            wlParams.width = DeviceUtils.getScreenWidth();
            wlParams.height = DeviceUtils.getHeight() - DeviceUtils.getStatusBarHeight();
            //重设
            window.setAttributes(wlParams);
        }


        layoutClick = findViewById(R.id.ll_dialog_click);
        layout = findViewById(R.id.ll_dialog);
        findViewById(R.id.tv_dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new PrivacyDialog(context);
                SPUtils.setPrivacy("");
                dialog.myShow();
                myDismiss();
            }
        });
        layoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout.getVisibility() != View.VISIBLE) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void myShow() {
        super.myShow();
        layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.INVISIBLE);
        }
    }
}

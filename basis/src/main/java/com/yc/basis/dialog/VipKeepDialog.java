package com.yc.basis.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.yc.basis.R;


public class VipKeepDialog extends BaseDialog {

    public VipKeepDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_vip_keep);
        findViewById(R.id.tv_dialog_ok).setOnClickListener(v -> {
            myDismiss();
            if (baseDialogListener != null) baseDialogListener.ok("");
        });
    }

}

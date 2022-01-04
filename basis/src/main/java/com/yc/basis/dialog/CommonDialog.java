package com.yc.basis.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yc.basis.R;
import com.yc.basis.utils.DataUtils;

public class CommonDialog extends BaseDialog {

    private View line1, line2;
    private TextView title, desc, ok;
    private String tvDesc, tvTitle, okDesc, tvCancel;

    public CommonDialog(@NonNull Context context) {
        super(context);
    }

    public CommonDialog setCancelGone() {
        if (cancel != null) {
            cancel.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        }
        return this;
    }

    public CommonDialog setCancelVisible() {
        if (cancel != null) {
            cancel.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public CommonDialog setTitle(String str) {
        tvTitle = str;
        if (title != null) {
            title.setText(str);
        }
        return this;
    }

    public CommonDialog setDesc(String str) {
        tvDesc = str;
        if (desc != null) {
            desc.setText(str);
        }
        return this;
    }

    public CommonDialog setOk(String str) {
        okDesc = str;
        if (ok != null) {
            ok.setText(str);
        }
        return this;
    }

    public CommonDialog setcancel(String str) {
        tvCancel = str;
        if (cancel != null) {
            cancel.setText(str);
        }
        return this;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_common);
        line1 = findViewById(R.id.view_dialog_line1);
        line2 = findViewById(R.id.view_dialog_line2);
        title = findViewById(R.id.tv_dialog_title);
        desc = findViewById(R.id.tv_dialog_desc);
        desc.setText(tvDesc);
        title.setText(tvTitle);
        ok = findViewById(R.id.tv_dialog_ok);
        ok.setOnClickListener(v -> {
            if (baseDialogListener != null) baseDialogListener.ok("");
            myDismiss();
        });
        if (!DataUtils.isEmpty(okDesc)) {
            ok.setText(okDesc);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (!DataUtils.isEmpty(tvCancel)) {
            cancel.setText(tvCancel);
        }
    }
}

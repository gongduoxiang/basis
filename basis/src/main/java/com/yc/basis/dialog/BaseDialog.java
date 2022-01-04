package com.yc.basis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yc.basis.R;
import com.yc.basis.base.BasisBaseActivity;


public abstract class BaseDialog extends Dialog {
    protected Context context;
    protected View cancelLine;
    protected TextView cancel, ok;
    public BaseDialogListener baseDialogListener;
    protected Window mWindow;

    public void setBaseDialogListener(BaseDialogListener listener) {
        baseDialogListener = listener;
    }

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.custom_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }


    protected abstract void initView();

    protected void initData() {
        cancel = findViewById(R.id.tv_dialog_cancel);
        if (cancel != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseDialogListener != null) baseDialogListener.cancel(new Object());
                    myDismiss();
                }
            });
        }
        cancelLine = findViewById(R.id.view_dialog_cancel);
    }

    public void myShow() {
        if (!isShowing()) {
            show();
        }
    }

    public void myDismiss() {
        if (isShowing()) {
            dismiss();
        }
    }

    /**
     * 全屏
     */
    protected void setWidth() {
        if (mWindow == null)
            mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindow.setAttributes(mParams);
    }
    /**
     * 全屏
     */
    protected void setHeight() {
        if (mWindow == null)
            mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindow.setAttributes(mParams);
    }


    /**
     * 位置
     * Gravity.CENTER_HORIZONTAL
     */
    protected void setGravity(int gravity) {
        if (mWindow == null)
            mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        mParams.gravity = gravity;
        mWindow.setAttributes(mParams);
    }

    protected void showLoadLayout() {
        ((BasisBaseActivity) (context)).showLoadLayout();
    }

    protected void removeLoadLayout() {
        ((BasisBaseActivity) (context)).removeLoadLayout();
    }

    /**
     * false  点击空白不会关闭  返回还可以使用
     */
    public void setmCanceledOnTouchOutside(boolean b) {
        setCanceledOnTouchOutside(b);
    }

    /**
     * false  点击空白不会关闭  返回也不可以使用
     */
    public void setmCancelable(boolean b) {
        setCancelable(b);
    }
}

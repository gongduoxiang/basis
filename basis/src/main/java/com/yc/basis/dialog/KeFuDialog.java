package com.yc.basis.dialog;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yc.basis.R;
import com.yc.basis.http.BaseHttpListener;
import com.yc.basis.http.BasisInfo;
import com.yc.basis.http.response.HttpCommon;
import com.yc.basis.utils.DataUtils;
import com.yc.basis.utils.Toaster;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeFuDialog extends BaseDialog {

    private TextView desc, ok;


    public KeFuDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dilaog_kefu);
        desc = findViewById(R.id.tv_dialog_desc);
        ok = findViewById(R.id.tv_dialog_ok);
        ok.setEnabled(false);
        HttpCommon.getCustomerService(new BaseHttpListener() {
            @Override
            public void success(Object o) {
                desc.setText("QQ:" + o);
                ok.setEnabled(true);
            }

            @Override
            public void error(String msg) {
                desc.setText("QQ:" + BasisInfo.qq);
                ok.setEnabled(true);
            }
        });
        ok.setOnClickListener(v -> {
            try {
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(desc.getText().toString());
                m.find();
                String qqNumber = m.group();
                DataUtils.copy(qqNumber);
                Toaster.toast("QQ号已复制");
            } catch (Exception e) {
                Toaster.toast("复制失败");
            } finally {
                myDismiss();
            }
        });
    }
}

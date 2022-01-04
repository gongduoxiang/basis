package com.yc.basis.dialog;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yc.basis.R;
import com.yc.basis.base.BasisBaseActivity;
import com.yc.basis.http.BasisInfo;
import com.yc.basis.utils.DeviceUtils;
import com.yc.basis.utils.SPUtils;
import com.yc.basis.utils.channel.WalleChannelReader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;


/**
 * Created by gongduoxiang on 2019/10/16.
 */

public class PrivacyDialog extends BaseDialog {

    String desc = "《用户协议》";
    String desc2 = "《隐私政策》";
    String desc3 = "尊敬的用户欢迎使用";
    String value = "";
    private TextView tv;
    private Privacy2Dialog dialog;

    public PrivacyDialog(@NonNull Context context) {
        super(context);
        dialog = new Privacy2Dialog(context);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_privacy);
        value = context.getString(R.string.pirvacy);
        tv = findViewById(R.id.tv_privacy_3);
        desc3 += context.getResources().getString(R.string.app_name) + "\n";
        value = desc3 + value;
        int index = value.indexOf(desc);
        int index2 = value.indexOf(desc2);
        SpannableString ss2 = new SpannableString(value);
        ss2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                BasisInfo.startXY();
            }

            //去掉下划线，重写updateDrawState并且setUnderlineText(false)
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, index, index + desc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                BasisInfo.startZC();
            }

            //去掉下划线，重写updateDrawState并且setUnderlineText(false)
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, index2, index2 + desc2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //设置0-mytext.length()距离的颜色
        ss2.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_color)), index,
                index + desc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_color)), index2,
                index2 + desc2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss2);
        tv.setHighlightColor(context.getResources().getColor(R.color.color_00000000));
        tv.setMovementMethod(LinkMovementMethod.getInstance());

        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);


        WindowManager.LayoutParams wlParams = window.getAttributes();
        if (wlParams != null) {
            wlParams.width = DeviceUtils.getScreenWidth();
            //重设
            window.setAttributes(wlParams);
        }

        findViewById(R.id.tv_privacy_no).setOnClickListener(clickListener);
        findViewById(R.id.tv_privacy_yes).setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_privacy_no) {
//                SPUtils.setPrivacy("false");
//                dialog.myShow();
                dismiss();
                ((BasisBaseActivity)context).finish();
            } else if (id == R.id.tv_privacy_yes) {
                UMConfigure.init(context, WalleChannelReader.youMeng, WalleChannelReader.getChannel(context),
                        // "替换为秘钥信息,服务后台位置：应用管理 -> 应用信息 -> Umeng Message Secret"
                        UMConfigure.DEVICE_TYPE_PHONE, "");
                // 选用AUTO页面采集模式
                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
                SPUtils.setPrivacy("true");
                dismiss();
            }

        }
    };

    public void myShow() {
        if (SPUtils.getPrivacy().equals("false")) {
            dialog.myShow();
            return;
        }
        super.myShow();
    }

    @Override
    public void onBackPressed() {
    }
}

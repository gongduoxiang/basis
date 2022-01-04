package com.yc.basis.ui;

import android.os.Handler;

import com.yc.basis.R;
import com.yc.basis.base.BasisBaseActivity;
import com.yc.basis.dialog.PrivacyDialog;
import com.yc.basis.entity.PayEntity;
import com.yc.basis.http.BaseHttpListener;
import com.yc.basis.http.response.HttpCommon;
import com.yc.basis.utils.SPUtils;
import com.yc.basis.utils.Toaster;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public abstract class BasisMainActivity extends BasisBaseActivity {


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //指定为经典Header，默认是 贝塞尔雷达Header
            layout.setPrimaryColorsId(R.color.color_tm, R.color.color_000000);//全局设置主题颜色
            ClassicsHeader header = new ClassicsHeader(context);
            header.setBackgroundResource(R.color.color_tm);
            return header;
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
//                layout.setDisableContentWhenLoading(true);
            layout.setEnableAutoLoadMore(false);
            ClassicsFooter footer = new ClassicsFooter(context).setDrawableSize(20);
            footer.setFinishDuration(500);//参数很明显是时间参数，将参数设为0，就没用白框了
            return footer;
        });
    }

    protected BaseHttpListener httpListener;

    //首页只需要关注   initView的初始化
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        if (!SPUtils.getPrivacy().equals("true")) {
            new PrivacyDialog(this).myShow();
        }
        HttpCommon.getUserInfo(httpListener);
    }

    /**
     * 支付结果
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PayEntity entity) {
        if (PayEntity.ok.equals(entity.flag)) {
            new Handler().postDelayed(() -> HttpCommon.getUserInfo(httpListener), 1000);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    long oldTime = 0;

    @Override
    public void onBackPressed() {
        if (oldTime > System.currentTimeMillis() - 1000) {
            super.onBackPressed();
        } else {
            oldTime = System.currentTimeMillis();
            Toaster.toast("再按一次退出" + getString(R.string.app_name));
        }
    }
}

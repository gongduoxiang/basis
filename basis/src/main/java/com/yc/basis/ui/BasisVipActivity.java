package com.yc.basis.ui;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.yc.basis.base.BasisBaseActivity;
import com.yc.basis.base.CommonAdapter;
import com.yc.basis.dialog.BaseDialogListener;
import com.yc.basis.dialog.VipKeepDialog;
import com.yc.basis.entity.PayEntity;
import com.yc.basis.entity.PayType;
import com.yc.basis.entity.VipEntity;
import com.yc.basis.http.BaseHttpListener;
import com.yc.basis.http.response.HttpCommon;
import com.yc.basis.http.response.HttpVip;
import com.yc.basis.utils.DataUtils;
import com.yc.basis.utils.SPUtils;
import com.yc.basis.utils.Toaster;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BasisVipActivity extends BasisBaseActivity {

    protected TextView wxZf, zfbZf, ok;
    protected PayType payType = new PayType();
    protected ArrayList<VipEntity> mData = new ArrayList<>();
    protected CommonAdapter<VipEntity> adapter;
    protected View wxZfLayout, zfbZfLayout;
    protected int index = 0;//当前选择的下标
    // alipay   wechat
    protected String payName;
    protected HttpVip.PayTypeListener typeListener;
    private VipKeepDialog dialog;

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        dialog = new VipKeepDialog(this);
        dialog.setBaseDialogListener(new BaseDialogListener() {
            @Override
            public void ok(Object o) {
                finish();
            }
        });
        backLayout.setOnClickListener(v -> back());
        zfbZf.setOnClickListener(v -> {
            zfbZf.setSelected(true);
            wxZf.setSelected(false);
            payName = "alipay";
            pay();
        });
        wxZf.setOnClickListener(v -> {
            zfbZf.setSelected(false);
            wxZf.setSelected(true);
            payName = "wechat";
            pay();
        });
        if (ok != null)
            ok.setOnClickListener(v -> {

            });
        HttpVip.payType(new BaseHttpListener() {
            @Override
            public void success(Object o) {
                payType = (PayType) o;
                zfbZfLayout.setVisibility(payType.isZfbPay ? View.VISIBLE : View.GONE);
                wxZfLayout.setVisibility(payType.isWxPay ? View.VISIBLE : View.GONE);
                if (adapter != null) adapter.notifyDataSetChanged();
            }

            @Override
            public void error(String msg) {
                payType = new PayType();
            }
        });
        showLoadLayout();
        HttpVip.payConfig(new BaseHttpListener() {
            @Override
            public void success(Object o) {
                mData.clear();
                mData.addAll((Collection<? extends VipEntity>) o);
                setIndex(mData.size() - 1);
                removeLoadLayout();
            }

            @Override
            public void error(String msg) {
                removeLoadLayout();
            }
        });
    }

    protected void setIndex(int position) {
        index = position;
        zfbZf.setText("支付宝   " + DataUtils.getYuan() + mData.get(index).zfbMoney);
        wxZf.setText("微  信   " + DataUtils.getYuan() + mData.get(index).wxMoney);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    protected String getMoney() {
        if (index >= 0 && index < mData.size()) {
            if ("alipay".equals(payName))
                return mData.get(index).zfbMoney;
            else
                return mData.get(index).wxMoney;
        }
        return "0";
    }

    protected String getVipId() {
        if (index >= 0 && index < mData.size()) {
            return mData.get(index).id;
        }
        return "0";
    }

    protected void pay() {
        if (!SPUtils.isLogin()) {
            login();
            return;
        }
        if (index >= 0 && index < mData.size()) {
            showLoadLayout();
            HttpVip.pay(getMoney(), payName, getVipId(), typeListener);
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (!SPUtils.isVip()) {
            dialog.myShow();
        } else {
            finish();
        }
    }

    /**
     * 支付结果
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PayEntity entity) {
        if (PayEntity.ok.equals(entity.flag)) {
            Toaster.toast("支付成功");
            showLoadLayout();
            new Handler().postDelayed(() -> HttpCommon.getUserInfo(new BaseHttpListener() {
                @Override
                public void success(Object o) {
                    removeLoadLayout();
                    finish();
                }

                @Override
                public void error(String msg) {
                    removeLoadLayout();
                    finish();
                }
            }), 500);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    protected abstract void login();

}

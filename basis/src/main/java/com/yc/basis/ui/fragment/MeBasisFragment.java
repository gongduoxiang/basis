package com.yc.basis.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.basis.R;
import com.yc.basis.base.BaseFragment;
import com.yc.basis.entity.LoginEventEntity;
import com.yc.basis.http.BasisInfo;
import com.yc.basis.satusbar.StatusBarUtil;
import com.yc.basis.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MeBasisFragment extends BaseFragment {

    protected ImageView userPhoto, vipKg;
    protected TextView userName, vip, login;
    protected View line, guangGao;

    @Override
    protected int setLayout() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {
        StatusBarUtil.setStatusBarHeight(findViewById(R.id.fl_title));
        userPhoto = findViewById(R.id.iv_me_userPhoto);
        userName = findViewById(R.id.tv_me_userName);
        vip = findViewById(R.id.tv_me_vip);
        login = findViewById(R.id.tv_me_login);
        guangGao = findViewById(R.id.tv_me_gg);
        line = findViewById(R.id.view_line_me);
        vipKg = findViewById(R.id.iv_me_vipKg);

        guangGao.setOnClickListener(this);
        findViewById(R.id.tv_me_kefu).setOnClickListener(this);
        findViewById(R.id.tv_me_yszc).setOnClickListener(this);
        findViewById(R.id.tv_me_yhxy).setOnClickListener(this);
        findViewById(R.id.tv_me_gy).setOnClickListener(this);
        userPhoto.setOnClickListener(this);
        userName.setOnClickListener(this);
        vip.setOnClickListener(this);
        login.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        setUser();
    }

    protected void setUser() {
//        if (DataUtils.isEmpty(SPUtils.getToken())) {
//            userPhoto.setImageResource(R.drawable.user_default);
//            userName.setText("未登录");
//            login.setText("登录");
//        } else {
//            User user = SPUtils.getUser();
//            GlideUtil.userPhoto(user.photo, userPhoto);
//            userName.setText(user.name);
//            login.setText("退出登录");
//        }
    }


    /**
     * 登录成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEventEntity entity) {
        if (LoginEventEntity.ok.equals(entity.flag)) {
            setUser();
            setVipText();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setVipText();
    }

    protected void setVipText() {
//        if (BuildConfigUtils.isHuaWei()) {
//            guangGao.setVisibility(View.GONE);
//            line.setVisibility(View.GONE);
//        }
//        if (SPUtils.isVip()) {
//            vipKg.setSelected(SPUtils.getAd());
//            long time = SPUtils.getUser().time;
//            if (time > 1000) {
//                vip.setText("会员中心      到期时间:" + DataUtils.timeToData(time, "yyyy-MM-dd"));
//            } else {
//                vip.setText("会员中心");
//            }
//        } else {
//            vipKg.setSelected(false);
//            vip.setText("会员中心");
//        }
    }

    protected void login() {
//        if (!SPUtils.isLogin()) {
//            WeChatPay.weChatLogin(getActivity());
//        }
    }

    protected void startVipActivity() {
//        startActivity(new Intent(getContext(), VipActivity.class));
    }


    @Override
    public void baseClick(View v) {
        Intent intent;
        int id = v.getId();
        if (id == R.id.iv_me_userPhoto || id == R.id.tv_me_userName) {
            login();
        } else if (id == R.id.tv_me_vip) {
            if (!SPUtils.isLogin()) {
                login();
                return;
            }
            startVipActivity();
        } else if (id == R.id.tv_me_kefu) {
            BasisInfo.startKeFu(getActivity());
        } else if (id == R.id.tv_me_yszc) {
            BasisInfo.startZC();
        } else if (id == R.id.tv_me_yhxy) {
            BasisInfo.startXY();
        } else if (id == R.id.tv_me_gy) {
            BasisInfo.startGY(getActivity());
        } else if (id == R.id.tv_me_login) {
            if (SPUtils.isLogin()) {
                SPUtils.loginOut();
                setUser();
                vip.setText("会员中心");
            } else {
                login();
            }
        } else if (id == R.id.tv_me_gg) {
            if (!SPUtils.isLogin()) {
                login();
                return;
            }
            if (SPUtils.isVip()) {
                SPUtils.setAd(!SPUtils.getAd());
                vipKg.setSelected(SPUtils.getAd());
            } else {
                startVipActivity();
            }
        }
    }
}

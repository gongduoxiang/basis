package com.yc.basis.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.basis.R;
import com.yc.basis.satusbar.StatusBarUtil;
import com.yc.basis.widget.TitleView;

import java.util.ArrayList;
import java.util.List;


public abstract class BasisBaseActivity extends AppCompatActivity implements BaseClickListener {
    protected static List<Activity> activities = new ArrayList<>();
    protected TitleView titleLayout;
    protected View backLayout;
    protected View loadLayout;
    private ViewGroup viewRoot;
    protected View noMessage;

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(WifiEntity entity)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        create(savedInstanceState);
        super.onCreate(savedInstanceState);
        if (!activities.contains(this)) {
            activities.add(this);
        }
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        setTextBlack(true);
        initView();
        initSmartRefreshLayout();
        initTitleLayout();
        initData();
    }

    protected void create(@Nullable Bundle savedInstanceState) {
    }

    protected void initSmartRefreshLayout() {
    }

    private void initNoMessage() {
        if (noMessage == null)
            noMessage = findViewById(R.id.tv_noMsg);
    }

    protected void showNoMessage() {
        initNoMessage();
        if (noMessage != null) noMessage.setVisibility(View.VISIBLE);
    }

    protected void removeNoMessage() {
        if (noMessage != null) noMessage.setVisibility(View.GONE);
    }

    protected abstract void initView();

    protected abstract void initData();

    protected void http() {
    }

    public void initTitleLayout() {
        backLayout = findViewById(R.id.ll_title_back);
        if (backLayout != null) {
            backLayout.setOnClickListener(view -> onBackPressed());
        }
        titleLayout = findViewById(R.id.titleView);
        if (titleLayout != null) {
            StatusBarUtil.setStatusBarHeight(titleLayout);
        }
    }

    public void setTitleLayoutBackgroundColor(int colorId) {
        if (titleLayout != null) titleLayout.setBackgroundColor(getResources().getColor(colorId));
    }

    public void setTitleLayoutBackgroundResource(int id) {
        if (titleLayout != null) titleLayout.setBackgroundResource(id);
    }

    public void showLoadLayout() {
        if (viewRoot == null) viewRoot = (ViewGroup) getWindow().getDecorView();
        if (loadLayout == null) {
            loadLayout = LayoutInflater.from(this).inflate(R.layout.g_load_layout,
                    null);
            viewRoot.addView(loadLayout);
        } else {
            loadLayout.setVisibility(View.VISIBLE);
        }
    }

    public void removeLoadLayout() {
        if (loadLayout != null) {
            runOnUiThread(() -> loadLayout.setVisibility(View.GONE));
        }
    }

    public void load_layout_click(View view) {

    }

    /**
     * 是否改变状态栏的字体颜色  true黑色  false白色
     */
    public void setTextBlack(boolean b) {
        StatusBarUtil.setStatusBarDarkTheme(this, b);
    }

    @Override
    protected void onDestroy() {
        activities.remove(this);
        super.onDestroy();
    }

    protected void finishAll() {
        if (activities != null) {
            for (int i = 0; i < activities.size(); i++) {
                activities.get(i).finish();
            }
        }
    }

    //设置字体为默认大小，不随系统字体大小改而改变
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}

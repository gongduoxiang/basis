package com.yc.basis.ui;

import android.annotation.SuppressLint;
import android.view.Gravity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.yc.basis.R;

public abstract class TabMainActivity extends BasisMainActivity {
    protected ActionBarDrawerToggle toggle;
    protected DrawerLayout drawerLayout;

    @Override
    protected void initData() {
        super.initData();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.s_1, R.string.s_2);
        toggle.syncState();
    }

    @SuppressLint("WrongConstant")
    public void close() {
        drawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

}

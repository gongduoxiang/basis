package com.yc.basis.ui;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yc.basis.R;
import com.yc.basis.base.BaseFragment;
import com.yc.basis.utils.DrawableUtil;
import com.yc.basis.widget.ViewPagerFixed;

import java.util.ArrayList;
import java.util.List;

public abstract class BasisMainVpActivity extends BasisMainActivity {
    protected List<BaseFragment> mData = new ArrayList<>();
    protected FragmentPagerAdapter adapter;
    protected ViewPagerFixed pager;
    protected List<TextView> textViews = new ArrayList<>();
    protected List<Drawable> drawablesY = new ArrayList<>();
    protected List<Drawable> drawablesN = new ArrayList<>();
    protected int colorY, colorN;

    @Override
    protected void initData() {
        super.initData();
        adapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mData.get(position);
            }

            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }
        };
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(mData.size());
        pager.addOnPageChangeListener(new ViewPagerFixed.pageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setSelectText(position);
                selectUpdate(position);
            }
        });
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTag(R.id.main_id, i);
            textViews.get(i).setOnClickListener(v -> {
                int position = (int) v.getTag(R.id.main_id);
                setSelect(position);
            });
        }
        setSelect(0);
    }

    protected void setSelect(int position) {
        setSelectText(position);
        pager.setCurrentItem(position);
        selectUpdate(position);
    }

    protected void setSelectText(int position) {
        for (int i = 0; i < textViews.size(); i++) {
            if (position == i) {
                textViews.get(i).setTextColor(colorY);
                DrawableUtil.drawableTop(textViews.get(i), drawablesY.get(i));
            } else {
                textViews.get(i).setTextColor(colorN);
                DrawableUtil.drawableTop(textViews.get(i), drawablesN.get(i));
            }
        }
    }

    protected void selectUpdate(int position) {

    }
}

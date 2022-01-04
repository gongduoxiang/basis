package com.yc.basis.ui;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yc.basis.R;
import com.yc.basis.base.BasisBaseActivity;
import com.yc.basis.share.SystemShareUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:   查看照片的原图
 */

public class PhotoLookActivity extends BasisBaseActivity {

    private ViewPager vp;
    private List<String> data;
    private ImageView[] mImageViews;
    private int position = 0;
    private RequestOptions options = RequestOptions.errorOf(R.drawable.error_img).diskCacheStrategy(DiskCacheStrategy.DATA);
    private TextView share;

    @Override
    protected void initView() {
        setContentView(R.layout.photo_look);
        Intent intent = getIntent();
        try {
            data = intent.getStringArrayListExtra("photo");
            if (data == null) {
                String string = intent.getStringExtra("photo");
                data = new ArrayList<>();
                data.add(string);
            }
            position = getIntent().getIntExtra("position", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        vp = findViewById(R.id.vp_photo_look);
        findViewById(R.id.fl_title_1).setOnClickListener(v -> finish());
        share = findViewById(R.id.tv_look_share);

        share.setOnClickListener(view -> SystemShareUtils.shareFile(data.get(position)));
    }

    @Override
    protected void initData() {
        if (data == null) {
            finish();
            return;
        }
        mImageViews = new ImageView[data.size()];
        vp.setAdapter(new PagerAdapter() {
            // 添加方法
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(PhotoLookActivity.this);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(lp);
                Glide.with(PhotoLookActivity.this).load(data.get(position)).thumbnail(0.5f).apply(options).into(imageView);
                container.addView(imageView);
                mImageViews[position] = imageView;
                return imageView;
            }

            // 移除方法
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews[position]);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return data.size();
            }
        });
        vp.setCurrentItem(position);
    }

    @Override
    public void baseClick(View v) {
    }
}

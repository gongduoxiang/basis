package com.yc.basis.ui;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.basis.R;
import com.yc.basis.adapter.SelectPhotoAdapter;
import com.yc.basis.base.BasisBaseActivity;
import com.yc.basis.entity.FileEntity;
import com.yc.basis.utils.SystemFindFile;
import com.yc.basis.utils.Toaster;
import com.yc.basis.utils.VersionUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectPhotoActivity extends BasisBaseActivity {
    private RecyclerView rlv;
    private TextView desc;
    private SelectPhotoAdapter adapter;
    private List<FileEntity> mData = new ArrayList<>();
    private int min = 2;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_photo);
        min = getIntent().getIntExtra("min", 2);
        desc = findViewById(R.id.tv_select_photo_desc);
        rlv = findViewById(R.id.rlv_select_photo);
        rlv.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new SelectPhotoAdapter(this, mData, R.layout.activity_select_photo_item);
        adapter.max = getIntent().getIntExtra("max", 100);
        rlv.setAdapter(adapter);
        setDesc();
        adapter.setListener(this::setDesc);
        adapter.setRecyclerOnIntemClickListener((v, position) -> {
            if (adapter.selectPhoto.size() >= adapter.max) {
                Toaster.toast("最多选择" + adapter.max + "张图片");
                return;
            }
            adapter.selectPhoto.add(mData.get(position).url);
            adapter.uriPhoto.add(mData.get(position).uri.toString());
            if (adapter.max == 1 && adapter.selectPhoto.size() == 1) {
                Intent intent = new Intent();
                if (VersionUtils.isAndroid10()){
                    intent.putExtra("data", adapter.uriPhoto);
                }else {
                    intent.putExtra("data", adapter.selectPhoto);
                }
                setResult(RESULT_OK, intent);
                finish();
                return;
            }
            adapter.notifyDataSetChanged();
            setDesc();
        });
    }

    private void setDesc() {
        String allTxt = getString(R.string.select_photo, adapter.selectPhoto.size() + "", min + "");
        SpannableString spannable = new SpannableString(allTxt);
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.main_color));
        int index = allTxt.indexOf(adapter.selectPhoto.size() + "");
        if (index != -1)
            spannable.setSpan(span, index, (adapter.selectPhoto.size() + "").length() + index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        desc.setText(spannable);
    }

    @Override
    protected void initData() {
        titleLayout.setRightTvClick(v -> {
            if (adapter.selectPhoto.size() < min) {
                Toaster.toast("请选择最少" + min + "张的图片");
                return;
            }
            if (adapter.selectPhoto.size() == 0) {
                Toaster.toast("请选择图片");
                return;
            }
            Intent intent = new Intent();
            if (VersionUtils.isAndroid10()){
                intent.putExtra("data", adapter.uriPhoto);
            }else {
                intent.putExtra("data", adapter.selectPhoto);
            }
            setResult(RESULT_OK, intent);
            finish();
        });
        showLoadLayout();
        new Thread() {
            @Override
            public void run() {
                mData.clear();
                mData.addAll(SystemFindFile.getAllImag());
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    removeLoadLayout();
                });
            }
        }.start();
    }

    @Override
    public void baseClick(View v) {
    }
}

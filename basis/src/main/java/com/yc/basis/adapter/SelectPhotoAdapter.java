package com.yc.basis.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.yc.basis.R;
import com.yc.basis.base.CommonRecyclerAdapter;
import com.yc.basis.base.RecyclerViewHolder;
import com.yc.basis.entity.FileEntity;
import com.yc.basis.utils.GlideUtil;
import com.yc.basis.utils.Toaster;

import java.util.ArrayList;
import java.util.List;

public class SelectPhotoAdapter extends CommonRecyclerAdapter<FileEntity> {

    public ArrayList<String> selectPhoto = new ArrayList<>();
    public ArrayList<String> uriPhoto = new ArrayList<>();

    public int max = 30;

//    private boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    public SelectPhotoAdapter(Context context, List<FileEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(RecyclerViewHolder holder, FileEntity item, int position) {
        ImageView icon = holder.getView(R.id.iv_select_photo_item_icon);
        ImageView selecty = holder.getView(R.id.iv_select_photo_item_y);
        ImageView select = holder.getView(R.id.iv_select_photo_item_select);
        selecty.setOnClickListener(v -> {
            selectPhoto.remove(item.url);
            uriPhoto.remove(item.uri.toString());
            select.setSelected(false);
            notifyDataSetChanged();
            listener.update();
        });
        if (max == 1) {
            select.setVisibility(View.GONE);
        } else {
            select.setVisibility(View.VISIBLE);
        }
        if (selectPhoto.contains(item.url)) {
            select.setSelected(true);
            selecty.setVisibility(View.VISIBLE);
        } else {
            select.setSelected(false);
            selecty.setVisibility(View.GONE);
        }
        select.setOnClickListener(v -> {
            if (selectPhoto.contains(item.url)) {
                selectPhoto.remove(item.url);
                uriPhoto.remove(item.uri.toString());
                select.setSelected(false);
            } else {
                if (selectPhoto.size() >= max) {
                    Toaster.toast("最多选择" + max + "张图片");
                    return;
                }
                selectPhoto.add(item.url);
                uriPhoto.add(item.uri.toString());
                select.setSelected(true);
            }
            listener.update();
            notifyDataSetChanged();
        });
//        GlideUtil.filletPhoto(isAndroidQ ? item.uri : item.url, icon, 4);
        GlideUtil.filletPhoto(item.url, icon, 4);
    }

    private updateListener listener;

    public void setListener(updateListener listener) {
        this.listener = listener;
    }

    public interface updateListener {
        void update();
    }

}

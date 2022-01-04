package com.yc.basis.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    //数据怎么办？利用泛型
    protected List<T> mDatas;
    // 布局怎么办？直接从构造里面传递
    private int mLayoutId;
    protected RecyclerOnIntemClickListener listener;
    private RecyclerOnIntemLongClickListener longListener;

    public CommonRecyclerAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = datas;
        this.mLayoutId = layoutId;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 先inflate数据
        View itemView = mInflater.inflate(mLayoutId, parent, false);
        // 返回ViewHolder
        RecyclerViewHolder holder = new RecyclerViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.setOnIntemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClick(v, position);

            }
        });
        holder.setOnIntemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longListener != null) longListener.onLongClick(v, position);
                return true;
            }
        });
        // 绑定怎么办？回传出去
        convert(holder, mDatas.get(position), position);
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     * @param item 当前的数据
     */
    public abstract void convert(RecyclerViewHolder holder, T item, int position);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setRecyclerOnIntemClickListener(RecyclerOnIntemClickListener listener) {
        this.listener = listener;
    }

    public void setRecyclerOnIntemLongClickListener(RecyclerOnIntemLongClickListener listener) {
        this.longListener = listener;
    }


}

package com.yc.basis.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewHolder {

	private final SparseArray<View> mView;
	private View mConverView;
	private int mPosition;

	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mView = new SparseArray<>();
		mConverView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		this.mPosition = position;
		// settag
		mConverView.setTag(this);
	}

	public View getConvertView() {
		return mConverView;
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
//		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
//		}
//		return (ViewHolder) convertView.getTag();
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @param <T>
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mView.get(viewId);
		if (view == null) {
			view = mConverView.findViewById(viewId);
			mView.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	public int getPosition() {
		return mPosition;
	}

}

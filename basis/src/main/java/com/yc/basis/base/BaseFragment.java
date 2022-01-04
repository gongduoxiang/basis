package com.yc.basis.base;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.yc.basis.R;

import java.util.Locale;


public abstract class BaseFragment extends Fragment implements BaseClickListener {
    protected View mView;
    protected View noMessage;
    //    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(WifiEntity entity)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            int id = setLayout();
            if (id > 0)
                mView = inflater.inflate(id, container, false);
            initBinding(inflater);
            initView();
            initSmartRefreshLayout();
            initData();
//            shiftLanguage(SPUtils.getLanguage());
        }
        return mView;
    }

    public void shiftLanguage(int sta) {
        if (sta == 2) {
            Locale.setDefault(Locale.ENGLISH);
            Configuration config = getActivity().getBaseContext().getResources().getConfiguration();
            config.locale = Locale.ENGLISH;
            getActivity().getBaseContext().getResources().updateConfiguration(config
                    , getActivity().getBaseContext().getResources().getDisplayMetrics());
        }else{
            Locale.setDefault(Locale.CHINESE);
            Configuration config = getActivity().getBaseContext().getResources().getConfiguration();
            config.locale = Locale.CHINESE;
            getActivity().getBaseContext().getResources().updateConfiguration(config
                    , getActivity().getBaseContext().getResources().getDisplayMetrics());
        }
    }

    protected void initBinding(LayoutInflater inflater) {

    }

    protected void initSmartRefreshLayout() {
    }

    private void initNoMessage() {
        if (noMessage == null)
            noMessage = mView.findViewById(R.id.tv_noMsg);
    }

    protected void isShowNoMessage(boolean b) {
        if (b) {
            showNoMessage();
        } else {
            removeNoMessage();
        }
    }

    protected void showNoMessage() {
        initNoMessage();
        if (noMessage != null) noMessage.setVisibility(View.VISIBLE);
    }

    protected void removeNoMessage() {
        if (noMessage != null) noMessage.setVisibility(View.GONE);
    }

    protected abstract int setLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected <T extends View> T findViewById(int id) {
        return mView.findViewById(id);
    }

    protected void showLoadLayout() {
        try {
            ((BasisBaseActivity) getActivity()).showLoadLayout();
        } catch (Exception e) {
        }
    }

    protected void removeLoadLayout() {
        try {
            ((BasisBaseActivity) getActivity()).removeLoadLayout();
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }
}

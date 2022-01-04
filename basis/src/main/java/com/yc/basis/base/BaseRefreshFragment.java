package com.yc.basis.base;

import androidx.annotation.NonNull;

import com.yc.basis.R;
import com.yc.basis.http.BaseHttpListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public abstract class BaseRefreshFragment extends BaseFragment implements OnRefreshLoadMoreListener {

    protected int pageIndex = 1;//这个是页数
    protected BaseHttpListener httpListener;
    protected SmartRefreshLayout mRefreshLayout;
    protected boolean refresh = true;

    protected void initSmartRefreshLayout() {
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        httpListener = new BaseHttpListener() {
            @Override
            public void success(Object o) {
                if (o instanceof List) {
                    List list = (List) o;
                    if (list.size() == 0 && !refresh) pageIndex--;
                }
                onSuccess(refresh, o);
                finishRefreshAndLoadMore();
                ((BasisBaseActivity) getActivity()).removeLoadLayout();
            }

            @Override
            public void error(String msg) {
                onFailure(msg);
                if (!refresh && pageIndex > 1) {
                    pageIndex--;
                }
                finishRefreshAndLoadMore();
                if (getActivity() != null)
                    ((BasisBaseActivity) getActivity()).removeLoadLayout();
            }
        };
    }

    /**
     * 接口请求的方法
     * 这个只能是需要刷新或者加载的接口
     * true  刷新   false加载
     */
    protected abstract void getData();

    //刷新
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mRefreshLayout != null) {
            pageIndex = 1;
            this.refresh = true;
            getData();
        }
    }

    //加载
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mRefreshLayout != null) {
            pageIndex++;
            this.refresh = false;
            getData();
        }
    }

    protected abstract void onSuccess(boolean refresh, Object obj);

    protected abstract void onFailure(String errorInfo);


    /**
     * 自动刷新
     */
    protected void autoRefresh() {
        if (mRefreshLayout != null) mRefreshLayout.autoRefresh();//自动刷新
    }

    /**
     * 停止动画 刷新和加载的动画
     */
    protected void finishRefreshAndLoadMore() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
        }
    }

    /**
     * 禁止刷新
     */
    protected void closeRefresh(boolean b) {
        if (mRefreshLayout != null) mRefreshLayout.setEnableRefresh(b);
    }

    /**
     * 禁止加载   false 关闭
     */
    protected void closeLoadMore(boolean b) {
        if (mRefreshLayout != null) mRefreshLayout.setEnableLoadMore(b);
    }

    /**
     * 如果加载数据不够一页的就关闭加载
     * 是否在列表不满一页时候开启上拉加载功能
     */
    private void closeLoadMore2(boolean b) {
        if (mRefreshLayout != null) mRefreshLayout.setEnableLoadMoreWhenContentNotFull(b);
    }

}

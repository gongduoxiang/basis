package com.yc.basis.base;

import androidx.annotation.NonNull;

import com.yc.basis.R;
import com.yc.basis.http.BaseHttpListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * 需要刷新的界面
 */
public abstract class BaseRefreshActivity extends BasisBaseActivity implements OnRefreshLoadMoreListener {

    protected int pageIndex = 1;//这个是页数
    protected BaseHttpListener httpListener;
    SmartRefreshLayout mRefreshLayout;
    protected boolean refresh;
    private boolean isLoad = false;//是否开启过禁止加载提示了


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
                finishRefreshAndLoadMore();
                onSuccess(refresh, o);
            }

            @Override
            public void error(String msg) {
                if (!refresh && pageIndex > 1) {
                    pageIndex--;
                }
                finishRefreshAndLoadMore();
                onFailure(msg);
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
     * 禁止加载
     */
    protected void closeLoadMore(boolean b) {
        if (mRefreshLayout != null) mRefreshLayout.setEnableLoadMore(b);
    }

    /**
     * 如果加载数据不够一页的就关闭加载
     * 是否在列表不满一页时候开启上拉加载功能
     */
    private void closeLoadMore2(boolean b) {
        // 这个方法是在最后一页，没有更多数据时调用的，会在页面底部标记没有更多数据
        if (mRefreshLayout != null)
            mRefreshLayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
        // 这个方法最重要，当在最后一页调用完上一个完成加载并标记没有更多数据的方法时，
        // 需要将refreshLayout的状态更改为还有更多数据的状态，此时就需要调用此方法，
        // 参数为false代表还有更多数据，true代表没有更多数据
        if (mRefreshLayout != null) mRefreshLayout.setNoMoreData(b);//恢复没有更多数据的原始状态 1.0.5
    }

}

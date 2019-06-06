package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.MoreAdapter;
import com.yc.jianjiao.adapter.WorthSeeingChildAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.BNotTitleRecyclerBinding;
import com.yc.jianjiao.presenter.WorthSeeingChildPresenter;
import com.yc.jianjiao.view.impl.WorthSeeingChildContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/22.
 */

public class WorthSeeingChildFrg extends BaseFragment<WorthSeeingChildPresenter, BNotTitleRecyclerBinding> implements WorthSeeingChildContract.View{


    private List<DataBean> listBean = new ArrayList<>();
    private WorthSeeingChildAdapter adapter;
    private boolean isRefresh = false;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.b_not_title_recycler;
    }

    @Override
    protected void initView(View view) {
        if (adapter == null) {
            adapter = new WorthSeeingChildAdapter(act, listBean);
        }
        setRecyclerViewGridType(mB.recyclerView, 3, 10, R.color.white);
        mB.recyclerView.setAdapter(adapter);

        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest("");
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest("");
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
        if (!isRefresh){
            isRefresh = true;
            showLoadDataing();
            mB.refreshLayout.startRefresh();
        }
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {
        List<DataBean> list = (List<DataBean>) data;
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }
}

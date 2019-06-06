package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.DramaLabelAdapter;
import com.yc.jianjiao.adapter.DramaSeriesAdapter;
import com.yc.jianjiao.adapter.WorthSeeingChildAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.FDramaSeriesBinding;
import com.yc.jianjiao.presenter.DramaSeriesPresenter;
import com.yc.jianjiao.view.impl.DramaSeriesContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/27.
 *  剧集
 */

public class DramaSeriesFrg extends BaseFragment<DramaSeriesPresenter, FDramaSeriesBinding> implements DramaSeriesContract.View{

    private List<DataBean> listBean = new ArrayList<>();
    private WorthSeeingChildAdapter adapter;

    private List<DataBean> listLabel = new ArrayList<>();
    private DramaSeriesAdapter labelAdapter;
    private String id;
    private List<String> listStr = new ArrayList<>();
    private String screenId;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        screenId = bundle.getString("screenId");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_drama_series;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.drama_series));
        if (labelAdapter == null){
            labelAdapter = new DramaSeriesAdapter(act, listLabel, screenId);
        }
        mB.listView.setAdapter(labelAdapter);
        labelAdapter.setOnClickListener(new DramaSeriesAdapter.OnClickListener() {
            @Override
            public void onClick(List<String> listStr) {
                DramaSeriesFrg.this.listStr.clear();
                DramaSeriesFrg.this.listStr = listStr;
                mB.refreshLayout.startRefresh();
            }
        });

        if (adapter == null) {
            adapter = new WorthSeeingChildAdapter(act, listBean);
        }
        setRecyclerViewGridType(mB.recyclerView, 3, 10, R.color.white);
        mB.recyclerView.setAdapter(adapter);
        mPresenter.onLabel(id);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, listStr);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1, listStr);
            }
        });
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

    @Override
    public void setLabelData(List<DataBean> list) {
        listLabel.addAll(list);
        labelAdapter.notifyDataSetChanged();
    }
}

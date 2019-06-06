package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.CardAdapter;
import com.yc.jianjiao.adapter.FileAdapter;
import com.yc.jianjiao.adapter.StarAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.presenter.StarPresenter;
import com.yc.jianjiao.databinding.FStarBinding;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.impl.StarContract;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by edison on 2019/1/20.
 *  星球
 */

public class StarFrg extends BaseFragment<StarPresenter, FStarBinding> implements StarContract.View, View.OnClickListener, BGABanner.Delegate, BGABanner.Adapter<ImageView, DataBean>{

    public static StarFrg newInstance() {
        Bundle args = new Bundle();
        StarFrg fragment = new StarFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listBean = new ArrayList<>();
    private StarAdapter adapter;
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
        return R.layout.f_star;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        mB.lySearch.setOnClickListener(this);
        mB.banner.setDelegate(this);
        if (adapter == null) {
            adapter = new StarAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  10));
        mB.recyclerView.setBackgroundColor(act.getColor(R.color.white));
        mB.recyclerView.setAdapter(adapter);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1);
                mPresenter.onBanner();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1);
            }
        });
        adapter.setOnClickListener(new StarAdapter.OnClickListener() {
            @Override
            public void onZan(int position, String id, int type) {
                mPresenter.onPraise(position, id, type);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_search:
//                UIHelper.startSearchFrg(this, 1, 2);
                UIHelper.startSearchStarFrg(this);
                break;
        }
    }
    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable DataBean model, int position) {
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + model.getImage(), itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
        DataBean bean = (DataBean) model;
        UIHelper.startHtmlAct(-1, bean.getUrl());
    }

    @Override
    public void setBanner(List<DataBean> list) {
        mB.banner.setAutoPlayAble(list.size() > 1);
        mB.banner.setData(list, new ArrayList<String>());
        mB.banner.setAdapter(StarFrg.this);
    }

    @Override
    public void onZanSuccess(int position, int type) {
        DataBean bean = listBean.get(position);
        if (type == 0){
            bean.setPraise(bean.getPraise() + 1);
        }else {
            bean.setPraise(bean.getPraise() - 1);
        }
        bean.setCirclePraise(type);
        adapter.notifyItemChanged(position);
    }

}

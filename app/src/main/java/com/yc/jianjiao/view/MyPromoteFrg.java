package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.PromoteAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseListContract;
import com.yc.jianjiao.base.BaseListPresenter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.databinding.BRecyclerBinding;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;
import com.yc.jianjiao.weight.LoadDataLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/29.
 *  我的推广
 */

public class MyPromoteFrg extends BaseFragment<BaseListPresenter, BRecyclerBinding> implements BaseListContract.View{

    private List<DataBean> listBean = new ArrayList<>();
    private PromoteAdapter adapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.b_recycler;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.my_promotion2));
        initLoad();
        if (adapter == null) {
            adapter = new PromoteAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.setBackgroundColor(act.getColor(R.color.white));
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  2));

        mB.recyclerView.setAdapter(adapter);
        showLoadDataing();
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(CloudApi.userInvitationPage, pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(CloudApi.userInvitationPage, pagerNumber += 1);
            }
        });
    }

    private void initLoad() {
        mB.swipeLoadDataLayout
                .setLoadingText(getString(R.string.custom_loading_text))
                .setLoadingTextSize(16)
                .setLoadingTextColor(R.color.colorPrimary)
                //.setLoadingViewLayoutId(R.layout.custom_loading_view) //如果设置了自定义loading视图,上面三个方法失效
                .setErrorImage(R.mipmap.home_h01)
//                .setErrorImgId(R.drawable.ic_error)
//                .setNoNetWorkImgId(R.drawable.ic_no_network)
//                .setEmptyImageVisible(true)
//                .setErrorImageVisible(true)
//                .setNoNetWorkImageVisible(true)
                .setEmptyText("暂无信息，快去推广")
//                .setErrorText(getString(R.string.custom_error_text))
//                .setNoNetWorkText(getString(R.string.custom_nonetwork_text))
                .setAllTipTextSize(16)
                .setAllTipTextColor(R.color.text_color_child)
                .setAllPageBackgroundColor(R.color.pageBackground)
                .setReloadBtnText(getString(R.string.custom_reloadBtn_text))
                .setReloadBtnTextSize(16)
                .setReloadBtnTextColor(R.color.colorPrimary)
                .setReloadBtnBackgroundResource(R.drawable.selector_btn_normal)
                .setReloadBtnVisible(false)
                .setReloadClickArea(LoadDataLayout.FULL);
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

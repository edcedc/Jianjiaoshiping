package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.DramaLabelAdapter;
import com.yc.jianjiao.adapter.MyPagerAdapter;
import com.yc.jianjiao.adapter.WorthSeeingChildAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.FWorthBinding;
import com.yc.jianjiao.presenter.WorthSeeingPresenter;
import com.yc.jianjiao.view.impl.WorthSeeingContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/22.
 * 值得看
 */

public class WorthSeeingFrg extends BaseFragment<WorthSeeingPresenter, FWorthBinding> implements WorthSeeingContract.View {

    private String id;
    private List<DataBean> listBean = new ArrayList<>();
    private WorthSeeingChildAdapter adapter;
    private List<DataBean> listLabel = new ArrayList<>();
    private DramaLabelAdapter labelAdapter;
    private List<String> listId;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_worth;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.what_is));
        if (labelAdapter == null) {
            labelAdapter = new DramaLabelAdapter(act, listLabel);
        }
        mB.rvLabel.setLayoutManager(new LinearLayoutManager(act, RecyclerView.HORIZONTAL, false));
        mB.rvLabel.setHasFixedSize(true);
        mB.rvLabel.setNestedScrollingEnabled(false);
        mB.rvLabel.setAdapter(labelAdapter);
        labelAdapter.setOnClickListener(new DramaLabelAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                DataBean bean = listLabel.get(position);
                if (bean.isSelect()) {
                    bean.setSelect(false);
                } else {
                    bean.setSelect(true);
                }
                labelAdapter.notifyDataSetChanged();

                listId = new ArrayList<>();
                for (int i = 0; i < listLabel.size(); i++) {
                    DataBean bean1 = listLabel.get(i);
                    if (bean1.isSelect()) {
                        listId.add(bean1.getId());
                    }
                }
                mB.refreshLayout.startRefresh();

            }
        });
        mPresenter.onLabel(id);

        if (adapter == null) {
            adapter = new WorthSeeingChildAdapter(act, listBean);
        }
        setRecyclerViewGridType(mB.recyclerView, 3, 10, R.color.white);
        mB.recyclerView.setAdapter(adapter);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, listId);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1, listId);
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
    public void setLabel(List<DataBean> data) {
        listLabel.addAll(data);
//        labelAdapter.setPosition(0);
        labelAdapter.notifyDataSetChanged();
//        listId = data.get(0).getId();
//        mB.refreshLayout.startRefresh();
    }
}

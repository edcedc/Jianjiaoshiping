package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.CommentAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.BNotTitleRecyclerBinding;
import com.yc.jianjiao.presenter.CommentPresenter;
import com.yc.jianjiao.view.bottom.InputBottomFrg;
import com.yc.jianjiao.view.impl.CommentContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/24.
 *  评论
 */

public class CommentFrg extends BaseFragment<CommentPresenter, BNotTitleRecyclerBinding> implements CommentContract.View{

    private List<DataBean> listBean = new ArrayList<>();
    private CommentAdapter adapter;
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
            adapter = new CommentAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.setAdapter(adapter);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1, 1);
            }
        });
        setSwipeBackEnable(false);
        adapter.setOnClickListener(new CommentAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                if (inputBottomFrg != null && !inputBottomFrg.isShowing()){
                    inputBottomFrg.show(getChildFragmentManager(), "dialog");
                    inputBottomFrg.setActivity(act);
                }
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(String text) {

                    }

                    @Override
                    public void onChildClick(String text, String circleId, String replyUserId, String byReplyUserId, String parentId) {

                    }
                });
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
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

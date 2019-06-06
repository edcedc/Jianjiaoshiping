package com.yc.jianjiao.view.bottom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.VideoCommentAdapter;
import com.yc.jianjiao.base.BaseBottomSheetFrag;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.FVideoDescBinding;
import com.yc.jianjiao.databinding.PCommentBinding;
import com.yc.jianjiao.presenter.VideoDescPresenter;
import com.yc.jianjiao.view.impl.VideoDescContract;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/3/30.
 */

public class AllCommentBottomFrg extends BaseBottomSheetFrag<VideoDescPresenter, PCommentBinding> implements VideoDescContract.View, View.OnClickListener{


    private int type;
    private String id;
    private int position;
    private List<DataBean> listBean = new ArrayList<>();
    private VideoCommentAdapter adapter;

    @Override
    protected void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        id = bundle.getString("id");
        position = bundle.getInt("position");
    }

    @Override
    public int bindLayout() {
        return R.layout.p_comment;
    }

    @Override
    public void initView(View view) {
        mB.btSubmit.setOnClickListener(this);
        mB.ivClose.setOnClickListener(this);
        view.findViewById(R.id.ly_comment).setVisibility(View.GONE);
        if (adapter == null) {
            adapter = new VideoCommentAdapter(act, listBean, type, false, false);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2));
        mB.recyclerView.setAdapter(adapter);

        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, id);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1, id);
            }
        });

        adapter.setOnClickListener(new VideoCommentAdapter.OnClickListener() {
            @Override
            public void onClick(int position, List<DataBean> list, DataBean bean) {

            }

            @Override
            public void onComment(final int position, final int i, String circle_id, String userId, String byReplyUserId, String parentId, final int commentType, final String id) {
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("id", AllCommentBottomFrg.this.id);
                bundle.putString("replyUserId", userId);
                bundle.putString("byReplyUserId", byReplyUserId);
                bundle.putString("parentId", parentId);
                inputBottomFrg.setArguments(bundle);
                inputBottomFrg.show(getChildFragmentManager(), "dialog");
                inputBottomFrg.setActivity(act);
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                    }

                    @Override
                    public void onChildClick(String text, String circleId, String replyUserId, String byReplyUserId, String parentId) {
                        mPresenter.onChildComment(position, i, text, circleId, replyUserId, byReplyUserId, parentId, type, id);
                    }
                });

            }

            @Override
            public void onZan(int position, String id, int isPraise) {
                mPresenter.onChildPraise(position, id, isPraise);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putString("id", id);
                inputBottomFrg.setArguments(bundle);
                inputBottomFrg.show(getChildFragmentManager(), "dialog");
                inputBottomFrg.setActivity(act);
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                        mPresenter.onComment(text, id);
                    }

                    @Override
                    public void onChildClick(String text, String circleId, String replyUserId, String byReplyUserId, String parentId) {

                    }
                });
                break;
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_zan:
                break;
        }
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {

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
    public void setVideoDesc(DataBean data) {

    }

    @Override
    public void setCollect(int isCollect) {

    }

    @Override
    public void onCommentSuccess(DataBean data) {
        listBean.add(0, data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildCommentSuccess(int position, int i, DataBean data) {
        DataBean bean = listBean.get(position);
        DataBean.PageCommentBean pageComment = bean.getPageComment();
        if (pageComment == null) {
            DataBean.PageCommentBean pageCommentBean = new DataBean.PageCommentBean();
            List<DataBean> list = new ArrayList<>();
            list.add(data);
            pageCommentBean.setList(list);
            bean.setPageComment(pageCommentBean);
        } else {
            List<DataBean> list = pageComment.getList();
            list.add(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildZanSuccess(int position, int isPraise) {
        DataBean bean = listBean.get(position);
        int praise = bean.getPraise();
        if (isPraise == 1) {
            isPraise = 0;
            bean.setPraise(praise -= 1);
        } else {
            isPraise = 1;
            bean.setPraise(praise += 1);
        }
        bean.setIsPraise(isPraise);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setBanner(List<DataBean> data) {

    }

    @Override
    public void onWatch() {

    }

    private Activity act;
    public void setActivity(Activity activity) {
        this.act = activity;
    }
}

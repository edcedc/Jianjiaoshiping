package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.VideoCommentAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.FDramaItemBinding;
import com.yc.jianjiao.presenter.DynamicItemPresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.impl.DynamicItemContract;
import com.yc.jianjiao.weight.CircleImageView;
import com.yc.jianjiao.weight.WithScrollListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/29.
 *  动态详情 item 详情
 */

public class DynamicItemFrg extends BaseFragment<DynamicItemPresenter, FDramaItemBinding> implements DynamicItemContract.View, View.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private VideoCommentAdapter commentAdapter;

    CircleImageView ivHead;
    AppCompatTextView tvName;
    AppCompatTextView tvTime;
    AppCompatTextView tvCommentSize;
    AppCompatTextView tvZan;
    AppCompatTextView tvContent;
    AppCompatTextView tvLook;
    WithScrollListView listView;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_drama_item;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.details));
        ivHead = view.findViewById(R.id.iv_head);
        tvName = view.findViewById(R.id.tv_name);
        tvTime = view.findViewById(R.id.tv_time);
        tvCommentSize = view.findViewById(R.id.tv_comment_size);
        tvZan = view.findViewById(R.id.tv_zan);
        tvContent = view.findViewById(R.id.tv_content);
        tvLook = view.findViewById(R.id.tv_look);
        listView = view.findViewById(R.id.listView);

        if (commentAdapter == null) {
            commentAdapter = new VideoCommentAdapter(act, listBean);
        }
//        setRecyclerViewType(mB.recyclerView);
//        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  2));
//        mB.recyclerView.setAdapter(commentAdapter);
        /*commentAdapter.setOnClickListener(new VideoCommentAdapter.OnClickListener() {
            @Override
            public void onClick(int position, List<DataBean> list) {
                *//*CommentBottomFrg frg = new CommentBottomFrg();
                Bundle bundle = new Bundle();
                String json = new Gson().toJson(list, new TypeToken<ArrayList<DataBean>>() {}.getType());
                bundle.putString("list", json);
                frg.setArguments(bundle);
                frg.show(getChildFragmentManager(), "dialog");*//*
            }

            @Override
            public void onComment(int position, String circle_id, String userId, String user_id, String parent_id) {

            }

            @Override
            public void onChildComment(int position, int i, String circle_id, String userId, String reply_user_id) {

            }

            @Override
            public void onZan(int position, String id, int isPraise) {

            }
        });*/

        showLoadDataing();
        mB.refreshLayout.startRefresh();
        mB.btSubmit.setOnClickListener(this);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1);
            }
        });

        GlideLoadingUtils.load(act, "", ivHead);
        tvName.setText("昵称");
        tvTime.setText("12-17");
        tvCommentSize.setText("123");
        tvZan.setText("123");
        tvContent.setText("副标题副标题副标题副标题副标题副标题题副标 题副标 ");
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
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_submit:

                break;
        }
    }
}

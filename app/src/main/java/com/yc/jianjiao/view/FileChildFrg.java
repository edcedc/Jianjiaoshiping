package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.FileAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.BNotTitleRecyclerBinding;
import com.yc.jianjiao.presenter.FileChildPresenter;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.view.impl.FileChildContract;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/28.
 */

public class FileChildFrg extends BaseFragment<FileChildPresenter, BNotTitleRecyclerBinding> implements FileChildContract.View{

    private boolean isRefresh = false;
    private List<DataBean> listBean = new ArrayList<>();
    private FileAdapter adapter;
    private String id;
    private LinearLayoutManager linearLayoutManager;

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
        return R.layout.b_not_title_recycler;
    }

    @Override
    protected void initView(View view) {
        if (adapter == null) {
            adapter = new FileAdapter(act, listBean);
        }
        linearLayoutManager = new LinearLayoutManager(act);
        mB.recyclerView.setLayoutManager(linearLayoutManager);
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setNestedScrollingEnabled(false);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  20));
        mB.recyclerView.setBackgroundColor(act.getColor(R.color.white));
        mB.recyclerView.setAdapter(adapter);
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
        setSwipeBackEnable(false);

        adapter.setOnClickListener(new FileAdapter.OnClickListener() {
            @Override
            public void comment(int position) {
                UIHelper.startVideoDescAct(listBean.get(position).getId(), Constants.VIDEO_TYPE);
//                CommentBottomFrg frg = new CommentBottomFrg();
//                Bundle bundle = new Bundle();
//                String json = new Gson().toJson(listBean, new TypeToken<ArrayList<DataBean>>() {}.getType());
//                bundle.putString("list", json);
//                frg.setArguments(bundle);
//                frg.show(getChildFragmentManager(), "dialog");
            }

            @Override
            public void zan(int i, String id, int isCollect) {
                mPresenter.onCollect(i, id, isCollect);
            }

            @Override
            public void share(int position) {

            }

            @Override
            public void item(int position) {

            }
        });

        mB.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals("RecyclerView2List")
                            && (position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if(!GSYVideoManager.isFullState(act)) {
                            GSYVideoManager.releaseAllVideos();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

        });
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        GSYVideoManager.onPause();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
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
    public void setCollect(int i, int isCollect) {
        DataBean bean = listBean.get(i);
        bean.setIsCollect(isCollect);
        adapter.notifyItemChanged(i);
    }
}

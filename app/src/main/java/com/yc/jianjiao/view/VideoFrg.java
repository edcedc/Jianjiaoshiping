package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.LogUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.CardAdapter;
import com.yc.jianjiao.adapter.HomeLabelAdapter;
import com.yc.jianjiao.adapter.HotLabelAdapter;
import com.yc.jianjiao.adapter.HotVideoAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FVideoBinding;
import com.yc.jianjiao.presenter.HotVideoPresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.impl.HotVideoContract;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/20.
 *  视频
 */

public class VideoFrg extends BaseFragment<HotVideoPresenter, FVideoBinding> implements HotVideoContract.View, View.OnClickListener{

    private String advUrl;

    public static VideoFrg newInstance() {
        Bundle args = new Bundle();
        VideoFrg fragment = new VideoFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private HotLabelAdapter labelAdapter;
    private List<DataBean> listLabelBean = new ArrayList<>();

    private CardAdapter cardAdapter;
    private List<DataBean> listCardBean = new ArrayList<>();
    private InfiniteScrollAdapter infiniteAdapter;

    private List<DataBean> listBean = new ArrayList<>();
    private HotVideoAdapter adapter;
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
        return R.layout.f_video;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        mB.ivImg.setOnClickListener(this);
        mB.tvMore.setOnClickListener(this);

        if (cardAdapter == null){
            cardAdapter = new CardAdapter(act, this, listCardBean);
        }
        mB.speedRecyclerView.setOrientation(DSVOrientation.HORIZONTAL);
        infiniteAdapter = InfiniteScrollAdapter.wrap(cardAdapter);
        mB.speedRecyclerView.setAdapter(infiniteAdapter);
        mB.speedRecyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        if (labelAdapter == null) {
            labelAdapter = new HotLabelAdapter(act, listLabelBean);
        }
        mB.gridView.setAdapter(labelAdapter);
        mB.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startStarAlbumFrg(VideoFrg.this, listLabelBean.get(i).getId(), 0);
            }
        });

        if (adapter == null) {
            adapter = new HotVideoAdapter(act, this, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  20));
        mB.recyclerView.setAdapter(adapter);

        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onBanner();
                mPresenter.onAdv();
                mPresenter.onLabel();
                mPresenter.onRequest(pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1);
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
        listBean.clear();
        mB.refreshLayout.finishRefreshing();
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setBanner(List<DataBean> list) {
        listCardBean.clear();
        listCardBean.addAll(list);
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLabel(List<DataBean> listBean) {
        listLabelBean.clear();
        listLabelBean.addAll(listBean);
        labelAdapter.notifyDataSetChanged();
    }

    @Override
    public void setAdv(DataBean bean) {
        advUrl = bean.getUrl();
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getImage(), mB.ivImg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_more:
                UIHelper.startHotSpecialFrg(this);
                break;
            case R.id.iv_img:
                UIHelper.startHtmlAct(-1, advUrl);
                break;
        }
    }


}

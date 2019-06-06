package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.HomeChildAdapter;
import com.yc.jianjiao.adapter.HomeChildList2Adapter;
import com.yc.jianjiao.adapter.HomeChildListAdapter;
import com.yc.jianjiao.adapter.HomeChildScreenAdapter;
import com.yc.jianjiao.adapter.HomeLabelAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FChildHomeBinding;
import com.yc.jianjiao.presenter.HomeChildPresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.impl.HomeChildContract;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by edison on 2019/1/20.
 */

public class HomeChildFrg extends BaseFragment<HomeChildPresenter, FChildHomeBinding> implements HomeChildContract.View, BGABanner.Delegate, BGABanner.Adapter<ImageView, DataBean>, View.OnClickListener{

    private HomeChildListAdapter lookAdapter;
    private List<DataBean> lookListBean = new ArrayList<>();
    private HomeChildListAdapter dramaticAdapter;
    private List<DataBean> dramaticListBean = new ArrayList<>();
    private HomeChildList2Adapter likeAdapter;
    private List<DataBean> likeListBean = new ArrayList<>();
    private HomeLabelAdapter labelAdapter;
    private List<DataBean> listLabelBean = new ArrayList<>();
    private HomeChildScreenAdapter screenAdapter;
    private List<DataBean> listscreenBean = new ArrayList<>();
    private boolean isRefresh = false;
    private String id;
    private int position;
    private String id0;
    private String id1;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        position = bundle.getInt("position");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_child_home;
    }

    @Override
    protected void initView(View view) {
        mB.etSearch.setOnClickListener(this);
        mB.tvAll.setOnClickListener(this);
        mB.tvScreen.setOnClickListener(this);
        mB.tvScreen2.setOnClickListener(this);
        if (labelAdapter == null) {
            labelAdapter = new HomeLabelAdapter(act, listLabelBean);
        }
        mB.gridView.setAdapter(labelAdapter);
        mB.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startDramaSeriesFrg(HomeChildFrg.this, id, listLabelBean.get(i).getId(), 0);
            }
        });
        if (lookAdapter == null) {
            lookAdapter = new HomeChildListAdapter(act, this, lookListBean);
        }
        mB.gvLook.setAdapter(lookAdapter);
        if (dramaticAdapter == null) {
            dramaticAdapter = new HomeChildListAdapter(act, this, dramaticListBean);
        }
        mB.gvDramatic.setAdapter(dramaticAdapter);
        mB.gvDramatic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startVideoDescAct(dramaticListBean.get(i).getId());
            }
        });
        if (likeAdapter == null){
            likeAdapter = new HomeChildList2Adapter(act, this, likeListBean);
        }
        mB.gvLike.setAdapter(likeAdapter);
        mB.gvLike.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startVideoDescAct(likeListBean.get(i).getId());
            }
        });
        mB.banner.setDelegate(this);
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onBanner(position);
                mPresenter.onLabel(id);
                mPresenter.onLookRequest(id);
                mPresenter.onDramaticRequest(id);
                mPresenter.onLikeRequest(id);
                mPresenter.onScreen(id);
            }
        });
        setSwipeBackEnable(false);
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
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {

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
        mB.banner.setAdapter(HomeChildFrg.this);
    }

    @Override
    public void setLabel(List<DataBean> listBean) {
        listLabelBean.clear();
        listLabelBean.addAll(listBean);
        labelAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLookData(List<DataBean> listBean) {
        lookListBean.clear();
        mB.refreshLayout.finishRefreshing();
        if (listBean != null && listBean.size() != 0){
            lookListBean.addAll(listBean);
            lookAdapter.notifyDataSetChanged();
            mB.lyLook.setVisibility(View.VISIBLE);
        }else {
            mB.lyLook.setVisibility(View.GONE);
        }
    }

    @Override
    public void setDramaticData(List<DataBean> listBean) {
        dramaticListBean.clear();
        mB.refreshLayout.finishRefreshing();
        if (listBean != null && listBean.size() != 0){
            dramaticListBean.addAll(listBean);
            dramaticAdapter.notifyDataSetChanged();
            mB.lyDramatic.setVisibility(View.VISIBLE);
        }else {
            mB.lyDramatic.setVisibility(View.GONE);
        }
    }

    @Override
    public void setLikeData(List<DataBean> listBean) {
        likeListBean.clear();
        mB.refreshLayout.finishRefreshing();
        if (listBean != null && listBean.size() != 0){
            likeListBean.addAll(listBean);
            likeAdapter.notifyDataSetChanged();
            mB.lyLike.setVisibility(View.VISIBLE);
        }else {
            mB.lyLike.setVisibility(View.GONE);
        }
    }

    @Override
    public void setScreenData(List<DataBean> data) {
        for (int i = 0;i < data.size();i++){
            DataBean bean = data.get(i);
            switch (i){
                case 0:
                    id0 = bean.getId();
                    mB.tvScreen.setText(bean.getName());
                    break;
                case 1:
                    id1 = bean.getId();
                    mB.tvScreen2.setText(bean.getName());
                    break;
            }
        }
    }

    public static final String allId = "99999";
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_search://搜索
                UIHelper.startSearchFrg(this, 0, 0);
                break;
            case R.id.tv_screen:
                UIHelper.startDramaSeriesFrg(this, id, id0, 0);
                break;
            case R.id.tv_screen2:
                UIHelper.startDramaSeriesFrg(this, id, id1, 0);
                break;
            case R.id.tv_all:
                UIHelper.startDramaSeriesFrg(this, id, HomeChildFrg.allId, 0);
                break;
        }
    }
}

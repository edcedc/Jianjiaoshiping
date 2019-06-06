package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.HomeChildList2Adapter;
import com.yc.jianjiao.adapter.HomeChildListAdapter;
import com.yc.jianjiao.adapter.HomeLabelAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FSelectedBinding;
import com.yc.jianjiao.presenter.SelectedPresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.impl.SelectedContract;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by edison on 2019/2/14.
 *  精选
 */

public class SelectedFrg extends BaseFragment<SelectedPresenter, FSelectedBinding> implements SelectedContract.View, View.OnClickListener, BGABanner.Delegate, BGABanner.Adapter<ImageView, DataBean>{

    private HomeChildListAdapter hotAdapter;
    private List<DataBean> hotListBean = new ArrayList<>();
    private HomeChildList2Adapter whatAdapter;
    private List<DataBean> whatListBean = new ArrayList<>();
    private HomeChildList2Adapter likeAdapter;
    private List<DataBean> likeListBean = new ArrayList<>();

    private HomeLabelAdapter labelAdapter;
    private List<DataBean> listLabelBean = new ArrayList<>();
    private boolean isRefresh = false;
    private String id;
    private int position;
    private String advId;
    private String advUrl;

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
        return R.layout.f_selected;
    }

    @Override
    protected void initView(View view) {
        mB.ivDownload.setOnClickListener(this);
        mB.ivMsg.setOnClickListener(this);
        mB.ivDotMsg.setOnClickListener(this);
        mB.etSearch.setOnClickListener(this);
        mB.ivImg.setOnClickListener(this);
        mB.tvMore.setOnClickListener(this);
        if (labelAdapter == null) {
            labelAdapter = new HomeLabelAdapter(act, listLabelBean);
        }
        mB.gridView.setAdapter(labelAdapter);
        mB.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startDramaSeriesFrg(SelectedFrg.this, id, listLabelBean.get(i).getId(), 0);
            }
        });

        if (hotAdapter == null) {
            hotAdapter = new HomeChildListAdapter(act, this, hotListBean);
        }
        mB.gvHot.setAdapter(hotAdapter);
        if (whatAdapter == null) {
            whatAdapter = new HomeChildList2Adapter(act, this, whatListBean);
        }
        mB.gvWhatIs.setAdapter(whatAdapter);
        mB.gvWhatIs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startWorthSeeingFrg(SelectedFrg.this, whatListBean.get(i).getId());
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
                mPresenter.onBanner();
                mPresenter.onLabel(id);
                mPresenter.onHotRequest(id);
                mPresenter.onWhatRequest(id);
                mPresenter.onLikeRequest(id);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_download://下载
                UIHelper.startDownloadFrg(this, 0);
                break;
            case R.id.iv_msg://消息
                UIHelper.startMsgFrg(this);
                break;
            case R.id.et_search://搜索
                UIHelper.startSearchFrg(this, 0, 1);
                break;
            case R.id.tv_more:
                UIHelper.startMoreFrg(this, id);
                break;
            case R.id.iv_img:
                UIHelper.startHtmlAct(-1, advUrl);
                break;
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
        mB.banner.setAdapter(SelectedFrg.this);
    }

    @Override
    public void setLabel(List<DataBean> listBean) {
        listLabelBean.clear();
        listLabelBean.addAll(listBean);
        labelAdapter.notifyDataSetChanged();
    }

    @Override
    public void setHotData(List<DataBean> listBean) {
        hotListBean.clear();
        mB.refreshLayout.finishRefreshing();
        if (listBean != null && listBean.size() != 0){
            hotListBean.addAll(listBean);
            hotAdapter.notifyDataSetChanged();
            mB.lyHot.setVisibility(View.VISIBLE);
        }else {
            mB.lyHot.setVisibility(View.GONE);
        }
    }

    @Override
    public void setWhatData(List<DataBean> listBean) {
        whatListBean.clear();
        mB.refreshLayout.finishRefreshing();
        if (listBean != null && listBean.size() != 0){
            whatListBean.addAll(listBean);
            whatAdapter.notifyDataSetChanged();
            mB.lyWhat.setVisibility(View.VISIBLE);
        }else {
            mB.lyWhat.setVisibility(View.GONE);
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
    public void setAdvert(DataBean bean) {
        JSONObject basicGet = User.getInstance().getBasicGet();
        if (basicGet != null){
            advUrl = basicGet.optString("handpick_adv_url");
            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + basicGet.optString("handpick_adv_image"), mB.ivImg);
        }
    }
}

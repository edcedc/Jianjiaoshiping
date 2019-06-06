package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yanzhenjie.sofia.Sofia;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.HotVideoChildAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.databinding.FStarAlbumBinding;
import com.yc.jianjiao.presenter.StarAlbumPresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.impl.SearchChildContract;
import com.yc.jianjiao.view.impl.StarAlbumContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/28.
 *  明星专辑
 */

public class StarAlbumFrg extends BaseFragment<StarAlbumPresenter, FStarAlbumBinding> implements StarAlbumContract.View{

    private List<DataBean> listBean = new ArrayList<>();
    private HotVideoChildAdapter adapter;
    private View topView;
    private String id;

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
        return R.layout.f_star_album;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.star_album));
        topView = view.findViewById(R.id.top_layout);
        view.findViewById(R.id.top_view).setVisibility(View.GONE);
        if (adapter == null){
            adapter = new HotVideoChildAdapter(act, listBean);
        }
        setRecyclerViewGridType(mB.recyclerView, 3, 10, R.color.white);
        mB.recyclerView.setAdapter(adapter);

        mB.refreshLayout.setPureScrollModeOn();
        mPresenter.onRequest(pagerNumber = 1, id);



        mB.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int headerHeight = topView.getHeight();
                int scrollDistance = Math.min(scrollY, headerHeight);
                int statusAlpha = (int) ((float) scrollDistance / (float) headerHeight * 255F);
                setAnyBarAlpha(statusAlpha);
            }
        });
        setAnyBarAlpha(0);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
    }

    private void setAnyBarAlpha(int alpha) {
        topView.getBackground().mutate().setAlpha(alpha);
        Sofia.with(act)
                .statusBarBackgroundAlpha(alpha);
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
        DataBean bean = (DataBean) data;
        mB.tvName.setText(bean.getNick());
        mB.tvContent.setText(bean.getBrief());
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getHead(), mB.ivHead);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getCover(), mB.ivImg);
        List<DataBean> list = bean.getListStarVideo();
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }

}

package com.yc.jianjiao.view;

import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.StringUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.HomeChildList2Adapter;
import com.yc.jianjiao.adapter.HomeChildListAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FSelectedTwoBinding;
import com.yc.jianjiao.presenter.SelectedTwoPresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.bottom.InputBottomFrg;
import com.yc.jianjiao.view.impl.SelectedTwoContract;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * Created by edison on 2019/2/14.
 *  精选 2
 */

public class SelectedTwoFrg extends BaseFragment<SelectedTwoPresenter, FSelectedTwoBinding> implements SelectedTwoContract.View, View.OnClickListener{

    private HomeChildListAdapter lookAdapter;
    private List<DataBean> lookListBean = new ArrayList<>();
    private HomeChildList2Adapter classAdapter;
    private List<DataBean> classListBean = new ArrayList<>();
    private HomeChildList2Adapter likeAdapter;
    private List<DataBean> likeListBean = new ArrayList<>();
    private boolean isRefresh = false;
    private String id;
    private int position;
    private DanmakuContext mContext;
    private BaseDanmakuParser mParser;
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
        return R.layout.f_selected_two;
    }

    @Override
    protected void initView(View view) {
        mB.tvDanmu.setOnClickListener(this);
        mB.btDanmu.setOnClickListener(this);
        mB.etSearch.setOnClickListener(this);
        mB.ivImg.setOnClickListener(this);

        JSONObject basicGet = User.getInstance().getBasicGet();
        if (basicGet != null){
            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + basicGet.optString("big_handsome_guy_background_image"), mB.ivBag);
            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + basicGet.optString("big_handsome_guy_base_image"), mB.ivBaseBag);
        }

        if (lookAdapter == null) {
            lookAdapter = new HomeChildListAdapter(act, this, lookListBean);
        }
        mB.gvLook.setAdapter(lookAdapter);
        if (classAdapter == null) {
            classAdapter = new HomeChildList2Adapter(act, this, classListBean);
        }
        mB.gvClass.setAdapter(classAdapter);
        mB.gvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startVideoDescAct(classListBean.get(i).getId());
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
        mB.refreshLayout.setEnableLoadmore(false);

        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onLookRequest(id);
                mPresenter.onClassRequest(id);
                mPresenter.onLikeRequest(id);
                mPresenter.onBanner();
                mPresenter.onDanWu();
                mPresenter.onLabelImg();
            }
        });
        setSwipeBackEnable(false);

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);
        if (mB.svDanmaku != null) {
            mParser = mPresenter.reateParser(this.getResources().openRawResource(R.raw.comments));
            mB.svDanmaku.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {
                    mB.svDanmaku.seekTo((long) 0); //设置循环播放
                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mB.svDanmaku.start();
                }
            });
            mB.svDanmaku.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    Log.d("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
                    BaseDanmaku latest = danmakus.last();
                    if (null != latest) {
                        Log.d("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onDanmakuLongClick(IDanmakus danmakus) {
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
                    return false;
                }
            });
            mB.svDanmaku.prepare(mParser, mContext);
            mB.svDanmaku.showFPS(false);
            mB.svDanmaku.enableDanmakuDrawingCache(true);
        }

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
            case R.id.bt_danmu:
//                addDanmaku(false, mB.tvDanmu.getText().toString().trim());
                break;
            case R.id.tv_danmu:
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                if (inputBottomFrg != null && !inputBottomFrg.isShowing()){
                    inputBottomFrg.show(getChildFragmentManager(), "dialog");
                    inputBottomFrg.setActivity(act);
                }
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        mPresenter.onScreenSave(text);
                    }

                    @Override
                    public void onChildClick(String text, String circleId, String replyUserId, String byReplyUserId, String parentId) {

                    }

                });
                break;
            case R.id.et_search://搜索
                UIHelper.startSearchFrg(this, 0, 1);
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
        List<DataBean> list = (List<DataBean>) data;
        for (int i = 0;i < list.size();i++){
            DataBean bean = list.get(i);
            if (!StringUtils.isEmpty(bean.getContent())){
                addDanmaku(false, bean.getContent());
            }
        }
    }

    @Override
    public void setBanner(List<DataBean> listBean) {
    }

    @Override
    public void setLabel(List<DataBean> listBean) {
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
    public void setClassData(List<DataBean> listBean) {
        classListBean.clear();
        mB.refreshLayout.finishRefreshing();
        if (listBean != null && listBean.size() != 0){
            classListBean.addAll(listBean);
            classAdapter.notifyDataSetChanged();
            mB.lyClass.setVisibility(View.VISIBLE);
        }else {
            mB.lyClass.setVisibility(View.GONE);
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
    public void setSendDanMu(String text) {
        addDanmaku(false, text);
    }

    @Override
    public void setAdvert(DataBean bean) {
        advUrl = bean.getUrl();
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getImage(), mB.ivImg);
    }

    @Override
    public void setLabelImg(List<DataBean> data) {
        for (int i = 0;i < data.size();i++){
            DataBean bean = data.get(i);
            String name = bean.getName();
            String image = bean.getImage();
            switch (i){
                case 0:
                    GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + image, mB.ivLook);
                    mB.tvLook.setText(name);
                    break;
                case 1:
                    GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + image, mB.ivClass);
                    mB.tvClass.setText(name);
                    break;
                case 2:
                    GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + image, mB.ivDanmu);
                    mB.tvDanmuImg.setText(name);
                    break;
                default:
                    GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + image, mB.ivLike);
                    mB.tvLike.setText(name);
                    break;
            }
        }
    }

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {
        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
        }
    };

    private void addDanmaku(boolean islive, String text) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if ((danmaku == null || mB.svDanmaku == null) && StringUtils.isEmpty(text)) {
            return;
        }
        // for(int i=0;i<100;i++){
        // }
        danmaku.text = text;
        danmaku.padding = 5;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        danmaku.setTime(mB.svDanmaku.getCurrentTime() + 1200);
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = act.getColor(R.color.orange_FF7D2D);
//        danmaku.textShadowColor = Color.WHITE;
        // danmaku.underlineColor = Color.GREEN;
//        danmaku.borderColor = act.getColor(R.color.orange_FF7D2D);
        mB.svDanmaku.addDanmaku(danmaku);
//        KeyboardUtils.hideSoftInput(mB.etDanmu);
    }
}

package com.yc.jianjiao.view;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseActivity;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FMeBinding;
import com.yc.jianjiao.event.StartPromoteInEvent;
import com.yc.jianjiao.presenter.MePresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.impl.MeContract;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

/**
 * Created by edison on 2019/1/20.
 *  我的
 */

public class MeFrg extends BaseFragment<MePresenter, FMeBinding> implements MeContract.View, View.OnClickListener{

    private String advUrl;

    public static MeFrg newInstance() {
        Bundle args = new Bundle();
        MeFrg fragment = new MeFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_me;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        mB.ivSet.setOnClickListener(this);
        mB.ivHead.setOnClickListener(this);
        mB.lyHistorical.setOnClickListener(this);
        mB.lyCache.setOnClickListener(this);
        mB.lyCollection.setOnClickListener(this);
        mB.lyPromote.setOnClickListener(this);
        mB.tvName.setOnClickListener(this);
        mB.ivImg.setOnClickListener(this);


        mB.refreshLayout.setEnableLoadmore(false);
        mB.refreshLayout.startRefresh();
        mPresenter.onAdv();
        mPresenter.onLabel(mB.gridView, MeFrg.this);

        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onInformation();
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onStartPromoteInEvent(StartPromoteInEvent event){
        UIHelper.startImmediatePromotionFrg(this, 0);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
        if (User.getInstance().isLogin()){
            setData(User.getInstance().getUserObj());
        }else {
            mB.tvName.setText(Html.fromHtml("登录后获得更多观影次数，" + "<font color='#FF7D2D'>" + "立即登录" + "</font>"));
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void onClick(View view) {
        if (!((BaseActivity)act).isLogin())return;
        switch (view.getId()){
            case R.id.iv_set:
                UIHelper.startSetFrg(this);
                break;
            case R.id.ly_historical://历史记录
                UIHelper.startHistoricalFrg(this);
                break;
            case R.id.ly_cache://下载
                UIHelper.startDownloadFrg(this, 1);
                break;
            case R.id.ly_collection://我的收藏
                UIHelper.startCollectionFrg(this);
                break;
            case R.id.tv_name:
//                UIHelper.startLoginAct();
                break;
            case R.id.ly_promote://去推广
                UIHelper.startImmediatePromotionFrg(this, 0);
                break;
            case R.id.iv_img:
                UIHelper.startHtmlAct(-1, advUrl);
                break;
        }
    }

    @Override
    public void setData(JSONObject data) {
        if (data == null)return;
        JSONObject userExtend = data.optJSONObject("userExtend");
        String nick = userExtend.optString("nick");
        mB.tvName.setText(nick.equals("null") == true ? "请设置昵称" : nick);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + userExtend.optString("head"), mB.ivHead);

        mB.tvHistorical.setText("目前历史观看过 " +
                data.optInt("countVideoHistory") +
                " 部");
        mB.tvCache.setText("目前已缓存 " +
                data.optInt("countVideoCache") +
                " 部");
        mB.tvCollection.setText("目前已收藏 " +
                data.optInt("countVideoCollect") +
                " 部");
        JSONObject promotionRules = data.optJSONObject("promotionRules");
        if (promotionRules != null){
            mB.tvToPromote.setText("已推广" +
                    userExtend.optInt("promote_number") +
                    "人/还需" +
                    promotionRules.optInt("promote_number") +
                    "人");
            mB.tvRemainingMovie.setText("" +
                    userExtend.optInt("video_frequency") +
                    "/" +
                    promotionRules.optInt("number"));
        }
    }

    @Override
    public void setAdvert(DataBean bean) {
        advUrl = bean.getUrl();
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getImage(), mB.ivImg);
    }
}

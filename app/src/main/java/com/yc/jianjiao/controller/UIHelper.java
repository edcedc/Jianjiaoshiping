package com.yc.jianjiao.controller;

import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.yc.jianjiao.MainActivity;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.view.AccountManagementFrg;
import com.yc.jianjiao.view.BindPhoneFrg;
import com.yc.jianjiao.view.CollectionFrg;
import com.yc.jianjiao.view.CommunicationGroupFrg;
import com.yc.jianjiao.view.DownloadFrg;
import com.yc.jianjiao.view.DramaSeriesFrg;
import com.yc.jianjiao.view.DynamicItemFrg;
import com.yc.jianjiao.view.FeedbackFrg;
import com.yc.jianjiao.view.HistoricalFrg;
import com.yc.jianjiao.view.HotSpecialFrg;
import com.yc.jianjiao.view.ImmediatePromotionFrg;
import com.yc.jianjiao.view.MainFrg;
import com.yc.jianjiao.view.MoreFrg;
import com.yc.jianjiao.view.MsgDescFrg;
import com.yc.jianjiao.view.MsgFrg;
import com.yc.jianjiao.view.MyPromoteFrg;
import com.yc.jianjiao.view.PromoteFrg;
import com.yc.jianjiao.view.SearchFindFrg;
import com.yc.jianjiao.view.SearchFrg;
import com.yc.jianjiao.view.SearchStarFrg;
import com.yc.jianjiao.view.SetFrg;
import com.yc.jianjiao.view.StarAlbumFrg;
import com.yc.jianjiao.view.StarFrg;
import com.yc.jianjiao.view.UpdateNicknameFrg;
import com.yc.jianjiao.view.UpdatePhoneFrg;
import com.yc.jianjiao.view.VideoIntroductionFrg;
import com.yc.jianjiao.view.WorthSeeingFrg;
import com.yc.jianjiao.view.act.DynamicDetailsAct;
import com.yc.jianjiao.view.act.HtmlAct;
import com.yc.jianjiao.view.act.LoginAct;
import com.yc.jianjiao.view.act.PromoteAct;
import com.yc.jianjiao.view.act.VideoDescAct;


/**
 * Created by Administrator on 2017/2/22.
 */

public final class UIHelper {

    private UIHelper() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void startMainAct() {
        ActivityUtils.startActivity(MainActivity.class);
    }

    public static void startLoginAct() {
        ActivityUtils.startActivity(LoginAct.class);
    }

    /**
     * Html
     */
    public static void startHtmlAct(int type, String url) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("url", url);
        ActivityUtils.startActivity(bundle, HtmlAct.class);
    }

    /**
     * 绑定手机号码
     *
     * @param root
     */
    public static void startBindPhoneFrg(BaseFragment root) {
        BindPhoneFrg frg = new BindPhoneFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 更多  正在热播
     *
     * @param root
     * @param id
     */
    public static void startMoreFrg(BaseFragment root, String id) {
        MoreFrg frg = new MoreFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment().getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 值得看
     * @param root
     * @param id
     */
    public static void startWorthSeeingFrg(BaseFragment root, String id) {
        WorthSeeingFrg frg = new WorthSeeingFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment().getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 搜索
     * @param root
     */
    public static void startSearchFrg(BaseFragment root, int starType, int type) {
        SearchFrg frg = new SearchFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        if (starType == 0) {
            ((MainFrg) root.getParentFragment().getParentFragment()).startBrotherFragment(frg);
        } else {
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);

        }
    }

    /**
     * 消息
     *
     * @param root
     */
    public static void startMsgFrg(BaseFragment root) {
        MsgFrg frg = new MsgFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment().getParentFragment()).startBrotherFragment(frg);
    }

    public static void startMsgFrg(BaseFragment root, int type) {
        MsgFrg frg = new MsgFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 消息详情
     *
     * @param root
     * @param content
     */
    public static void startMsgDescFrg(BaseFragment root, String content) {
        MsgDescFrg frg = new MsgDescFrg();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        frg.setArguments(bundle);
        ((MsgFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 视频详情
     *
     * @param id
     */
    public static void startVideoDescAct(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        ActivityUtils.startActivity(bundle, VideoDescAct.class);
    }

    public static void startVideoDescAct(String id, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("id", id);
        ActivityUtils.startActivity(bundle, VideoDescAct.class);
    }

    /**
     * 动态详情
     *
     * @param position
     * @param id
     */
    public static void startDynamicDetailsAct(int position, String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("position", position);
        ActivityUtils.startActivity(bundle, DynamicDetailsAct.class);
    }

    /**
     * 视频详细
     *
     * @param root
     */
    public static void startVideoIntroductionFrg(BaseFragment root) {
        VideoIntroductionFrg frg = new VideoIntroductionFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 剧集
     *
     * @param root
     * @param id
     */
    public static void startDramaSeriesFrg(BaseFragment root, String id, String screenId, int type) {
        DramaSeriesFrg frg = new DramaSeriesFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("screenId", screenId);
        frg.setArguments(bundle);
        if (type == 0) {
            ((MainFrg) root.getParentFragment().getParentFragment()).startBrotherFragment(frg);
        } else {
            root.start(frg);
        }
    }

    /**
     * 热门专题
     */
    public static void startHotSpecialFrg(BaseFragment root) {
        HotSpecialFrg frg = new HotSpecialFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 明星专辑
     * @param root
     * @param id
     * @param i
     */
    public static void startStarAlbumFrg(BaseFragment root, String id, int i) {
        StarAlbumFrg frg = new StarAlbumFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        frg.setArguments(bundle);
        if (i == 0) {
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        } else {
            root.start(frg);
        }
    }

    /**
     * 动态详情 item 详情
     */
    public static void startDynamicItemFrg(BaseFragment root) {
        DynamicItemFrg frg = new DynamicItemFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 设置
     *
     * @param root
     */
    public static void startSetFrg(BaseFragment root) {
        SetFrg frg = new SetFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 账号管理
     *
     * @param root
     */
    public static void startAccountManagementFrg(BaseFragment root) {
        AccountManagementFrg frg = new AccountManagementFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 修改昵称
     */
    public static void startUpdateNicknameFrg(BaseFragment root) {
        UpdateNicknameFrg frg = new UpdateNicknameFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 修改手机号码
     */
    public static void startUpdatePhoneFrg(BaseFragment root) {
        UpdatePhoneFrg frg = new UpdatePhoneFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 我要推广信息
     *
     * @param root
     */
    public static void startPromoteFrg(BaseFragment root) {
        PromoteFrg frg = new PromoteFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }
    public static void startPromoteAct() {
        ActivityUtils.startActivity(PromoteAct.class);
    }

    /**
     * 我的推广
     */
    public static void startMyPromoteFrg(BaseFragment root) {
        MyPromoteFrg frg = new MyPromoteFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 我要推广
     *
     * @param root
     */
    public static void startImmediatePromotionFrg(BaseFragment root) {
        ImmediatePromotionFrg frg = new ImmediatePromotionFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    public static void startImmediatePromotionFrg(BaseFragment root, int type) {
        ImmediatePromotionFrg frg = new ImmediatePromotionFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 意见反馈
     *
     * @param root
     */
    public static void startFeedbackFrg(BaseFragment root) {
        FeedbackFrg frg = new FeedbackFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 历史记录
     */
    public static void startHistoricalFrg(BaseFragment root) {
        HistoricalFrg frg = new HistoricalFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 我的收藏
     */
    public static void startCollectionFrg(BaseFragment root) {
        CollectionFrg frg = new CollectionFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 我的下载
     */
    public static void startDownloadFrg(BaseFragment root, int type) {
        DownloadFrg frg = new DownloadFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        switch (type) {
            case 0:
                ((MainFrg) root.getParentFragment().getParentFragment()).startBrotherFragment(frg);
                break;
            case 1:
                ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
                break;
            default:
                root.start(frg);
                break;
        }
    }

    /**
     * 火爆交流群
     */
    public static void startCommunicationGroupFrg(BaseFragment root) {
        CommunicationGroupFrg frg = new CommunicationGroupFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  搜索星球
     * @param root
     */
    public static void startSearchStarFrg(BaseFragment root) {
        SearchStarFrg frg = new SearchStarFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }
      /**
     *  搜索发现
     * @param root
     */
    public static void startSearchFindFrg(BaseFragment root) {
        SearchFindFrg frg = new SearchFindFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }


}
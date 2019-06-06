package com.yc.jianjiao.view.act;

import android.content.res.Configuration;
import android.databinding.ViewDataBinding;
import android.view.View;
import com.blankj.utilcode.util.LogUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.yc.jianjiao.base.BaseActivity;
import com.yc.jianjiao.base.BasePresenter;

/**
 * 详情模式播放页面基础类
 * Created by guoshuyu on 2017/9/14.
 */
public abstract class GSYBaseActivityDetail<P extends BasePresenter, VB extends ViewDataBinding, T extends GSYBaseVideoPlayer> extends BaseActivity<P, VB> implements VideoAllCallBack {

    protected boolean isPlay;

    protected boolean isPause;

    protected OrientationUtils orientationUtils;

    /**
     * 选择普通模式
     */
    public void initVideo() {
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, getGSYVideoPlayer());
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        if (getGSYVideoPlayer().getFullscreenButton() != null) {
            getGSYVideoPlayer().getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFull();
                    clickForFullScreen();
                }
            });
        }
    }

    /**
     * 选择builder模式
     */
    public void initVideoBuilderMode() {
        initVideo();
        getGSYVideoOptionBuilder().
                setVideoAllCallBack(this)
                .build(getGSYVideoPlayer());
    }

    public void showFull() {
        if (orientationUtils.getIsLand() != 1) {
            //直接横屏
            orientationUtils.resolveByClick();
        }
        //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
        getGSYVideoPlayer().startWindowFullscreen(GSYBaseActivityDetail.this, hideActionBarWhenFull(), hideStatusBarWhenFull());

    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getGSYVideoPlayer().getCurrentPlayer().onVideoPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGSYVideoPlayer().getCurrentPlayer().onVideoResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getGSYVideoPlayer().getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            getGSYVideoPlayer().onConfigurationChanged(this, newConfig, orientationUtils, hideActionBarWhenFull(), hideStatusBarWhenFull());
        }
    }

    @Override
    public void onStartPrepared(String url, Object... objects) {
        LogUtils.e("onStartPrepared");
    }

    @Override
    public void onPrepared(String url, Object... objects) {
        LogUtils.e("onPrepared");
        if (orientationUtils == null) {
            throw new NullPointerException("initVideo() or initVideoBuilderMode() first");
        }
        //开始播放了才能旋转和全屏
        orientationUtils.setEnable(getDetailOrientationRotateAuto() && !isAutoFullWithSize());
        isPlay = true;
    }



    @Override
    public void onClickStartIcon(String url, Object... objects) {
        LogUtils.e("onClickStartIcon");
    }

    @Override
    public void onClickStartError(String url, Object... objects) {
        LogUtils.e("onClickStartError");
    }

    @Override
    public void onClickStop(String url, Object... objects) {
        LogUtils.e("onClickStop");
    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {
        LogUtils.e("onClickStopFullscreen");
    }

    @Override
    public void onClickResume(String url, Object... objects) {
        LogUtils.e("onClickResume");
    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {
        LogUtils.e("onClickResumeFullscreen");
    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {
        LogUtils.e("onClickSeekbar");
    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {
        LogUtils.e("onClickSeekbarFullscreen");
    }

    @Override
    public void onAutoComplete(String url, Object... objects) {
        LogUtils.e("onAutoComplete");
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {
        LogUtils.e("onEnterFullscreen");
    }

    @Override
    public void onQuitFullscreen(String url, Object... objects) {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
    }

    @Override
    public void onQuitSmallWidget(String url, Object... objects) {
        LogUtils.e("onQuitSmallWidget");
    }

    @Override
    public void onEnterSmallWidget(String url, Object... objects) {
        LogUtils.e("onEnterSmallWidget");
    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {
        LogUtils.e("onTouchScreenSeekVolume");
    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {
        LogUtils.e("onTouchScreenSeekPosition");
    }

    @Override
    public void onTouchScreenSeekLight(String url, Object... objects) {
        LogUtils.e("onTouchScreenSeekLight");
    }

    @Override
    public void onPlayError(String url, Object... objects) {
        showToast("视频格式错误");
        LogUtils.e("onPlayError", url);
    }

    @Override
    public void onClickStartThumb(String url, Object... objects) {
        LogUtils.e("onClickStartThumb");
    }

    @Override
    public void onClickBlank(String url, Object... objects) {
        LogUtils.e("onClickBlank");
    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {
        LogUtils.e("onClickBlankFullscreen");
    }

    public boolean hideActionBarWhenFull() {
        return  true;
    }

    public boolean hideStatusBarWhenFull() {
        return  true;
    }

    /**
     * 播放控件
     */
    public abstract T getGSYVideoPlayer();

    /**
     * 配置播放器
     */
    public abstract GSYVideoOptionBuilder getGSYVideoOptionBuilder();

    /**
     * 点击了全屏
     */
    public abstract void clickForFullScreen();

    /**
     * 是否启动旋转横屏，true表示启动
     */
    public abstract boolean getDetailOrientationRotateAuto();

    /**
     * 是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏，注意，这时候默认旋转无效
     */
    public boolean isAutoFullWithSize() {
        return false;
    }
}

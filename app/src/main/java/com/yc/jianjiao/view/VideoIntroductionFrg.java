package com.yc.jianjiao.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.VideoIntroductionAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.FVideoIntroductionBinding;
import com.yc.jianjiao.presenter.VideoIntroductionPresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.utils.PopupWindowTool;
import com.yc.jianjiao.utils.ShareTool;
import com.yc.jianjiao.view.impl.VideoIntroductionContract;
import com.yc.jianjiao.weight.GridDividerItemDecoration;
import com.yc.jianjiao.weight.WithScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public class VideoIntroductionFrg extends BaseFragment<VideoIntroductionPresenter, FVideoIntroductionBinding> implements VideoIntroductionContract.View, View.OnClickListener{

    private OrientationUtils orientationUtils;
    private GSYVideoOptionBuilder gsyVideoOption;
    private boolean isPlay;
    private boolean isPause;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_video_introduction;
    }

    @Override
    protected void initView(View view) {
        mB.refreshLayout.setPureScrollModeOn();
        mB.fyClose.setOnClickListener(this);
        mB.fyShare.setOnClickListener(this);

        ViewGroup.LayoutParams params = mB.videoPlayer.getLayoutParams();
        params.height = ScreenUtils.getScreenHeight() / 3;
        mB.videoPlayer.setLayoutParams(params);

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(act, mB.videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        setVideoPlay("http://ws3.sinaimg.cn/large/62306eealy1fziy0cfoepj20s71hc4qp.jpg", null);

        view.findViewById(R.id.iv_close).setOnClickListener(this);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        AppCompatTextView tvFen = view.findViewById(R.id.tv_fen);
        AppCompatTextView tvContent = view.findViewById(R.id.tv_content);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        VideoIntroductionAdapter adapter = new VideoIntroductionAdapter(act, list);
        recyclerView.setLayoutManager(new GridLayoutManager(act, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridDividerItemDecoration(10, ContextCompat.getColor(act,R.color.white)));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        tvTitle.setText("标题标题标题 拷贝 2");
        tvFen.setText("9.0");
        tvContent.setText("简介");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fy_close:
            case R.id.iv_close:
                pop();
                break;
            case R.id.fy_share:
                PopupWindowTool.showDialog(act, PopupWindowTool.report, new PopupWindowTool.DialogListener() {
                    @Override
                    public void onClick() {
                        mPresenter.onReport();
                    }
                });
                break;
        }
    }

    private void setVideoPlay(String imgUrl, String videoUrl) {
        //增加封面
        ImageView imageView = new ImageView(act);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideLoadingUtils.load(act, imgUrl, imageView);
        gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(imageView)
                .setLooping(false)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
                .setCacheWithPlay(true)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        LogUtils.e("onPrepared", objects[0], objects[1]);
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        LogUtils.e("onEnterFullscreen", objects[0], objects[1]);
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        LogUtils.e("onAutoComplete", url);

                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                        LogUtils.e("onClickStartError", url);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        LogUtils.e("onQuitFullscreen", objects[0], objects[1]);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
//                        LogUtils.e(" progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration);
                    }
                })
                .build(mB.videoPlayer);
        mB.videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mB.videoPlayer.startWindowFullscreen(act, true, true);
            }
        });
    }
    @Override
    public void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    public void onResume() {
        getCurPlay().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        ShareTool.getInstance().release(act);
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null){
            orientationUtils.releaseListener();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            mB.videoPlayer.onConfigurationChanged(act, newConfig, orientationUtils, true, true);
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (mB.videoPlayer.getFullWindowPlayer() != null) {
            return mB.videoPlayer.getFullWindowPlayer();
        }
        return mB.videoPlayer;
    }
}

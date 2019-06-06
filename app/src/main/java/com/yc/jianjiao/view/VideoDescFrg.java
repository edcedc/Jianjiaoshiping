package com.yc.jianjiao.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.AnthologyAdapter;
import com.yc.jianjiao.adapter.VideoCommentAdapter;
import com.yc.jianjiao.adapter.WorthSeeingChildAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FVideoDescBinding;
import com.yc.jianjiao.event.DownloadSuccessInEvent;
import com.yc.jianjiao.presenter.VideoDescPresenter;
import com.yc.jianjiao.utils.FileSaveUtils;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.utils.PopupWindowTool;
import com.yc.jianjiao.utils.ShareTool;
import com.yc.jianjiao.view.bottom.CommentBottomFrg;
import com.yc.jianjiao.view.bottom.DownloadBottomFrg;
import com.yc.jianjiao.view.bottom.InputBottomFrg;
import com.yc.jianjiao.view.bottom.VideoIntroductionBottomFrg;
import com.yc.jianjiao.view.impl.VideoDescContract;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by edison on 2019/1/26.
 */

public class VideoDescFrg extends BaseFragment<VideoDescPresenter, FVideoDescBinding> implements VideoDescContract.View, View.OnClickListener, BGABanner.Delegate, BGABanner.Adapter<ImageView, DataBean>{

    private int type;//哪个页面进来的
    private String id;
    private String videoUrl;
    private DataBean data;
    private int isCollect;
    private int play_time;

    public static VideoDescFrg newInstance() {
        Bundle args = new Bundle();
        VideoDescFrg fragment = new VideoDescFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listComment = new ArrayList<>();
    private VideoCommentAdapter commentAdapter;
    private List<DataBean> listAnthology = new ArrayList<>();
    private AnthologyAdapter anthologyAdapter;
    private List<DataBean> listLike = new ArrayList<>();
    private WorthSeeingChildAdapter seeingChildAdapter;


    private OrientationUtils orientationUtils;
    private GSYVideoOptionBuilder gsyVideoOption;
    private boolean isPlay;
    private boolean isPause;
    private boolean isOnePlay = true;//只记录一次播放


    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        id = bundle.getString("id");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_video_desc;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        mB.fyClose.setOnClickListener(this);
        mB.fyShare.setOnClickListener(this);
        mB.tvComment.setOnClickListener(this);
        mB.tvShare.setOnClickListener(this);
        mB.tvCollection.setOnClickListener(this);
        mB.tvDownload.setOnClickListener(this);
        mB.tvTitle.setOnClickListener(this);
        mB.tvContent.setOnClickListener(this);
        mB.tvFen.setOnClickListener(this);
        EventBus.getDefault().register(this);

        ViewGroup.LayoutParams params = mB.videoPlayer.getLayoutParams();
        params.height = ScreenUtils.getScreenHeight() / 3;
        mB.videoPlayer.setLayoutParams(params);
        mB.banner.setDelegate(this);


        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(act, mB.videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        if (anthologyAdapter == null){
            anthologyAdapter = new AnthologyAdapter(act, listAnthology);
        }
        mB.rvAnthology.setLayoutManager(new LinearLayoutManager(mB
                .rvAnthology.getContext(), RecyclerView.HORIZONTAL, false));
        mB.rvAnthology.setHasFixedSize(true);
        mB.rvAnthology.setNestedScrollingEnabled(false);
        mB.rvAnthology.setAdapter(anthologyAdapter);

        if (seeingChildAdapter == null) {
            seeingChildAdapter = new WorthSeeingChildAdapter(act, listLike);
        }
        setRecyclerViewGridType(mB.rvLike, 3, 10, R.color.white);
        mB.rvLike.setAdapter(seeingChildAdapter);

        if (commentAdapter == null) {
            commentAdapter = new VideoCommentAdapter(act, listComment, 1, false);
        }
//        setRecyclerViewType(mB.rvComment);
        mB.rvComment.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  2));
        mB.rvComment.setAdapter(commentAdapter);
        commentAdapter.setOnClickListener(new VideoCommentAdapter.OnClickListener() {
            @Override
            public void onClick(int position, List<DataBean> list, DataBean bean) {

            }

            @Override
            public void onComment(final int position, final int i, String circle_id, String userId, String byReplyUserId, String parentId, final int commentType, final String id) {
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("id", circle_id);
                bundle.putString("replyUserId", userId);
                bundle.putString("byReplyUserId", byReplyUserId);
                bundle.putString("parentId", parentId);
                inputBottomFrg.setArguments(bundle);
                inputBottomFrg.show(getChildFragmentManager(), "dialog");
                inputBottomFrg.setActivity(act);
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                    }

                    @Override
                    public void onChildClick(String text, String circleId, String replyUserId, String byReplyUserId, String parentId) {
                        mPresenter.onChildComment(position, i, text, circleId, replyUserId, byReplyUserId, parentId, commentType, id);
                    }
                });
            }

            @Override
            public void onZan(int position, String id, int isPraise) {
                mPresenter.onChildPraise(position, id, isPraise);
            }
        });


        showLoadDataing();
        mB.refreshLayout.startRefresh();
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onAddPlayback(id);
                mPresenter.onBanner(id);
                mPresenter.onVideoDesc(id);
                mPresenter.onRequest(pagerNumber = 1, id);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1, id);
            }
        });

    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listComment.size(), totalRow, mB.refreshLayout);
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
            listComment.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listComment.addAll(list);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fy_close:
                act.finish();
                break;
            case R.id.fy_share:
                PopupWindowTool.showDialog(act, PopupWindowTool.report, new PopupWindowTool.DialogListener() {
                    @Override
                    public void onClick() {
                        mPresenter.onSaveComplaints(id);
                    }
                });
                break;
            case R.id.tv_download:
                if (!StringUtils.isEmpty(videoUrl)){
                    FileSaveUtils.saveVideo(act, videoUrl, mB.tvTitle.getText().toString());
                }
                break;
            case R.id.tv_title:
            case R.id.tv_content:
//                UIHelper.startVideoIntroductionFrg(this);
                break;
            case R.id.tv_fen:
                onFen();
                break;
            case R.id.tv_collection:
                mPresenter.onCollect(id, isCollect);
                break;
            case R.id.tv_comment:
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putString("id", id);
                inputBottomFrg.setArguments(bundle);
                inputBottomFrg.show(getChildFragmentManager(), "dialog");
                inputBottomFrg.setActivity(act);
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                        mPresenter.onComment(text,  id);
                    }

                    @Override
                    public void onChildClick(String text,String circleId, String replyUserId, String byReplyUserId, String parentId) {

                    }
                });
                break;
        }
    }

    private VideoIntroductionBottomFrg videoIntroductionBottomFrg;
    private void onFen() {
        if (videoIntroductionBottomFrg == null){
            videoIntroductionBottomFrg = new VideoIntroductionBottomFrg();
        }
        Bundle bundle = new Bundle();
        bundle.putString("bean", new Gson().toJson(data));
        videoIntroductionBottomFrg.setArguments(bundle);
        videoIntroductionBottomFrg.show(getChildFragmentManager(), "dialog");
    }

    private void setVideoPlay(String imgUrl, String videoUrl) {
        String ss = "api/";
        String s = (String) videoUrl;
        if (s.contains(ss)){
            s = s.replace(ss, "");
        }
        this.videoUrl = s;
        LogUtils.e(videoUrl);
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
                .setUrl(s)
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

    @Subscribe
    public void onMainDownloadSuccessInEvent(DownloadSuccessInEvent event){
        if (event.name.equals(mB.tvTitle.getText().toString())){
            mB.tvDownload.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.home_k16, null), null, null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Override
    public void setVideoDesc(DataBean data) {
        this.data = data;
        mB.tvTitle.setText(data.getTitle());
        mB.tvFen.setText(data.getMark() + "");
        mB.tvContent.setText(data.getBrief());
        mB.tvLabel.setText(data.getLabel_name());
        isCollect = data.getIsCollect();
        setCollect(isCollect);

        setVideoPlay(CloudApi.SERVLET_URL + data.getVideo_url(), CloudApi.SERVLET_URL + data.getVideo_url());

        List<DataBean> listVideoSeries = data.getListVideoSeries();//选集
        if (listVideoSeries != null && listVideoSeries.size() != 0){
            mB.tvAnthology.setVisibility(View.VISIBLE);
            mB.rvAnthology.setVisibility(View.VISIBLE);
            listAnthology.addAll(listVideoSeries);
            anthologyAdapter.setPosition(0);
            anthologyAdapter.setClickListener(new AnthologyAdapter.onClickListener() {
                @Override
                public void onClick(int position) {
                    anthologyAdapter.setPosition(position);
                    anthologyAdapter.notifyDataSetChanged();

                    DataBean bean = listAnthology.get(position);
                    setVideoPlay(CloudApi.SERVLET_URL + bean.getVideo_url(), CloudApi.SERVLET_URL + bean.getVideo_url());
                    mB.videoPlayer.startPlayLogic();
                }
            });

            DataBean bean = listVideoSeries.get(0);
            setVideoPlay(CloudApi.SERVLET_URL + bean.getVideo_url(), CloudApi.SERVLET_URL + bean.getVideo_url());
        }
        anthologyAdapter.notifyDataSetChanged();

        List<DataBean> listVideo = data.getListVideo();//猜你喜欢
        if (listVideo != null && listVideo.size() != 0){
            listLike.addAll(listVideo);
        }
        seeingChildAdapter.notifyDataSetChanged();
    }

    @Override
    public void setCollect(int isCollect) {
        this.isCollect = isCollect;
        if (this.isCollect == 0){
            mB.tvCollection.setText("收藏");
            mB.tvCollection.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.home_a09, null), null, null);
        }else {
            mB.tvCollection.setText("取消收藏");
            mB.tvCollection.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.add_a04, null), null, null);
        }
    }

    @Override
    public void onCommentSuccess(DataBean data) {
        listComment.add(data);
        commentAdapter.notifyDataSetChanged();
//        mB.scrollView.post(new Runnable() {
//
//            @Override
//            public void run() {
//                mB.scrollView.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });
    }

    @Override
    public void onChildCommentSuccess(int position, int i, DataBean data) {
        DataBean bean = listComment.get(position);
        DataBean.PageCommentBean pageComment = bean.getPageComment();
        if (pageComment == null){
            DataBean.PageCommentBean pageCommentBean = new DataBean.PageCommentBean();
            List<DataBean> list = new ArrayList<>();
            list.add(data);
            pageCommentBean.setList(list);
            bean.setPageComment(pageCommentBean);
        }else {
            List<DataBean> list = pageComment.getList();
            list.add(data);
        }
        commentAdapter.notifyDataSetChanged();

//        mB.scrollView.post(new Runnable() {
//
//            @Override
//            public void run() {
//                mB.scrollView.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });
    }

    @Override
    public void onChildZanSuccess(int position, int isPraise) {
        DataBean bean = listComment.get(position);
        int praise = bean.getPraise();
        if (isPraise == 1){
            isPraise = 0;
            bean.setPraise(praise -= 1);
        }else {
            isPraise = 1;
            bean.setPraise(praise += 1);
        }
        bean.setIsPraise(isPraise);
        commentAdapter.notifyDataSetChanged();
    }

    private int countNumber = 0;
    @Override
    public void setBanner(List<DataBean> list) {

        if (list.size() != 0){
//            mB.banner.setAutoPlayAble(data.size() > 1);
//            mB.banner.setData(data, new ArrayList<String>());
//            mB.banner.setAdapter(VideoDescFrg.this);
            mB.tvVideoBill.setVisibility(View.VISIBLE);
            timeCount(countNumber, list);
        }else {
            mB.tvVideoBill.setVisibility(View.GONE);
            setVideoPlay(CloudApi.SERVLET_URL + data.getVideo_url(), CloudApi.SERVLET_URL + data.getVideo_url());
        }
    }

    @Override
    public void onWatch() {

    }

    private void timeCount(int timeNumber, final List<DataBean> data){
        if (timeNumber == data.size() - 1){
            LogUtils.e("ok");
            return;
        }
        play_time = data.get(timeNumber).getPlay_time();
//        setVideoPlay(CloudApi.SERVLET_URL + data.getVideo_url(), CloudApi.SERVLET_URL + data.getVideo_url());

        timeNumber++;

    }

    private CountDownTimer downTimer = new CountDownTimer(play_time * 1000, 1000) {
        @Override
        public void onTick(long l) {
            LogUtils.e((play_time * 1000) / 1000);
        }

        @Override
        public void onFinish() {

        }
    };

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable DataBean model, int position) {
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + model.getVideo_url(), itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
        DataBean bean = (DataBean) model;
        LogUtils.e(bean.getPlay_time());
    }
}

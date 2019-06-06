package com.yc.jianjiao.view.act;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.video.GSYSampleADVideoPlayer;
import com.shuyu.gsyvideoplayer.video.ListGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.AnthologyAdapter;
import com.yc.jianjiao.adapter.VideoCommentAdapter;
import com.yc.jianjiao.adapter.WorthSeeingChildAdapter;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FVideoDescBinding;
import com.yc.jianjiao.event.DownloadSuccessInEvent;
import com.yc.jianjiao.event.StartPromoteInEvent;
import com.yc.jianjiao.event.ZanSuccessInEvent;
import com.yc.jianjiao.presenter.VideoDescPresenter;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.utils.FileSaveUtils;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.utils.PopupWindowTool;
import com.yc.jianjiao.utils.ShareTool;
import com.yc.jianjiao.view.MainFrg;
import com.yc.jianjiao.view.PromoteFrg;
import com.yc.jianjiao.view.VideoDescFrg;
import com.yc.jianjiao.view.bottom.AllCommentBottomFrg;
import com.yc.jianjiao.view.bottom.CommentBottomFrg;
import com.yc.jianjiao.view.bottom.InputBottomFrg;
import com.yc.jianjiao.view.bottom.VideoIntroductionBottomFrg;
import com.yc.jianjiao.view.impl.VideoDescContract;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/26.
 */

public class VideoDescAct extends GSYBaseActivityDetail<VideoDescPresenter, FVideoDescBinding, ListGSYVideoPlayer> implements VideoDescContract.View, View.OnClickListener {

    private int type;
    private String id;
    private String videoUrl;
    private DataBean data;
    private int isCollect;
    private int play_time;
    private boolean isPlay = true;

    private List<DataBean> listComment = new ArrayList<>();
    private VideoCommentAdapter commentAdapter;
    private List<DataBean> listAnthology = new ArrayList<>();
    private AnthologyAdapter anthologyAdapter;
    private List<DataBean> listLike = new ArrayList<>();
    private WorthSeeingChildAdapter seeingChildAdapter;
    private int selectPosition = 0;
    private ArrayList<GSYSampleADVideoPlayer.GSYADVideoModel> urls = new ArrayList<>();

    @Override
    protected void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_video_desc;
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        type = bundle.getInt("type");
    }

    @Override
    protected void initView() {
        setSofia(true);
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
        //设置视频屏幕的三分之一
        ViewGroup.LayoutParams params = mB.fyVideo.getLayoutParams();
        params.height = ScreenUtils.getScreenHeight() / 3;
        mB.fyVideo.setLayoutParams(params);

        if (anthologyAdapter == null) {
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
            commentAdapter = new VideoCommentAdapter(act,  listComment, Constants.VIDEO_TYPE, true);
        }
        setRecyclerViewType(mB.rvComment);
        mB.rvComment.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  2));
        mB.rvComment.setAdapter(commentAdapter);
        commentAdapter.setOnClickListener(new VideoCommentAdapter.OnClickListener() {
            @Override
            public void onClick(int position, List<DataBean> list, DataBean bean) {
                CommentBottomFrg frg = new CommentBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putInt("type", Constants.VIDEO_TYPE);
                bundle.putString("id", id);
                bundle.putString("bean", new Gson().toJson(bean));
                frg.setArguments(bundle);
                frg.setActivity(act);
                frg.show(getSupportFragmentManager(), "dialog");
            }

            @Override
            public void onComment(final int position, final int i, String circle_id, String userId, String byReplyUserId, String parentId, final int type, final String id) {
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("id", circle_id);
                bundle.putString("replyUserId", userId);
                bundle.putString("byReplyUserId", byReplyUserId);
                bundle.putString("parentId", parentId);
                inputBottomFrg.setArguments(bundle);
                inputBottomFrg.show(getSupportFragmentManager(), "dialog");
                inputBottomFrg.setActivity(act);
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                    }

                    @Override
                    public void onChildClick(String text, String video_id, String replyUserId, String byReplyUserId, String parentId) {
                        mPresenter.onChildComment(position, i, text, video_id, replyUserId, byReplyUserId, parentId, type, id);
                    }
                });
            }



            @Override
            public void onZan(int position, String id, int isPraise) {
                mPresenter.onChildPraise(position, id, isPraise);
            }
        });


        showLoadDataing();
        mPresenter.onBanner(id);
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, id);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1, id);
            }
        });
        //普通模式
        initVideo();
        resolveNormalVideoUI();
        mB.videoPlayer.setIsTouchWiget(true);
        //关闭自动旋转
        mB.videoPlayer.setRotateViewAuto(false);
        mB.videoPlayer.setLockLand(false);
        mB.videoPlayer.setShowFullAnimation(false);
        mB.videoPlayer.setNeedLockFull(true);
        mB.videoPlayer.setVideoAllCallBack(this);
        mB.videoPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });



    }

    @Subscribe
    public void onMainZanInEvent(ZanSuccessInEvent event){
        onChildZanSuccess(event.position, event.isPraise);
    }

    @Subscribe
    public void onMainDownloadSuccessInEvent(DownloadSuccessInEvent event){
        if (event.name.equals(mB.tvTitle.getText().toString())){
            mB.tvDownload.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.home_k16, null), null, null);
        }
    }

    @Override
    public ListGSYVideoPlayer getGSYVideoPlayer() {
        return mB.videoPlayer;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        return null;
    }

    @Override
    public void clickForFullScreen() {

    }

    @Override
    public boolean getDetailOrientationRotateAuto() {
        return false;
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {
        super.onEnterFullscreen(url, objects);
        //隐藏调全屏对象的返回按键
        GSYVideoPlayer gsyVideoPlayer = (GSYVideoPlayer) objects[1];
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ShareTool.getInstance().release(act);
    }

    private void resolveNormalVideoUI() {
        //增加title
        mB.videoPlayer.getTitleTextView().setVisibility(View.GONE);
        mB.videoPlayer.getBackButton().setVisibility(View.GONE);
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
//        setListViewHeight(mB.rvComment);
        commentAdapter.notifyDataSetChanged();


        if (type == Constants.VIDEO_TYPE){
            AllCommentBottomFrg frg = new AllCommentBottomFrg();
            Bundle bundle = new Bundle();
            bundle.putInt("type", Constants.VIDEO_TYPE);
            bundle.putString("id", id);
            frg.setArguments(bundle);
            frg.setActivity(act);
            frg.show(getSupportFragmentManager(), "dialog");
        }
    }

    /**
     * 解决ScrollView嵌套listView导致Listview无法正常显示的问题
     */
    public static void setListViewHeight(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for(int i = 0;i< listAdapter.getCount();i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                if (!StringUtils.isEmpty(videoUrl)) {
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
                inputBottomFrg.show(getSupportFragmentManager(), "dialog");
                inputBottomFrg.setActivity(act);
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                        mPresenter.onComment(text, id);
                    }

                    @Override
                    public void onChildClick(String text, String circleId, String replyUserId, String byReplyUserId, String parentId) {

                    }
                });
                break;
        }
    }

    private VideoIntroductionBottomFrg videoIntroductionBottomFrg;

    private void onFen() {
        if (videoIntroductionBottomFrg == null) {
            videoIntroductionBottomFrg = new VideoIntroductionBottomFrg();
        }
        Bundle bundle = new Bundle();
        bundle.putString("bean", new Gson().toJson(data));
        videoIntroductionBottomFrg.setArguments(bundle);
        videoIntroductionBottomFrg.show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public void setVideoDesc(DataBean data) {
        this.data = data;
        mB.tvTitle.setText(data.getTitle());
        mB.tvFen.setText(data.getMark() + "");
        mB.tvContent.setText(data.getVideo_desc());
        mB.tvLabel.setText(data.getLabel_name());
        isCollect = data.getIsCollect();
        setCollect(isCollect);
        setVideoPlay(CloudApi.SERVLET_URL + data.getVideo_url(), CloudApi.SERVLET_URL + data.getVideo_url());
        mB.videoPlayer.setAdUp(urls, true, 0);
        mB.videoPlayer.startPlayLogic();

        List<DataBean> listVideoSeries = data.getListVideoSeries();//选集
        if (listVideoSeries != null && listVideoSeries.size() != 0) {
            mB.tvAnthology.setVisibility(View.VISIBLE);
            mB.rvAnthology.setVisibility(View.VISIBLE);
            listAnthology.addAll(listVideoSeries);
            anthologyAdapter.setPosition(0);
            anthologyAdapter.setClickListener(new AnthologyAdapter.onClickListener() {
                @Override
                public void onClick(int position) {
                    if (selectPosition == position)return;
                    selectPosition = position;
                    anthologyAdapter.setPosition(position);
                    anthologyAdapter.notifyDataSetChanged();

//                    urls.clear();
                    DataBean bean = listAnthology.get(position);
                    setVideoPlay(CloudApi.SERVLET_URL + bean.getVideo_url(), CloudApi.SERVLET_URL + bean.getVideo_url());
                    mB.videoPlayer.setAdUp(urls, true, urls.size() - 1);
                    mB.videoPlayer.startPlayLogic();
                }
            });

//            DataBean bean = listVideoSeries.get(0);
//            setVideoPlay(CloudApi.SERVLET_URL + bean.getVideo_url(), CloudApi.SERVLET_URL + bean.getVideo_url());
        }
        anthologyAdapter.notifyDataSetChanged();

        List<DataBean> listVideo = data.getListVideo();//猜你喜欢
        if (listVideo != null && listVideo.size() != 0) {
            listLike.addAll(listVideo);
        }
        seeingChildAdapter.notifyDataSetChanged();
    }

    /**
     * 指定播放url
     */
    private void setVideoPlay(String videoUrl, String imgUrl) {

        String ss = "api/";
        String s = (String) videoUrl;
        if (s.contains(ss)){
            s = s.replace(ss, "");
        }
        this.videoUrl = s;
        LogUtils.e(s);
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideLoadingUtils.load(act, imgUrl, imageView);
        mB.videoPlayer.setThumbImageView(imageView);

        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel(s,
                "", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL));

//        mB.videoPlayer.playNext();
    }

    @Override
    public void setCollect(int isCollect) {
        this.isCollect = isCollect;
        if (this.isCollect == 0) {
            mB.tvCollection.setText("收藏");
            mB.tvCollection.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.home_a09, null), null, null);
        } else {
            mB.tvCollection.setText("取消收藏");
            mB.tvCollection.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.add_a04, null), null, null);
        }
    }

    @Override
    public void onCommentSuccess(DataBean data) {
        listComment.add(0, data);
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
        if (pageComment == null) {
            DataBean.PageCommentBean pageCommentBean = new DataBean.PageCommentBean();
            List<DataBean> list = new ArrayList<>();
            list.add(data);
            pageCommentBean.setList(list);
            bean.setPageComment(pageCommentBean);
        } else {
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
        if (isPraise == 1) {
            isPraise = 0;
            bean.setPraise(praise -= 1);
        } else {
            isPraise = 1;
            bean.setPraise(praise += 1);
        }
        bean.setIsPraise(isPraise);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void setBanner(List<DataBean> list) {
        if (list.size() != 0) {
            for (DataBean bean : list){
                String video_url = CloudApi.SERVLET_URL + bean.getVideo_url();
                String ss = "api/";
                String s = (String) video_url;
                if (s.contains(ss)){
                    s = s.replace(ss, "");
                }
                urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel(s,
                        "", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD));
            }
        }
        mPresenter.onVideoDesc(id);
    }

    @Override
    public void onWatch() {
        getGSYVideoPlayer().getCurrentPlayer().onVideoPause();
        mB.fyPop.setVisibility(View.VISIBLE);
        mB.btPopSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlay = false;
                UIHelper.startPromoteAct();
                mB.fyPop.setVisibility(View.GONE);
            }
        });
        mB.ivPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onClickResume(String url, Object... objects) {
        super.onClickResume(url, objects);
        for (GSYSampleADVideoPlayer.GSYADVideoModel model : urls){
            String modelUrl = model.getUrl();
            if (modelUrl.equals(url) && model.getType() == GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL){
                mPresenter.onAddPlayback(id);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isPlay){
            getGSYVideoPlayer().getCurrentPlayer().onVideoPause();
        }
    }

    @Override
    public void onPrepared(String url, Object... objects) {
        super.onPrepared(url, objects);
        for (GSYSampleADVideoPlayer.GSYADVideoModel model : urls){
            String modelUrl = model.getUrl();
            if (modelUrl.equals(url) && model.getType() == GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL){
                mPresenter.onAddPlayback(id);
            }
        }
    }

}

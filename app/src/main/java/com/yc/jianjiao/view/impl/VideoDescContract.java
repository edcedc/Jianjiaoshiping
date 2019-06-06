package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.view.act.GSYBaseActivityDetail;

import java.util.List;

/**
 * Created by edison on 2019/1/26.
 */

public interface VideoDescContract {

    interface View extends IBaseListView {

        void setVideoDesc(DataBean data);

        void setCollect(int isCollect);

        void onCommentSuccess(DataBean data);

        void onChildCommentSuccess(int position, int i, DataBean data);

        void onChildZanSuccess(int position, int isPraise);

        void setBanner(List<DataBean> data);

        void onWatch();
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber, String id);

        public abstract void onVideoDesc(String id);

        public abstract void onSaveComplaints(String id);

        public abstract void onCollect(String id, int isCollect);

        public abstract void onComment(String text, String id);

        public abstract void onChildComment(int position, int i, String text, String circleId, String replyUserId, String byReplyUserId, String parentId, int commentType, String id);

        public abstract void onChildPraise(int position, String id, int isPraise);

        public abstract void onBanner(String id);

        public abstract void onAddPlayback(String id);
    }


}

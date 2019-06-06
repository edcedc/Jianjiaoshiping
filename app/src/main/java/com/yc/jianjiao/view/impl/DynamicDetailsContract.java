package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

/**
 * Created by edison on 2019/1/29.
 */

public interface DynamicDetailsContract {

    interface View extends IBaseListView {

        void onZanSuccess(int type);

        void onCommentSuccess(DataBean data);

        void setCircleDetail(DataBean data);

        void onChildZanSuccess(int position, int isPraise);

        void onChildCommentSuccess(int position, int i, DataBean data);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber, String id);

        public abstract void onComment(String text, String id);

        public abstract void onPraise(int position, String id, int i);

        public abstract void onCircleDetail(String id);

        public abstract void onChildPraise(int position, String id, int isPraise);

        public abstract void onChildComment(int position, int i, String text, String circleId, String replyUserId, String byReplyUserId, String parentId, int commentType, String id);
    }

}

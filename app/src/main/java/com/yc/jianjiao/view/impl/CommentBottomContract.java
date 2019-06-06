package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

/**
 * Created by edison on 2019/3/21.
 */

public interface CommentBottomContract {
    interface View extends IBaseListView {


        void onChildZanSuccess(int position, int isPraise);

        void onChildCommentSuccess(int position, int i, DataBean data);

        void onZanSuccess(int position, int isPraise);

        void onCommentSuccess(int i, DataBean data);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber, int type, String id);

        public abstract void onChildPraise(int position, String id, int isPraise, int type);

        public abstract void onChildComment(int position, int i, String text, String circlevideoId, String replyUserId, String byReplyUserId, String parentId, int type, String id);

        public abstract void onPraise(int position, String id, int isPraise, int type);

        public abstract void onComment(int i, String text, String circleId, String replyUserId, String byReplyUserId, String parentId, int type, String id);
    }
}

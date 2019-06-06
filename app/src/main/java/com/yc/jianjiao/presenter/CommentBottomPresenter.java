package com.yc.jianjiao.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.lzy.okgo.model.Response;
import com.yc.jianjiao.bean.BaseListBean;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.callback.Code;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.view.impl.CommentBottomContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/3/21.
 */

public class CommentBottomPresenter extends CommentBottomContract.Presenter {
    @Override
    public void onRequest(int pagerNumber, final int type, String id) {
        CloudApi.commentPageLevel(pagerNumber, type, id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<BaseListBean<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<BaseListBean<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            BaseListBean<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                List<DataBean> list = data.getList();
                                if (list != null){
                                    for (DataBean bean : list){
                                        String level_content = bean.getLevel_content();
                                        if (type == Constants.VIDEO_TYPE && !StringUtils.isEmpty(level_content)){
                                            DataBean.PageCommentBean pageCommentBean = new DataBean.PageCommentBean();
                                            List<DataBean> listVideo = new ArrayList<>();
                                            DataBean dataBean = new DataBean();
                                            dataBean.setNick(bean.getNick());
                                            dataBean.setReply_user_id(bean.getReply_user_id());
                                            dataBean.setReply_nick(bean.getReply_nick());
                                            dataBean.setEmoji_content(bean.getLevel_emoji_content());
                                            dataBean.setContent(bean.getLevel_content());
                                            dataBean.setUser_id(bean.getUser_id());
                                            listVideo.add(dataBean);
                                            pageCommentBean.setList(listVideo);
                                            bean.setPageComment(pageCommentBean);
                                        }else if (type == Constants.CIRCLE_TYPE && !StringUtils.isEmpty(level_content)){
                                            DataBean.CircleCommentBean circleCommentBean = new DataBean.CircleCommentBean();
                                            List<DataBean> listCircle = new ArrayList<>();
                                            DataBean dataBean = new DataBean();
                                            dataBean.setNick(bean.getNick());
                                            dataBean.setReply_user_id(bean.getReply_user_id());
                                            dataBean.setReply_nick(bean.getReply_nick());
                                            dataBean.setEmoji_content(bean.getLevel_emoji_content());
                                            dataBean.setContent(bean.getLevel_content());
                                            dataBean.setUser_id(bean.getUser_id());
                                            listCircle.add(dataBean);
                                            circleCommentBean.setList(listCircle);
                                            bean.setCircleComment(circleCommentBean);
                                        }
                                    }
                                    mView.setData(list);
                                    mView.setRefreshLayoutMode(data.getTotalRow());
                                    mView.hideLoading();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onChildPraise(final int position, String id, final int isPraise, int type) {
        if (type == Constants.CIRCLE_TYPE){
            CloudApi.commentSavePraise(id, isPraise)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                mView.onChildZanSuccess(position, isPraise);
                            }
//                        showToast(baseResponseBeanResponse.body().desc);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }else {
            CloudApi.videoCommentSavePraise(id, isPraise)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                                mView.onChildZanSuccess(position, isPraise);
                            }
//                        showToast(baseResponseBeanResponse.body().desc);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });

        }
    }

    @Override
    public void onChildComment(final int position, final int i, String text, String circleId, String replyUserId, String byReplyUserId, String parentId, int type, String id) {
        if (type == Constants.CIRCLE_TYPE){
            CloudApi.commentSaveLevel(circleId, text, replyUserId, byReplyUserId, parentId, 0, id)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                DataBean data = baseResponseBeanResponse.body().data;
                                if (data != null){
                                    mView.onChildCommentSuccess(position, i, data);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }else {
            CloudApi.commentSaveAWCLevel(circleId, text, replyUserId, byReplyUserId, parentId, 0, id)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                                DataBean data = baseResponseBeanResponse.body().data;
                                if (data != null) {
                                    mView.onChildCommentSuccess(position, i, data);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }

    }

    @Override
    public void onPraise(final int position, String id, final int isPraise, int type) {
        if (type == Constants.CIRCLE_TYPE){
            CloudApi.commentSavePraise(id, isPraise)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                mView.onZanSuccess(position, isPraise);
                            }
//                        showToast(baseResponseBeanResponse.body().desc);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }else {
            CloudApi.videoCommentSavePraise(id, isPraise)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                                mView.onZanSuccess(position, isPraise);
                            }
//                        showToast(baseResponseBeanResponse.body().desc);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }

    }

    @Override
    public void onComment(final int i, String text, String circleId, String replyUserId, String byReplyUserId, String parentId, int type, String id) {
        if (type == Constants.CIRCLE_TYPE){
            CloudApi.commentSaveLevel(circleId, text, replyUserId, byReplyUserId, parentId, 1, id)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                DataBean data = baseResponseBeanResponse.body().data;
                                if (data != null){
                                    mView.onCommentSuccess(i, data);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }else {
            CloudApi.commentSaveAWCLevel(circleId, text, replyUserId, byReplyUserId, parentId, 1, id)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                                DataBean data = baseResponseBeanResponse.body().data;
                                if (data != null) {
                                    mView.onCommentSuccess(i, data);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }
    }
}

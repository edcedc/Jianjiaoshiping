package com.yc.jianjiao.presenter;

import android.os.Handler;

import com.lzy.okgo.model.Response;
import com.yc.jianjiao.bean.BaseListBean;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.callback.Code;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.view.impl.MoreContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/22.
 */

public class MorePresenter extends MoreContract.Presenter{
    @Override
    public void onRequest(int pagerNumber, String id) {
        CloudApi.videoPageLabelType(id, 5)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
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
                                if (list != null && list.size() != 0){
                                    mView.setData(list);
                                    mView.hideLoading();
                                    mView.setRefreshLayoutMode(data.getTotalRow());
                                }else {
                                    mView.showLoadEmpty();
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
}

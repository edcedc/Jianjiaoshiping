package com.yc.jianjiao.presenter;

import com.lzy.okgo.model.Response;
import com.yc.jianjiao.bean.BaseListBean;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.callback.Code;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.view.impl.DramaSeriesContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/27.
 */

public class DramaSeriesPresenter extends DramaSeriesContract.Presenter{
    @Override
    public void onRequest(int pagerNumber, List<String> listId) {
        CloudApi.videoPageSearch(pagerNumber, listId, null)
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
                                    mView.setData(list);
                                    mView.setRefreshLayoutMode(data.getTotalRow());
                                }
                            }
                        }else {
                            mView.setData(new ArrayList<>());
                            showToast(baseResponseBeanResponse.body().desc);
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

    @Override
    public void onLabel(String id) {
        CloudApi.labelListScreen(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoadDataing();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<List<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<List<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            List<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null && data.size() != 0){

                                for (int i = 0;i < data.size();i++) {
                                    DataBean bean1 = data.get(i);
                                    DataBean bean = new DataBean();
                                    List<DataBean> listSubclassification1 = bean1.getListSubclassification();
                                    StringBuilder sb = new StringBuilder();
                                    for (int j = 0;j < listSubclassification1.size();j++){
                                        bean.setName(bean1.getName());
                                        sb.append(listSubclassification1.get(j).getId()).append(",");
                                        bean.setId(sb.toString());
                                    }
                                    listSubclassification1.add(0, bean);
                                }
                                mView.setLabelData(data);
                            }else {
                                mView.showLoadEmpty();
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

package com.yc.jianjiao.presenter;

import com.lzy.okgo.model.Response;
import com.yc.jianjiao.bean.BaseListBean;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.bean.SaveSearchFindListBean;
import com.yc.jianjiao.callback.Code;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.view.impl.SearchFindContract;
import org.litepal.LitePal;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/3/20.
 */

public class SearchFindPresenter extends SearchFindContract.Presenter{
    @Override
    public void onSearch(final String text, int pagerNumber) {
        onSaveHistory(text);

        CloudApi.videoPageSearch(pagerNumber, null, text)
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
                                if (list != null && list.size() != 0){
                                    mView.setData(list, text);
                                    mView.setRefreshLayoutMode(data.getTotalRow());
                                    mView.hideLoading();
                                }else {
                                    mView.showLoadEmpty();
                                }
                            }
                        }else {
                            mView.showLoadEmpty();
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
    public void onSaveHistory(String text) {
        final List<SaveSearchFindListBean> all = LitePal.findAll(SaveSearchFindListBean.class);
        if (all != null){
            List<SaveSearchFindListBean> saveSearchListBeans = LitePal.where("message = ?" , text).find(SaveSearchFindListBean.class);
            if (saveSearchListBeans.size() == 0){
                SaveSearchFindListBean bean = new SaveSearchFindListBean();
                bean.setMessage(text);
                bean.save();
            }
        }
        mView.setSearchData();
    }

    @Override
    public void onHotLabel(int type) {
        CloudApi.hotListHot(type)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<List<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<List<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            List<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                mView.setHotData(data);
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

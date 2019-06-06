package com.yc.jianjiao.presenter;

import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.model.Response;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.LabelAdapter;
import com.yc.jianjiao.base.BaseActivity;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.callback.Code;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.utils.cache.ShareSessionIdCache;
import com.yc.jianjiao.view.MeFrg;
import com.yc.jianjiao.view.impl.MeContract;
import com.yc.jianjiao.weight.WithScrollGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/29.
 */

public class MePresenter extends MeContract.Presenter{
    @Override
    public void onLabel(WithScrollGridView gridView, final BaseFragment root) {
        String[] laberStr = {act.getString(R.string.my_promote), act.getString(R.string.feedback),
                act.getString(R.string.my_msg), act.getString(R.string.communication)};
        int[] laberImg = {R.mipmap.mine_a03, R.mipmap.mine_a04, R.mipmap.daishouhuo, R.mipmap.daipingjia};
        List<DataBean> listStr = new ArrayList<>();
        for (int i = 0; i < laberStr.length; i++) {
            DataBean bean = new DataBean();
            bean.setName(laberStr[i]);
            bean.setImg(laberImg[i]);
            listStr.add(bean);
        }
        LabelAdapter labelAdapter = new LabelAdapter(act, listStr);
        gridView.setAdapter(labelAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!((BaseActivity)act).isLogin())return;
                switch (i){
                    case 0:
//                        UIHelper.startPromoteFrg(root);
                        UIHelper.startPromoteAct();
                        break;
                    case 1:
                        UIHelper.startFeedbackFrg(root);
                        break;
                    case 2:
                        UIHelper.startMsgFrg(root, 0);
                        break;
                    case 3:
                        UIHelper.startCommunicationGroupFrg(root);
                        break;
                }
            }
        });
    }

    @Override
    public void onInformation() {
        CloudApi.userInformation()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                            final JSONObject data = jsonObject.optJSONObject("data");
                            try {
                                final List<File> files = FileUtils.listFilesInDir(Constants.videoUrl);
                                if (files != null){
                                    data.put("countVideoCache", files.size());
                                }else {
                                    data.put("countVideoCache", 0);
                                }
                                User.getInstance().setUserObj(data);
                                User.getInstance().setLogin(true);
                                mView.setData(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
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

    @Override
    public void onAdv() {
        CloudApi.advList(8)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
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
                                mView.setAdvert(data.get(0));
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

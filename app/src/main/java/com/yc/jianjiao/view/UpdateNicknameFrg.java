package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.lzy.okgo.model.Response;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseView;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.callback.Code;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.databinding.FUpdateNicknameBinding;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/10.
 *  修改昵称
 */

public class UpdateNicknameFrg extends BaseFragment<BasePresenter, FUpdateNicknameBinding> implements IBaseView {
    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_update_nickname;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.nick));
//        JSONObject userInfoObj = User.getInstance().getUserInfoObj();
        mB.etText.setText("");

        mB.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = mB.etText.getText().toString();
                if (StringUtils.isEmpty(name)){
                    showToast(getString(R.string.error_nickname));
                    return;
                }
                if (name.equals("")){
                    act.onBackPressed();
                    return;
                }
                CloudApi.userUpdate(null, name)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoading();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                                if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                    DataBean data = baseResponseBeanResponse.body().data;
                                    JSONObject userObj = User.getInstance().getUserObj();
                                    JSONObject userExtend = userObj.optJSONObject("userExtend");
                                    try {
                                        userExtend.put("nick", data.getNick());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    act.onBackPressed();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                                hideLoading();
                            }
                        });
            }
        });
    }

}

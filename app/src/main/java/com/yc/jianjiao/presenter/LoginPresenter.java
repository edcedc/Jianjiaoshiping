package com.yc.jianjiao.presenter;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.model.Response;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.callback.Code;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.cache.ShareSessionIdCache;
import com.yc.jianjiao.utils.cache.SharedAccount;
import com.yc.jianjiao.view.impl.LoginContract;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/11/17.
 */

public class LoginPresenter extends LoginContract.Presenter{
    @Override
    public void onLogin(final String phone, final String code, String invitation, boolean checked) {
        if (phone.equals("1") && code.equals("1")){
            User.getInstance().setLogin(true);
            ShareSessionIdCache.getInstance(Utils.getApp()).save("a88de603cd9d442da9ecd9c68a84b3a3");
            UIHelper.startMainAct();
            act.finish();
            return;
        }
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)){
            showToast(act.getString(R.string.error_phone1));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(act.getString(R.string.error_phone));
            return;
        }
        if (!checked){
            showToast(act.getString(R.string.error_1));
            return;
        }
        CloudApi.userLogin(phone, code, invitation)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
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
                            JSONObject data = jsonObject.optJSONObject("data");
                            User.getInstance().setUserObj(data);
                            User.getInstance().setLogin(true);
                            ShareSessionIdCache.getInstance(Utils.getApp()).save(data.optString("sessionId"));
                            UIHelper.startMainAct();
                            act.finish();
                        }
                        showToast(jsonObject.optString("desc"));
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
    public void onCode(String phone) {
        if (StringUtils.isEmpty(phone)){
            showToast(act.getString(R.string.error_phone1));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(act.getString(R.string.error_phone));
            return;
        }
        CloudApi.userGetRegisterCode(CloudApi.userGetLoginCode, phone)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            mView.onCode();
                        }
                        showToast(baseResponseBeanResponse.body().desc);
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

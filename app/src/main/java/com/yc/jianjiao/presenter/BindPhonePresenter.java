package com.yc.jianjiao.presenter;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yc.jianjiao.R;
import com.yc.jianjiao.view.impl.BindPhoneContract;
import com.yc.jianjiao.view.impl.LoginContract;

/**
 * Created by edison on 2018/11/17.
 */

public class BindPhonePresenter extends BindPhoneContract.Presenter{
    @Override
    public void onLogin(final String phone, final String code) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)){
            showToast(act.getString(R.string.error_phone1));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(act.getString(R.string.error_phone));
            return;
        }
        /*CloudApi.authLogin(phone, pwd)
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
                            SharedAccount.getInstance(Utils.getApp()).save(phone, pwd);
                            ShareSessionIdCache.getInstance(act).save(data.optString("userId") + "_" + data.optString("token"));
                            UIHelper.startMainAct();
                            act.onBackPressed();
                        }else {
                            showToast(jsonObject.optString("message"));
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
                });*/
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
        mView.onCode();
    }
}

package com.yc.jianjiao.view.impl;


import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseView;

/**
 * Created by edison on 2018/11/17.
 */

public interface LoginContract {

    interface View extends IBaseView {

        void onCode();
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onLogin(String phone, String code, String invitation, boolean checked);

        public abstract void onCode(String phone);
    }

}

package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseView;

/**
 * Created by edison on 2019/1/29.
 */

public interface UpdatePhoneContract {

    interface View extends IBaseView {

        void onCode();
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onLogin(String phone, String code, boolean checked);

        public abstract void onCode(String phone);
    }

}

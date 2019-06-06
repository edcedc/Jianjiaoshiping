package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.base.IBaseView;

/**
 * Created by edison on 2019/1/29.
 */

public interface AccountManagementContract {

    interface View extends IBaseView {


        void setHead(String head);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onUpdateHead(String path);
    }

}

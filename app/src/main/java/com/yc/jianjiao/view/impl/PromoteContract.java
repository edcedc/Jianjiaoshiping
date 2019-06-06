package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.base.IBaseView;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/29.
 */

public interface PromoteContract {

    interface View extends IBaseView {

    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest();
    }

}

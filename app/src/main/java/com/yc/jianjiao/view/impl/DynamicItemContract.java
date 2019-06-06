package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/29.
 */

public interface DynamicItemContract {

    interface View extends IBaseListView {
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber);
    }

}

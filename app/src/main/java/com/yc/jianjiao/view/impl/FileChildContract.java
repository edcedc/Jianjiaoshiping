package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;

/**
 * Created by edison on 2019/1/28.
 */

public interface FileChildContract {

    interface View extends IBaseListView {

        void setCollect(int i, int isCollect);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber, String id);

        public abstract void onCollect(int i, String id, int isCollect);
    }

}

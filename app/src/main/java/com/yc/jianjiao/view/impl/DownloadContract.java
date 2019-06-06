package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;

/**
 * Created by edison on 2019/1/30.
 */

public interface DownloadContract {

    interface View extends IBaseListView {


    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber);

    }

}

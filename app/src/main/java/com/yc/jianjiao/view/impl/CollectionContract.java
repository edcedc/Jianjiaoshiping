package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;

import java.util.List;

/**
 * Created by edison on 2019/1/30.
 */

public interface CollectionContract {

    interface View extends IBaseListView {


        void onDelSuccess();
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber);

        public abstract void onDel(List<String> listStr);
    }

}

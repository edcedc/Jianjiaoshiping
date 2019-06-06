package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/28.
 */

public interface StarContract {

    interface View extends IBaseListView {

        void setBanner(List<DataBean> listBean);

        void onZanSuccess(int position, int type);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber);

        public abstract void onBanner();

        public abstract void onPraise(int position, String id, int type);
    }

}

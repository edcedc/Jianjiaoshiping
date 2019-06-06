package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public interface HotVideoContract {

    interface View extends IBaseListView {


        void setBanner(List<DataBean> listBean);

        void setLabel(List<DataBean> listBean);

        void setAdv(DataBean bean);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber);

        public abstract void onBanner();

        public abstract void onLabel();

        public abstract void onAdv();
    }

}

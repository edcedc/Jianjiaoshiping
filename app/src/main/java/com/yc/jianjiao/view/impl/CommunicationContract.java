package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.base.IBaseView;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/30.
 */

public interface CommunicationContract {

    interface View extends IBaseView {
        void setWXData(List<DataBean> listBean);

        void setQQData(List<DataBean> listBean);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequestQQ();
        public abstract void onRequestWX();
    }
    
}

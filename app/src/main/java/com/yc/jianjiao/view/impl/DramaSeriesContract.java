package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public interface DramaSeriesContract {

    interface View extends IBaseListView {

        void setLabelData(List<DataBean> data);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber, List<String> listId);

        public abstract void onLabel(String id);
    }

}

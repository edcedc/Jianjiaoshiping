package com.yc.jianjiao.view.impl;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.base.IBaseView;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/22.
 */

public interface SearchContract {

    interface View extends IBaseView {

        void setSearchData();

        void setData(List<DataBean> listBean);

        void setSearchList(List<DataBean> listBean);

        void setHotData(List<DataBean> data);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onSearch(String text);

        public abstract void onSaveHistory(String text);

        public abstract void onHotLabel(int type);

        public abstract void onHomeLabel();
    }
}

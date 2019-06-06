package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/3/20.
 */

public interface SearchFindContract {

    interface View extends IBaseListView {

        void setSearchData();

        void setData(List<DataBean> listBean, String text);

        void setHotData(List<DataBean> data);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onSearch(String text, int pagerNumber);

        public abstract void onSaveHistory(String text);

        public abstract void onHotLabel(int type);

    }

}

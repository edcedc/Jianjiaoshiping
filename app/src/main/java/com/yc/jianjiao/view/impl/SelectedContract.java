package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

import java.io.InputStream;
import java.util.List;

import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * Created by edison on 2019/2/14.
 */

public interface SelectedContract {

    interface View extends IBaseListView {

        void setBanner(List<DataBean> listBean);

        void setLabel(List<DataBean> listBean);

        void setHotData(List<DataBean> listBean);

        void setWhatData(List<DataBean> listBean);

        void setLikeData(List<DataBean> listBean);

        void setAdvert(DataBean dataBean);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onHotRequest(String id);

        public abstract void onWhatRequest(String id);

        public abstract void onLikeRequest(String id);

        public abstract void onBanner();

        public abstract void onLabel(String id);

    }

}

package com.yc.jianjiao.view.impl;


import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

import java.io.InputStream;
import java.util.List;

import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * Created by edison on 2018/11/16.
 */

public interface HomeChildContract {

    interface View extends IBaseListView {

        void setBanner(List<DataBean> listBean);

        void setLabel(List<DataBean> listBean);

        void setLookData(List<DataBean> listBean);

        void setDramaticData(List<DataBean> listBean);

        void setLikeData(List<DataBean> listBean);

        void setScreenData(List<DataBean> data);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onLookRequest(String id);

        public abstract void onDramaticRequest(String id);

        public abstract void onLikeRequest(String id);

        public abstract void onBanner(int position);

        public abstract void onLabel(String id);

        public abstract BaseDanmakuParser reateParser(InputStream inputStream);

        public abstract void onScreen(String id);
    }

}

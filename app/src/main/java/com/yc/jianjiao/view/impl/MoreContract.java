package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;
import com.yc.jianjiao.bean.DataBean;

import java.io.InputStream;
import java.util.List;

import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * Created by edison on 2019/1/22.
 */

public interface MoreContract {

    interface View extends IBaseListView {


    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pagerNumber, String id);

    }

}

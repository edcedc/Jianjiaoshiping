package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseListView;

import java.util.List;

/**
 * Created by edison on 2019/1/22.
 */

public interface SearchChildContract {

    interface View extends IBaseListView {

    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pageNumber, String listId, String search);

    }
}

package com.yc.jianjiao.view.impl;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseView;
import com.yc.jianjiao.bean.DataBean;

/**
 * Created by edison on 2019/1/27.
 */

public interface VideoIntroductionContract {

    interface View extends IBaseView {

    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onReport();
    }

}

package com.yc.jianjiao.view.impl;

import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.IBaseView;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.view.MeFrg;
import com.yc.jianjiao.weight.WithScrollGridView;

import org.json.JSONObject;

/**
 * Created by edison on 2019/1/29.
 */

public interface MeContract {

    interface View extends IBaseView {

        void setData(JSONObject data);

        void setAdvert(DataBean bean);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onLabel(WithScrollGridView gridView, BaseFragment root);

        public abstract void onInformation();

        public abstract void onAdv();
    }

}

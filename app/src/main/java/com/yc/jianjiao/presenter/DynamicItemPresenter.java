package com.yc.jianjiao.presenter;

import android.os.Handler;

import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.view.impl.DynamicItemContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/29.
 */

public class DynamicItemPresenter extends DynamicItemContract.Presenter{
    @Override
    public void onRequest(int pagerNumber) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<DataBean> listBean = new ArrayList<>();
                for (int i = 0;i < 10;i++){
                    listBean.add(new DataBean());
                }
                mView.setData(listBean);
                mView.hideLoading();
            }
        }, 500);
    }
}

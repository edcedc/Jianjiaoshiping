package com.yc.jianjiao.presenter;

import android.os.Handler;

import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.view.impl.WorthSeeingChildContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/22.
 */

public class WorthSeeingChildPresenter extends WorthSeeingChildContract.Presenter{
    @Override
    public void onRequest(String url) {
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

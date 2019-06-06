package com.yc.jianjiao.view.act;

import android.os.Bundle;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseActivity;
import com.yc.jianjiao.view.MainFrg;
import com.yc.jianjiao.view.PromoteFrg;

/**
 * Created by edison on 2019/4/12.
 */

public class PromoteAct extends BaseActivity {
    @Override
    protected void initPresenter() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected void initView() {
        if (findFragment(PromoteFrg.class) == null) {
            loadRootFragment(R.id.fl_container, PromoteFrg.newInstance());
        }
    }
}

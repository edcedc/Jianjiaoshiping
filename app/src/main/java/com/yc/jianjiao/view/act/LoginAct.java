package com.yc.jianjiao.view.act;

import android.os.Bundle;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseActivity;
import com.yc.jianjiao.view.LoginFrg;

/**
 * Created by edison on 2019/1/20.
 */

public class LoginAct extends BaseActivity{
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
        setSofia(true);
        if (findFragment(LoginFrg.class) == null) {
            loadRootFragment(R.id.fl_container, LoginFrg.newInstance());
        }
    }
}

package com.yc.jianjiao.view.act;

import android.os.Bundle;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseActivity;
import com.yc.jianjiao.event.KeyInEvent;
import com.yc.jianjiao.view.DynamicDetailsFrg;
import com.yc.jianjiao.view.LoginFrg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by edison on 2019/1/29.
 */

public class DynamicDetailsAct extends BaseActivity{

    private String id;
    private int position;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        position = bundle.getInt("position");
    }

    @Override
    protected void initView() {
        setSofia(true);
        DynamicDetailsFrg frg = DynamicDetailsFrg.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("position", position);
        frg.setArguments(bundle);
        if (findFragment(DynamicDetailsFrg.class) == null) {
            loadRootFragment(R.id.fl_container, frg);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMainKeyInEvent(KeyInEvent event){
//        onBackPressedSupport();
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }
}

package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FSetBinding;
import com.yc.jianjiao.utils.PopupWindowTool;

/**
 * Created by edison on 2019/1/29.
 *  设置
 */

public class SetFrg extends BaseFragment<BasePresenter, FSetBinding> implements View.OnClickListener{
    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_set;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.set));
        mB.refreshLayout.setPureScrollModeOn();
        mB.lyAccountManagement.setOnClickListener(this);
        mB.lyClearCache.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_account_management:
                UIHelper.startAccountManagementFrg(this);
                break;
            case R.id.ly_clear_cache:
                PopupWindowTool.showDialog(act, PopupWindowTool.clear_cache, new PopupWindowTool.DialogListener() {
                    @Override
                    public void onClick() {

                    }
                });
                break;
            case R.id.bt_submit:
                PopupWindowTool.showDialog(act, PopupWindowTool.sign_out, new PopupWindowTool.DialogListener() {
                    @Override
                    public void onClick() {
                        UIHelper.startLoginAct();
                    }
                });
                break;
        }
    }
}

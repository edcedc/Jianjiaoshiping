package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FUpdatePhoneBinding;
import com.yc.jianjiao.presenter.UpdatePhonePresenter;
import com.yc.jianjiao.utils.CountDownTimerUtils;
import com.yc.jianjiao.view.impl.UpdatePhoneContract;

/**
 * Created by edison on 2019/1/29.
 */

public class UpdatePhoneFrg extends BaseFragment<UpdatePhonePresenter, FUpdatePhoneBinding> implements UpdatePhoneContract.View, View.OnClickListener{
    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_update_phone;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.save_phone));
        mB.tvCode.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
    }

    @Override
    public void onCode() {
        new CountDownTimerUtils(act, 60000, 1000, mB.tvCode).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_code:
                mPresenter.onCode(mB.etPhone.getText().toString());
                break;
            case R.id.bt_submit:
                mPresenter.onLogin(mB.etPhone.getText().toString(), mB.etCode.getText().toString(), false);
                break;
        }
    }
}

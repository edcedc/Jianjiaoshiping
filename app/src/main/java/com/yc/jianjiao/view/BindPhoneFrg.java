package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FLoginBinding;
import com.yc.jianjiao.presenter.LoginPresenter;
import com.yc.jianjiao.utils.CountDownTimerUtils;
import com.yc.jianjiao.view.impl.LoginContract;

/**
 * Created by edison on 2019/1/20.
 *  绑定手机号码
 */

public class BindPhoneFrg extends BaseFragment<LoginPresenter, FLoginBinding> implements LoginContract.View, View.OnClickListener{

    public static BindPhoneFrg newInstance() {
        Bundle args = new Bundle();
        BindPhoneFrg fragment = new BindPhoneFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_login;
    }

    @Override
    protected void initView(View view) {
        mB.refreshLayout.setPureScrollModeOn();
        mB.ivQq.setVisibility(View.GONE);
        mB.ivWx.setVisibility(View.GONE);
        mB.tvCode.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        mB.tvAgreement.setOnClickListener(this);
        mB.fyClose.setOnClickListener(this);
        mB.tvLogin.setText(getString(R.string.bing_login));
        mB.btSubmit.setText(getString(R.string.submit4));
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
                mPresenter.onLogin(mB.etPhone.getText().toString(), mB.etCode.getText().toString(),mB.tvInvitation.getText().toString(), mB.cbSubmit.isChecked());
                break;
            case R.id.tv_agreement:
                UIHelper.startHtmlAct(0, null);
                break;
            case R.id.fy_close:
                act.onBackPressed();
                break;
        }
    }
}

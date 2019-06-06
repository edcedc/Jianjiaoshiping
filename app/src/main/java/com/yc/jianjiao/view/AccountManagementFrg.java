package com.yc.jianjiao.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FAccountBinding;
import com.yc.jianjiao.event.CameraInEvent;
import com.yc.jianjiao.presenter.AccountManagementPresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.bottom.CameraBottomFrg;
import com.yc.jianjiao.view.impl.AccountManagementContract;
import com.yc.jianjiao.weight.PictureSelectorTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/29.
 *  账号管理
 */

public class AccountManagementFrg extends BaseFragment<AccountManagementPresenter, FAccountBinding> implements AccountManagementContract.View, View.OnClickListener{
    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    private CameraBottomFrg cameraBottomFrg;
    private List<LocalMedia> localMediaList = new ArrayList<>();

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_account;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.account_management));
        mB.refreshLayout.setPureScrollModeOn();
        mB.lyHead.setOnClickListener(this);
        mB.lyNick.setOnClickListener(this);
        mB.lyPhone.setOnClickListener(this);

        if (cameraBottomFrg == null){
            cameraBottomFrg = new CameraBottomFrg();
        }
        cameraBottomFrg.setCameraListener(new CameraBottomFrg.onCameraListener() {
            @Override
            public void camera() {
                PictureSelectorTool.PictureSelectorImage(act, CameraInEvent.HEAD_CAMEAR, true);
                if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            }

            @Override
            public void photo() {
                PictureSelectorTool.photo(act, CameraInEvent.HEAD_PHOTO, true);
                if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            }
        });
        EventBus.getDefault().register(this);

        mB.tvNick.setText("nick");
        mB.tvPhone.setText("123");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThreadInEvent(CameraInEvent event) {
        if (event.getRequest() == CameraInEvent.HEAD_CAMEAR || event.getRequest() == CameraInEvent.HEAD_PHOTO){
            if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            localMediaList.clear();
            localMediaList.addAll(PictureSelector.obtainMultipleResult((Intent) event.getObject()));
            String path = localMediaList.get(0).getCompressPath();
            mPresenter.onUpdateHead(path);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        JSONObject data = User.getInstance().getUserObj();
        if (data != null){
            JSONObject userExtend = data.optJSONObject("userExtend");
            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + userExtend.optString("head"), mB.ivHead);
            String nick = userExtend.optString("nick");
            mB.tvNick.setText(nick.equals("null") == true ? "请设置昵称" : nick);
            mB.tvPhone.setText(data.optString("username"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_head:
                cameraBottomFrg.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.ly_nick:
                UIHelper.startUpdateNicknameFrg(this);
                break;
            case R.id.ly_phone:
                UIHelper.startUpdatePhoneFrg(this);
                break;
        }
    }

    @Override
    public void setHead(String head) {
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + head, mB.ivHead);
    }
}

package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.databinding.FMsgDescBinding;
import com.yc.jianjiao.utils.GlideLoadingUtils;

/**
 * Created by edison on 2019/1/24.
 *  消息详情
 */

public class MsgDescFrg extends BaseFragment<BasePresenter, FMsgDescBinding>{

    private String content;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        content = bundle.getString("content");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_msg_desc;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.system_msg));
        mB.refreshLayout.setPureScrollModeOn();

        GlideLoadingUtils.load(act, "", mB.ivImg);
        mB.tvTitle.setText("系统公告标题");
        mB.tvTime.setText("2018-12-17");
        mB.tvContent2.setText("视频APP官方");
        mB.tvContent.setText("服务简介：现代风格一种比较流行的风格，最求时尚与 潮流，非常注重空间布局现代风格一种比较流行的风格， 最求时尚与潮流，非常注重");
    }
}

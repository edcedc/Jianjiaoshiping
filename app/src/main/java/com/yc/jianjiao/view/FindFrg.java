package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.MyPagerAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseListContract;
import com.yc.jianjiao.base.BaseListPresenter;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FFindBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/20.
 *  发现
 */

public class FindFrg extends BaseFragment<BaseListPresenter, FFindBinding> implements BaseListContract.View, View.OnClickListener{

    public static FindFrg newInstance() {
        Bundle args = new Bundle();
        FindFrg fragment = new FindFrg();
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
    protected int bindLayout() {
        return R.layout.f_find;
    }

    @Override
    protected void initView(View view) {
        showLoadDataing();
        mPresenter.onRequest(CloudApi.labelListVideo);
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {

    }

    @Override
    public void setData(Object data) {
        List<DataBean> list = (List<DataBean>) data;
        ArrayList<Fragment> mFragments = new ArrayList<>();
        String[] strings = new String[list.size()];
        for (int i = 0;i < list.size();i++){
            DataBean bean = list.get(i);
            strings[i] = bean.getName();
            FileChildFrg frg = new FileChildFrg();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            bundle.putString("id", bean.getId());
            frg.setArguments(bundle);
            mFragments.add(frg);
        }
        mB.viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragments, strings));
        mB.tbLayout.setViewPager(mB.viewPager);
        mB.viewPager.setOffscreenPageLimit(list.size() - 1);
        mB.tbLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mB.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mB.lySearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_search:
                UIHelper.startSearchFindFrg(this);
                break;
        }
    }
}

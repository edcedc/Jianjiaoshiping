package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.HomeLabelAdapter;
import com.yc.jianjiao.adapter.MyPagerAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseListContract;
import com.yc.jianjiao.base.BaseListPresenter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.databinding.FHomeBinding;
import com.yc.jianjiao.weight.WithScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/20.
 *  首页
 */

public class HomeFrg extends BaseFragment<BaseListPresenter, FHomeBinding> implements BaseListContract.View, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    private WithScrollGridView gridView;

    public static HomeFrg newInstance() {
        Bundle args = new Bundle();
        HomeFrg fragment = new HomeFrg();
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
        return R.layout.f_home;
    }

    @Override
    protected void initView(View view) {
        mB.ivClass.setOnClickListener(this);
        showLoadDataing();
        mPresenter.onRequest(CloudApi.labelHome);
        setSwipeBackEnable(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                act, mB.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawer.setDrawerListener(toggle);
        mB.navView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        LinearLayout llNavHeader = (LinearLayout) mB.navView.getHeaderView(0);
        llNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mB.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        gridView = llNavHeader.findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mB.viewPager.setCurrentItem(i);
                mB.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mB.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mB.drawerLayout.closeDrawer(GravityCompat.START);
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_class:
                onOpenDrawer();
                break;
        }
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {

    }

    @Override
    public void setData(Object data) {
        List<DataBean> list = (List<DataBean>) data;
        HomeLabelAdapter labelAdapter = new HomeLabelAdapter(act, list);
        gridView.setAdapter(labelAdapter);
        ArrayList<Fragment> mFragments = new ArrayList<>();
        String[] strings = new String[list.size()];
        for (int i = 0;i < list.size();i++){
            DataBean bean = list.get(i);
            strings[i] = bean.getName();
            if (bean.getId().equals("98")){
                SelectedFrg frg = new SelectedFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("position", i);
                bundle.putString("id", bean.getId());
                frg.setArguments(bundle);
                mFragments.add(frg);
            }else if (bean.getId().equals("99")){
                SelectedTwoFrg frg = new SelectedTwoFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("position", i);
                bundle.putString("id", bean.getId());
                frg.setArguments(bundle);
                mFragments.add(frg);
            }else {
                HomeChildFrg frg = new HomeChildFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("position", i);
                bundle.putString("id", bean.getId());
                frg.setArguments(bundle);
                mFragments.add(frg);
            }
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
    }

    public void onOpenDrawer() {
        if (!mB.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mB.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mB.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

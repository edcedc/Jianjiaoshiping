package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.MyPagerAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.databinding.FMsgBinding;
import com.yc.jianjiao.weight.TabEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by edison on 2019/1/24.
 *  消息
 */

public class MsgFrg extends BaseFragment<BasePresenter, FMsgBinding>{

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] strings = {"系统消息", "评论", "点赞"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_msg;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.msg));
        setSwipeBackEnable(false);
        mFragments.add(new SystemMsgFrg());
        mFragments.add(new CommentFrg());
        mFragments.add(new ZanFrg());
        for (int i = 0; i < strings.length; i++) {
            mTabEntities.add(new TabEntity(strings[i], 0, 0));
        }
        mB.tabLayout.setTabData(mTabEntities);
        mB.viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragments));
//        mB.tbLayout.setViewPager(mB.viewPager);
        mB.viewPager.setOffscreenPageLimit(2);
        mB.tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mB.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mB.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mB.tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

}

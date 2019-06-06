package com.yc.jianjiao.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.lzy.okgo.model.Response;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseActivity;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.callback.Code;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.databinding.FMainBinding;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.buttonBar.BottomBar;
import com.yc.jianjiao.weight.buttonBar.BottomBarTab;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by edison on 2019/1/20.
 */

public class MainFrg extends BaseFragment<BasePresenter, FMainBinding> implements View.OnClickListener{

    private List<DataBean> data;

    public static MainFrg newInstance() {
        Bundle args = new Bundle();
        MainFrg fragment = new MainFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private final int ZERO = 0;
    private final int ONE = 1;
    private final int TWO = 2;
    private final int THREE = 3;
    private final int FOUR = 4;
    private SupportFragment[] mFragments = new SupportFragment[5];

    private int position = 0;
    private int prePosition;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_main;
    }

    @Override
    protected void initView(View view) {
        mB.ly1.setOnClickListener(this);
        mB.ly2.setOnClickListener(this);
        mB.ly3.setOnClickListener(this);
        mB.ly4.setOnClickListener(this);
        mB.ly5.setOnClickListener(this);
        CloudApi.labelListLabel("10")
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoadEmpty();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<List<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<List<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            data = baseResponseBeanResponse.body().data;
                            if (data != null && data.size() != 0){
                                for (int i = 0; i < data.size(); i++){
                                    DataBean bean = data.get(i);
                                    switch (i){
                                        case 0:
                                            mB.tv1.setText(bean.getName());
                                            break;
                                        case 1:
                                            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getImage(), mB.iv2);
                                            mB.tv2.setText(bean.getName());
                                            break;
                                        case 2:
                                            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getImage(), mB.iv3);
                                            mB.tv3.setText(bean.getName());

                                            break;
                                        case 3:
                                            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getImage(), mB.iv4);
                                            mB.tv4.setText(bean.getName());

                                            break;
                                        case 4:
                                            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getImage(), mB.iv5);
                                            mB.tv5.setText(bean.getName());
                                            break;
                                    }
                                }
                                setClick(position, mB.iv1, mB.tv1);
                                showHideFragment(mFragments[position], mFragments[prePosition]);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });






//        mB.bottomBar
//                .addItem(new BottomBarTab(_mActivity, R.mipmap.icon_a06, "首页"))
//                .addItem(new BottomBarTab(_mActivity, R.mipmap.icon_a02,"频道"))
//                .addItem(new BottomBarTab(_mActivity, R.mipmap.icon_a03,"发现"))
//                .addItem(new BottomBarTab(_mActivity, R.mipmap.icon_a04,"星球"))
//                .addItem(new BottomBarTab(_mActivity, R.mipmap.icon_a05,"我的"))
//        ;
//        mB.bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(int position, int prePosition) {
//                if (position == THREE){
//                    if (!((BaseActivity)act).isLogin())return;
//                }
//                showHideFragment(mFragments[position], mFragments[prePosition]);            }
//
//            @Override
//            public void onTabUnselected(int position) {
//            }
//
//            @Override
//            public void onTabReselected(int position) {
//            }
//        });
//        mB.bottomBar.setCurrentItem(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(HomeFrg.class);
        if (firstFragment == null) {
            mFragments[ZERO] = HomeFrg.newInstance();
            mFragments[ONE] = VideoFrg.newInstance();
            mFragments[TWO] = FindFrg.newInstance();
            mFragments[THREE] = StarFrg.newInstance();
            mFragments[FOUR] = MeFrg.newInstance();

            loadMultipleRootFragment(R.id.fl_container,
                    ZERO,
                    mFragments[ZERO],
                    mFragments[ONE],
                    mFragments[TWO],
                    mFragments[THREE],
                    mFragments[FOUR]
            );

        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[ZERO] = firstFragment;
            mFragments[ONE] = findChildFragment(VideoFrg.class);
            mFragments[TWO] = findChildFragment(FindFrg.class);
            mFragments[THREE] = findChildFragment(StarFrg.class);
            mFragments[FOUR] = findChildFragment(MeFrg.class);
        }
        setSwipeBackEnable(false);
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

    @Override
    public boolean onBackPressedSupport() {
        exitBy2Click();// 调用双击退出函数
//        return super.onBackPressedSupport();
        return true;
    }

    private Boolean isExit = false;

    private void exitBy2Click() {
        Handler tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            showToast("再按一次退出程序");
            tExit = new Handler();
            tExit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            return;
        } else {
//            Cockroach.uninstall();
            act.finish();
            System.exit(0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_1:
                position = 0;
                setClick(position, mB.iv1, mB.tv1);
                break;
            case R.id.ly_2:
                position = 1;
                setClick(position, mB.iv2, mB.tv2);
                break;
            case R.id.ly_3:
                position = 2;
                setClick(position, mB.iv3, mB.tv3);
                break;
            case R.id.ly_4:
                position = 3;
                setClick(position, mB.iv4, mB.tv4);
                break;
            case R.id.ly_5:
                position = 4;
                setClick(position, mB.iv5, mB.tv5);
                break;
        }
        showHideFragment(mFragments[position], mFragments[prePosition]);
        switch (prePosition){
            case 0:
                setNoClick(prePosition, mB.iv1, mB.tv1);
                break;
            case 1:
                setNoClick(prePosition, mB.iv2, mB.tv2);
                break;
            case 2:
                setNoClick(prePosition, mB.iv3, mB.tv3);
                break;
            case 3:
                setNoClick(prePosition, mB.iv4, mB.tv4);
                break;
            case 4:
                setNoClick(prePosition, mB.iv5, mB.tv5);
                break;

        }
        prePosition = position;
    }

    private void setClick(int position, ImageView imageView, TextView textView){
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + data.get(position).getClick_image(), imageView);
        textView.setTextColor(act.getColor(R.color.orange_FF7D2D));
    }
    private void setNoClick(int position, ImageView imageView, TextView textView){
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + data.get(position).getImage(), imageView);
        textView.setTextColor(act.getColor(R.color.gray_a1a1a1));
    }
}

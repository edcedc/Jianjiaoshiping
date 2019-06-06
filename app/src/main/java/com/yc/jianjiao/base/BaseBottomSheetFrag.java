package com.yc.jianjiao.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.event.PayInEvent;
import com.yc.jianjiao.utils.TUtil;
import com.yc.jianjiao.utils.pay.PayResult;
import com.yc.jianjiao.weight.LoadDataLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by 李可乐 on 2016/9/8 0008.
 */
public abstract class BaseBottomSheetFrag<P extends BasePresenter, VB extends ViewDataBinding> extends BottomSheetDialogFragment {
    protected Context act;

    protected View rootView;
    protected BottomSheetBehavior mBehavior;

    protected Dialog dialog;

    protected VB mB;
    public P mPresenter;
    private LoadDataLayout swipeLoadDataLayout;
    private TwinklingRefreshLayout refreshLayout;


    protected int pagerNumber = 1;//网络请求默认第一页

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.act = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除缓存View和当前ViewGroup的关联
        ((ViewGroup) (rootView.getParent())).removeView(rootView);
        hideLoading();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void setRecyclerViewType(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(act){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        //解决数据加载不完的问题
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        recyclerView.setFocusable(false);

//        recyclerView.setLayoutManager(new LinearLayoutManager(act));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setBackgroundColor(ContextCompat.getColor(act,R.color.white));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        //每次打开都调用该方法 类似于onCreateView 用于返回一个Dialog实例
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        if (rootView == null) {
            getActivity().getWindow().setSoftInputMode
                    (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
//            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

            //缓存下来的View 当为空时才需要初始化 并缓存
            rootView = View.inflate(act, bindLayout(), null);
            mB = DataBindingUtil.bind(rootView);
            mPresenter = TUtil.getT(this, 0);
            if (mPresenter != null) {
                mPresenter.act = this.getActivity();
                this.initPresenter();
            }
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenUtils.getScreenHeight() / 3 * 2));
        }
        resetView();
        //设置View重新关联
        dialog.getWindow().getAttributes().windowAnimations = R.style.FragmentDialogAnimation;
        dialog.setContentView(rootView);
        mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        mBehavior.setHideable(true);
        //圆角边的关键
        ((View) rootView.getParent()).setBackgroundColor(Color.TRANSPARENT);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                /**
                 * PeekHeight默认高度256dp 会在该高度上悬浮
                 * 设置等于view的高 就不会卡住
                 */
                mBehavior.setPeekHeight(rootView.getHeight());
            }
        });
        // 初始化参数
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        initParms(bundle);
        initView(rootView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        return dialog;
    }

    protected abstract void initPresenter();

    protected abstract void initParms(Bundle bundle);

    public abstract int bindLayout();

    /**
     * 初始化View和设置数据等操作的方法
     */
    public abstract void initView(View view);

    /**
     * 重置的View和数据的空方法 子类可以选择实现
     * 为避免多次inflate 父类缓存rootView
     * 所以不会每次打开都调用{@link #initView(View)}方法
     * 但是每次都会调用该方法 给子类能够重置View和数据
     */
    public void resetView() {

    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    /**
     * 使用关闭弹框 是否使用动画可选
     * 使用动画 同时切换界面Aty会卡顿 建议直接关闭
     *
     * @param isAnimation
     */
    public void close(boolean isAnimation) {
        if (isAnimation) {
            if (mBehavior != null)
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            dismiss();
        }
    }





    private CompositeDisposable compositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    private ProgressDialog progressDialog;


    protected void showToast(final String str) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.showLong(str);
        ToastUtils.showShort(str);
    }

    public void showLoading() {
        mHandler.sendEmptyMessage(handler_load);
    }

    public void hideLoading() {
        mHandler.sendEmptyMessage(handler_hide);
        mHandler.sendEmptyMessage(handler_success);
    }

    public void onError(Throwable e, String errorName) {
        errorText(e, errorName);
    }
    public void onError(Throwable e) {
        errorText(e, null);
    }
    private void errorText(Throwable e, String errorName){
        if (null != e) {
            mHandler.sendEmptyMessage(handler_hide);
            mHandler.sendEmptyMessage(handler_success);
            LogUtils.e(e.getMessage(), errorName);
            showToast(e.getMessage());
            if (refreshLayout != null){
                refreshLayout.finishRefreshing();
                refreshLayout.finishLoadmore();
                showError();
            }
        }else{
            LogUtils.e("请求之外Throwable,断点");
        }
    }


    private final int handler_load = 0;
    private final int handler_hide = 1;
    private final int handler_empty = 2;
    private final int handler_error = 3;
    private final int handler_no_network = 4;
    private final int handler_loadData = 5;
    private final int handler_success = 6;

    private final int SDK_PAY_FLAG = 7;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case handler_load:
                    if (progressDialog != null && progressDialog.isShowing()) return;
                    progressDialog = new ProgressDialog(act);
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("请求网络中...");
                    progressDialog.show();
                    break;
                case handler_hide:
                    hideLoad2ing();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    break;
                case handler_empty:
                    if (swipeLoadDataLayout != null && pagerNumber == 1) {
                        swipeLoadDataLayout.setStatus(LoadDataLayout.EMPTY);
                    }
                    break;
                case handler_error:
                    if (swipeLoadDataLayout != null && pagerNumber == 1) {
                        swipeLoadDataLayout.setStatus(LoadDataLayout.ERROR);
                    }
                    break;
                case handler_loadData:
                    if (swipeLoadDataLayout != null) {
                        swipeLoadDataLayout.setStatus(LoadDataLayout.LOADING);
                    }
                    break;
                case handler_success:
                    if (swipeLoadDataLayout != null) {
                        swipeLoadDataLayout.setStatus(LoadDataLayout.SUCCESS);
                    }
                    break;
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToast("支付成功");
                        LogUtils.e("支付成功");
                        EventBus.getDefault().post(new PayInEvent());
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToast("支付取消");
                    }
                    break;
            }
        }
    };

    private void hideLoad2ing() {
        mHandler.sendEmptyMessage(handler_success);
    }

    public void showLoadDataing() {
        mHandler.sendEmptyMessage(handler_loadData);
    }

    public void showLoadEmpty() {
        mHandler.sendEmptyMessage(handler_empty);
    }

    private void showError(){
        mHandler.sendEmptyMessage(handler_error);
    }

    protected void setRefreshLayout(TwinklingRefreshLayout refreshLayout, RefreshListenerAdapter listener) {
//        ProgressLayout headerView = new ProgressLayout(act);
//        refreshLayout.setHeaderView(headerView);
//        refreshLayout.setOverScrollRefreshShow(false);
        ProgressLayout header = new ProgressLayout(act);
        refreshLayout.setHeaderView(header);
        refreshLayout.setFloatRefresh(true);
        refreshLayout.setOverScrollRefreshShow(false);
        refreshLayout.setHeaderHeight(140);
        refreshLayout.setMaxHeadHeight(240);
        refreshLayout.setOverScrollHeight(200);
        refreshLayout.setOnRefreshListener(listener);
        this.refreshLayout = refreshLayout;
    }

    /**
     *  是否有更多
     * @param listSize
     * @param totalRow
     * @param refreshLayout
     */
    protected void setRefreshLayoutMode(int listSize, int totalRow, TwinklingRefreshLayout refreshLayout) {
        if (listSize == totalRow) {
            refreshLayout.setEnableLoadmore(false);
        } else {
            refreshLayout.setEnableLoadmore(true);
        }
    }

    protected void setRefreshLayout(final int pagerNumber, final TwinklingRefreshLayout refreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pagerNumber == 1) {
                    refreshLayout.finishRefreshing();
                } else {
                    refreshLayout.finishLoadmore();
                }
            }
        }, 300);

    }

 }
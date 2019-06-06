package com.yc.jianjiao.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.yanzhenjie.sofia.Sofia;
import com.yc.jianjiao.R;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.event.PayInEvent;
import com.yc.jianjiao.utils.PopupWindowTool;
import com.yc.jianjiao.utils.TUtil;
import com.yc.jianjiao.utils.pay.PayResult;
import com.yc.jianjiao.weight.GridDividerItemDecoration;
import com.yc.jianjiao.weight.LoadDataLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * 作者：yc on 2018/7/25.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public abstract class BaseActivity<P extends BasePresenter, VB extends ViewDataBinding> extends SupportActivity {

    protected Activity act;
    protected VB mB;
    public P mPresenter;
    private LoadDataLayout swipeLoadDataLayout;
    private TwinklingRefreshLayout refreshLayout;
    protected int pagerNumber = 1;//网络请求默认第一页

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mB = DataBindingUtil.setContentView(this,this.bindLayout());;
        act = this;
        mPresenter = TUtil.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.act = this;
            this.initPresenter();
        }
        // 初始化参数
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        initParms(bundle);
        initView();
        swipeLoadDataLayout = findViewById(R.id.swipeLoadDataLayout);
    }

    protected abstract void initPresenter();

    protected void setSofia(boolean isFullScreen) {
        if (!isFullScreen) {
            Sofia.with(act)
                    .invasionStatusBar()
                    .statusBarBackgroundAlpha(0)
                    .statusBarLightFont()
                    .statusBarBackground(ContextCompat.getColor(act, R.color.white))
            ;
        } else {
            Sofia.with(this)
                    .invasionStatusBar()
                    .statusBarBackground(Color.TRANSPARENT)
                    .navigationBarBackground(Color.TRANSPARENT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void setRecyclerViewGridType(RecyclerView recyclerView, int spanCount, int height, int color){
        recyclerView.setLayoutManager(new GridLayoutManager(act, spanCount));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridDividerItemDecoration(height, ContextCompat.getColor(act,color)));
        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setBackgroundColor(ContextCompat.getColor(act,R.color.white));
    }

    protected abstract int bindLayout();

    protected abstract void initParms(Bundle bundle);

    protected abstract void initView();

    protected void setCenterTitle(String title){
        title(title, 0, null, -1);
    }
    protected void setTitle(String title){
        title(title, 1, null, -1);
    }
    protected void setTitle(String title, String right){
        title(title, 2, right, -1);
    }
    protected void setTitle(String title, int rightImg){
        title(title, 1, null, rightImg);
    }

    private void title(String title, int type, String rightText, int img) {
        final AppCompatActivity mAppCompatActivity = (AppCompatActivity) act;
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView topTitle = findViewById(R.id.top_title);
        TextView topRight = findViewById(R.id.top_right);
        FrameLayout topRightFy = findViewById(R.id.top_right_fy);
        //需要调用该函数才能设置toolbar的信息
        mAppCompatActivity.setSupportActionBar(toolbar);
        switch (type){
            case 0:
                mAppCompatActivity.getSupportActionBar().setTitle("");
                topTitle.setVisibility(View.VISIBLE);
                topTitle.setText(title);
                toolbar.setNavigationIcon(null);
                break;
            case 1:
                topTitle.setVisibility(View.GONE);
                mAppCompatActivity.getSupportActionBar().setTitle(title);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            case 2:
                topTitle.setVisibility(View.GONE);
                mAppCompatActivity.getSupportActionBar().setTitle(title);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                topRight.setText(rightText);
                topRightFy.setVisibility(View.VISIBLE);
                topRightFy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setOnRightClickListener();
                    }
                });
                break;
        }
    }

    protected void setOnRightClickListener() {

    }

    private ProgressDialog dialog;

    public void showLoading() {
        mHandler.sendEmptyMessage(handler_load);
    }

    public void hideLoading() {
        mHandler.sendEmptyMessage(handler_hide);
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

    protected void showToast(final String str){
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.showLong(str);
                ToastUtils.showShort(str);
            }
        });
    }

    private CompositeDisposable compositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    private void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
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



    @Override
    protected void onDestroy() {
        hideLoading();
        dispose();
        super.onDestroy();
    }

    // true  false未登录
    public boolean isLogin(){
        if (!User.getInstance().isLogin()){
            PopupWindowTool.showDialog(act, PopupWindowTool.notLogin, new PopupWindowTool.DialogListener() {
                @Override
                public void onClick() {
                    UIHelper.startLoginAct();
                }
            });
            return false;
        }
        return User.getInstance().isLogin();
    }


    /**
     * Android 点击EditText文本框之外任何地方隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void setRecyclerViewType(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(act));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setBackgroundColor(ContextCompat.getColor(act,R.color.white));
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {

                return true;
            }
        }
        return false;
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
                    if (dialog != null && dialog.isShowing()) return;
                    dialog = new ProgressDialog(act);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("请求网络中...");
                    dialog.show();
                    break;
                case handler_hide:
                    hideLoad2ing();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
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

}

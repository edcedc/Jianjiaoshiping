package com.yc.jianjiao.view.act;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.model.Response;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseActivity;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.callback.Code;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.databinding.AHtmlBinding;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/7/25.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  统一H5
 */

public class HtmlAct extends BaseActivity<BasePresenter, AHtmlBinding> {

    private int type = -1;
    private String url;

    @Override
    public void initPresenter() {mPresenter.init(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.a_html;
    }

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        url = bundle.getString("url");
    }

    @Override
    protected void initView() {
        setSofia(true);
        switch (type){
            case 0://注册协议
                setTitle("注册协议");
                JSONObject basicGet = User.getInstance().getBasicGet();
                mB.webView.loadDataWithBaseURL(null, basicGet.optString("registration_protocol"), "text/html", "utf-8", null);
                break;
            case 1://系统消息详情
                setTitle("消息详情");
                mB.webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
                break;
            default:
                setTitle("广告");
                mB.webView.loadUrl(url);
                break;
        }
        mB.webView.setInitialScale(100);
        mB.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                mB.progressBar.setVisibility(View.GONE);
                ToastUtils.showShort("网页加载失败");
            }
        });
        //进度条
        mB.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mB.progressBar.setVisibility(View.GONE);
                    return;
                }
                mB.progressBar.setVisibility(View.VISIBLE);
                mB.progressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mB.webView.canGoBack()) {
            mB.webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getHtmlUrl(){
        /*CloudApi.commonQueryAPPAgreement()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            DataBean data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                String url = null;
                                switch (type){
                                    case 0:
                                        url = data.getRegistration_protocol();
                                        break;
                                }
                                mB.webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
                            }
                        }else {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        HtmlAct.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });*/
    }

    @Override
    protected void onDestroy() {
        mB.webView.removeAllViews();
        mB.webView.destroy();
        super.onDestroy();
    }
}

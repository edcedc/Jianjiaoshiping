package com.yc.jianjiao.view.bottom;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.VideoIntroductionAdapter;
import com.yc.jianjiao.base.BaseBottomSheetFrag;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.PVideoIntBinding;
import com.yc.jianjiao.weight.GridDividerItemDecoration;
import com.yc.jianjiao.weight.WithScrollGridView;
import com.yc.jianjiao.weight.X5WebView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public class VideoIntroductionBottomFrg extends BaseBottomSheetFrag<BasePresenter, PVideoIntBinding> {

    private DataBean bean;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        bean = new Gson().fromJson(bundle.getString("bean"), DataBean.class);
    }

    @Override
    public int bindLayout() {
        return R.layout.p_video_int;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        AppCompatTextView tvFen = view.findViewById(R.id.tv_fen);
        AppCompatTextView tvContent = view.findViewById(R.id.tv_content);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        X5WebView webView = view.findViewById(R.id.webView);

        tvTitle.setText(bean.getTitle());
        tvFen.setText(bean.getMark() + "");
        tvContent.setText(bean.getVideo_desc());

        webView.setInitialScale(100);
        webView.loadDataWithBaseURL(null, bean.getBrief(), "text/html", "utf-8", null);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                progressBar.setVisibility(View.GONE);
                ToastUtils.showShort("网页加载失败");
            }
        });
        //进度条
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        });
    }
}

package com.yc.jianjiao.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.WriterException;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FPromoteBinding;
import com.yc.jianjiao.presenter.PromotePresenter;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.utils.PopupWindowTool;
import com.yc.jianjiao.view.impl.PromoteContract;
import com.yc.jianjiao.weight.ZXingUtils;

import org.json.JSONObject;

/**
 * Created by edison on 2019/1/29.
 *   推广
 */

public class PromoteFrg extends BaseFragment<PromotePresenter, FPromoteBinding> implements PromoteContract.View, View.OnClickListener{

    public static PromoteFrg newInstance() {
        
        Bundle args = new Bundle();
        
        PromoteFrg fragment = new PromoteFrg();
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
        return R.layout.f_promote;
    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        UIHelper.startMyPromoteFrg(this);
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.promotion), getString(R.string.my_promotion2));
        mB.refreshLayout.setPureScrollModeOn();
        mB.btSubmit.setOnClickListener(this);
        mB.ivZking.setOnClickListener(this);


        mB.ivZking.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mB.ivZking.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Bitmap bitmap = null;
                try {
                    bitmap = ZXingUtils.creatBarcode("https://www.baidu.com/", 100);
                    if (bitmap != null) {
                        mB.ivZking.setImageBitmap(bitmap);
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

        JSONObject data = User.getInstance().getUserObj();
        JSONObject userExtend = data.optJSONObject("userExtend");
        JSONObject promotionRules = data.optJSONObject("promotionRules");
        if (promotionRules != null){
            mB.tvRen.setText("离下一级还差 " +
                    promotionRules.optInt("promote_number") +
                    " 人");
            mB.tvRemainingMovie.setText(promotionRules.optInt("number") +
                    "次");
            int level = promotionRules.optInt("level");
            mB.tvV1.setText(level + "");
            mB.tvGrowthValue.setText("当前成长值V" +
                    level);
            mB.tvV2.setText((level += 1) + "");
        }
        mB.tvToPromote.setText(userExtend.optInt("video_frequency") +
                "次");
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + userExtend.optString("head"), mB.ivHead);
        mB.tvName.setText(userExtend.optString("nick"));
        mB.tvCode.setText("我的邀请码：" + data.optString("share_code"));

        JSONObject basicGet = User.getInstance().getBasicGet();
        mB.webView.loadDataWithBaseURL(null, basicGet.optString("extension_content"), "text/html", "utf-8", null);
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
    public void onDestroy() {
        mB.webView.removeAllViews();
        mB.webView.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_submit:
                UIHelper.startImmediatePromotionFrg(this);
                break;
            case R.id.iv_zking:
                PopupWindowTool.showZking(act);
                break;
        }
    }
}

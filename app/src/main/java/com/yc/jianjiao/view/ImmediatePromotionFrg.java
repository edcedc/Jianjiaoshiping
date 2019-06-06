package com.yc.jianjiao.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.TimeUtils;
import com.google.zxing.WriterException;
import com.umeng.socialize.ShareAction;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.databinding.FImmediateBinding;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.utils.ImageUtils;
import com.yc.jianjiao.utils.ShareTool;
import com.yc.jianjiao.view.bottom.AllCommentBottomFrg;
import com.yc.jianjiao.view.bottom.ShareBottomFrg;
import com.yc.jianjiao.weight.ZXingUtils;

import org.json.JSONObject;

/**
 * Created by edison on 2019/1/29.
 *  我要推广
 */

public class ImmediatePromotionFrg extends BaseFragment<BasePresenter, FImmediateBinding> implements View.OnClickListener{

    private ShareAction shareAction;
    private ShareBottomFrg frg;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_immediate;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.my_promotion));
        mB.refreshLayout.setPureScrollModeOn();

        mB.tvSaveZking.setOnClickListener(this);
        mB.tvCopyPromotion.setOnClickListener(this);
        mB.tvMore.setOnClickListener(this);
        shareAction = ShareTool.getInstance().shareAction(act, "https://www.baidu.com/");
        JSONObject data = User.getInstance().getUserObj();
        mB.tvCode.setText("我的邀请码：" + data.optString("share_code"));

        mB.ivZking.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mB.ivZking.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                try {
                    Bitmap bitmap = ZXingUtils.creatBarcode("https://www.baidu.com/", 90);
                    if (bitmap != null) {
                        mB.ivZking.setImageBitmap(bitmap);
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

        frg = new ShareBottomFrg();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_save_zking:
                ImageUtils.viewSaveToImage(act, mB.layout, TimeUtils.getNowString());
                showToast("保存成功");
                break;
            case R.id.tv_copy_promotion:
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText("https://www.baidu.com/");
                showToast("复制成功");
                break;
            case R.id.tv_more:
//                shareAction.open();
                frg.show(getChildFragmentManager(), "dialog");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareTool.getInstance().release(act);
    }

}

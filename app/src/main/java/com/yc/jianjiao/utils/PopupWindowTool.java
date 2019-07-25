package com.yc.jianjiao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.zxing.WriterException;
import com.yc.jianjiao.R;
import com.yc.jianjiao.view.act.VideoDescAct;
import com.yc.jianjiao.weight.WPopupWindow;
import com.yc.jianjiao.weight.ZXingUtils;

/**
 * 作者：yc on 2018/8/23.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class PopupWindowTool {

    public static final int onTrial = 1; //使用六分钟
    public static final int notMember = 2; //不是会员
    public static final int notLogin = 3; //未登录
    public static final int clear_cache = 4; //清除缓存
    public static final int confirm_payment = 5; //是否确定支付
    public static final int sign_out= 6; //退出
        public static final int report= 7; //举报


    public static void showDialog(final Context act, final int type, final DialogListener listener){
        View wh = LayoutInflater.from(act).inflate(R.layout.p_dialog, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        TextView tvTitle = wh.findViewById(R.id.tv_title);
        TextView btCancel = wh.findViewById(R.id.bt_cancel);
        TextView btSubmit = wh.findViewById(R.id.bt_submit);
        View view = wh.findViewById(R.id.view);
        switch (type){
            case onTrial:
                tvTitle.setText("观看次数已无\n推广获得更多次数");
                btCancel.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                btSubmit.setText("去推广");
                break;
            case notMember:
                tvTitle.setText("您还不是会员，无法下载哦");
                btCancel.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                break;
            case notLogin:
                String str1 = "您还未登录，" + "<font color='#00CC66'> 前往登录>> </font>";
                tvTitle.setText(Html.fromHtml(str1));
                btCancel.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                break;
            case clear_cache:
                tvTitle.setText("确定清理缓存吗");
                break;
            case confirm_payment:

                break;
            case sign_out:
                tvTitle.setText("确定退出吗");
                break;
            case report:
                tvTitle.setText("确定举报该视频吗?");
                break;
        }
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == onTrial){

                }else {
                    popupWindow.dismiss();
                }
                if (listener != null){
                    listener.onClick();
                }
            }
        });
        /*popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (type == onTrial){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtils.finishActivity(VideoDescAct.class);
                        }
                    }, 500);
                }
            }
        });*/
    }

    public interface DialogListener{
        void onClick();
    }

    public static void showZking(final Context act){
        View wh = LayoutInflater.from(act).inflate(R.layout.p_zking, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        final ImageView ivZking = wh.findViewById(R.id.iv_zking);
        ivZking.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivZking.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Bitmap bitmap = null;
                try {
                    bitmap = ZXingUtils.creatBarcode("https://www.baidu.com/", 100);
                    if (bitmap != null) {
                        ivZking.setImageBitmap(bitmap);
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void showDialog(final Context act,  final DialogListener listener){
        View wh = LayoutInflater.from(act).inflate(R.layout.p_see, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        TextView btSubmit = wh.findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (listener != null){
                    listener.onClick();
                }
            }
        });
    }
}

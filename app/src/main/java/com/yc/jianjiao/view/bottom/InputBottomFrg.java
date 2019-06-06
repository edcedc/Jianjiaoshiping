package com.yc.jianjiao.view.bottom;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseBottomSheetFrag;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.databinding.PInputBinding;
import com.yc.jianjiao.event.KeyInEvent;
import com.yc.jianjiao.event.ZanSuccessInEvent;
import com.yc.jianjiao.utils.SoftKeyboardUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by edison on 2019/1/22.
 */

public class InputBottomFrg extends BaseBottomSheetFrag<BasePresenter, PInputBinding> {

    private AppCompatEditText etText;
    private FrameLayout btSubmit;
    private AppCompatTextView tvSize;
    private ConstraintLayout scrollView;
    private boolean isOne = false;
    private int type;
    private String id;
    private String replyUserId;
    private String byReplyUserId;
    private String parentId;

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        id = bundle.getString("id");
        replyUserId = bundle.getString("replyUserId");
        byReplyUserId = bundle.getString("byReplyUserId");
        parentId = bundle.getString("parentId");
    }

    @Override
    public int bindLayout() {
        return R.layout.p_input;
    }

    @Override
    public void initView(final View view) {
        scrollView = view.findViewById(R.id.scrollView);
        etText = view.findViewById(R.id.et_text);
        btSubmit = view.findViewById(R.id.bt_submit);
        tvSize = view.findViewById(R.id.tv_size);

        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.toString().length();
                if (length > 20){
                    showToast(getString(R.string.error_text_length));
                    return;
                }
                tvSize.setText(length + "/20");
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etText.getText().toString().trim();
                if (StringUtils.isEmpty(text) || text.length() > 20){
                    showToast(getString(R.string.error_text_length));
                    return;
                }
                if (type == 0){
                    if (listener != null)listener.onClick(text);
                }else {
                    if (listener != null)listener.onChildClick(text, id, replyUserId, byReplyUserId, parentId);
                }
                etText.setText("");
                dismiss();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtils.showSoftInput(etText);
            }
        }, 250);
        SoftKeyboardUtil.observeSoftKeyboard(act, new SoftKeyboardUtil.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
//                LogUtils.e(visible, isOne);
                if (!visible && isOne == true){
//                    dismiss();
                }
                isOne = true;
            }
        });
//        SoftKeyboardUtil.observeSoftKeyboard(act, new SoftKeyboardUtil.OnSoftKeyboardChangeListener() {
//            @Override
//            public void onSoftKeyBoardChange(final int softKeybardHeight, boolean visible) {
//                if (visible){
//                    isOne = true;
//                    scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                        @Override
//                        public void onGlobalLayout() {
//                            scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                            int measuredHeight = scrollView.getMeasuredHeight();
//                            LogUtils.e(softKeybardHeight, measuredHeight);
//
//                            ViewGroup.LayoutParams params = scrollView.getLayoutParams();
//                            params.height = measuredHeight + softKeybardHeight - 50;
//                            scrollView.setLayoutParams(params);
//                        }
//                    });
//                }else {
//                    if (isOne){
//                        dismiss();
//                    }
//                }
//            }
//        });

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int measuredHeight = scrollView.getMeasuredHeight();
                ViewGroup.LayoutParams params = scrollView.getLayoutParams();
//                params.height = measuredHeight + 750;
                scrollView.setLayoutParams(params);
            }
        });
    }

    private void getKeyboardHeight() {
        //注册布局变化监听
        act.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //判断窗口可见区域大小
                Rect r = new Rect();
                act.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                int heightDifference = ScreenUtils.getScreenHeight() - (r.bottom - r.top);
                boolean isKeyboardShowing = heightDifference > ScreenUtils.getScreenHeight() / 3;
                if (isKeyboardShowing) {
                    changeScrollView();
                    //移除布局变化监听
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        act.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        act.getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new KeyInEvent());

        KeyboardUtils.hideSoftInput(act);
        LogUtils.e("InputBottomFrg", "onDestroy");
    }

    @Override
    protected void initPresenter() {

    }

    private void changeScrollView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //将ScrollView滚动到底
                scrollView.scrollTo(0, scrollView.getChildAt(0).getHeight() - 500);
            }
        }, 100);
    }

    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    public interface OnClickListener{
        void onClick(String text);
        void onChildClick(String text, String circleId, String replyUserId, String byReplyUserId, String parentId);
    }

    private Activity act;
    public void setActivity(Activity activity) {
        this.act = activity;
    }

}

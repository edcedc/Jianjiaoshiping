package com.yc.jianjiao.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.FFeedBackBinding;
import com.yc.jianjiao.presenter.FeedbackPresenter;
import com.yc.jianjiao.view.impl.FeedbackContract;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Set;

/**
 * Created by edison on 2019/1/29.
 *  意见反馈
 */

public class FeedbackFrg extends BaseFragment<FeedbackPresenter, FFeedBackBinding> implements FeedbackContract.View, View.OnClickListener{

    private String name;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_feed_back;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.feedback));
        mB.refreshLayout.setPureScrollModeOn();
        mB.btSubmit.setOnClickListener(this);
        mPresenter.onRequest();
    }

    @Override
    public void setData(final List<DataBean> listBean) {
        mB.rvLabel.setAdapter(new TagAdapter<DataBean>(listBean) {
            @Override
            public View getView(FlowLayout parent, int position, DataBean dataBean) {
                View view = View.inflate(act, R.layout.i_feed, null);
                RoundTextView tvText = view.findViewById(R.id.tv_text);
                DataBean bean = listBean.get(position);
                tvText.setText(bean.getName());
                tvText.setSelected(bean.isSelect());
                return view;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                RoundTextView tvText = view.findViewById(R.id.tv_text);
                RoundViewDelegate delegate = tvText.getDelegate();
                DataBean bean = listBean.get(position);
                delegate.setStrokeColor(act.getColor(R.color.orange_FF7D2D));
                tvText.setTextColor(act.getColor(R.color.orange_FF7D2D));
                bean.setSelect(true);
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                RoundTextView tvText = view.findViewById(R.id.tv_text);
                RoundViewDelegate delegate = tvText.getDelegate();
                DataBean bean = listBean.get(position);
                delegate.setStrokeColor(act.getColor(R.color.black_333333));
                tvText.setTextColor(act.getColor(R.color.black_333333));
                bean.setSelect(false);
            }
        });

        mB.rvLabel.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                DataBean bean = listBean.get(position);
                name = bean.getName();
                return false;
            }
        });
        mB.rvLabel.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_submit:
                mPresenter.onSubmit(mB.etText.getText().toString(), name);
                break;
        }
    }
}

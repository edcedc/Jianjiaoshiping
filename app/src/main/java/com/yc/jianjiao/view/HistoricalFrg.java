package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.HistoricalAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.databinding.FHistoricalBinding;
import com.yc.jianjiao.presenter.HistoricalPresenter;
import com.yc.jianjiao.view.impl.HistoricalContract;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/29.
 *  历史记录
 */

public class HistoricalFrg extends BaseFragment<HistoricalPresenter, FHistoricalBinding> implements HistoricalContract.View, View.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private HistoricalAdapter adapter;

    private TextView topRight;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        String s = topRight.getText().toString();
        if (s.equals(getString(R.string.edit))){
            setTitle(getString(R.string.historical_record), getString(R.string.cancel));
            mB.lyButton.setVisibility(View.VISIBLE);
            adapter.setEdit(true);
        }else {
            setTitle(getString(R.string.historical_record), getString(R.string.edit));
            mB.lyButton.setVisibility(View.GONE);
            adapter.setEdit(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_historical;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.historical_record), getString(R.string.edit));
        topRight = view.findViewById(R.id.top_right);
        mB.tvAll.setOnClickListener(this);
        mB.tvClear.setOnClickListener(this);
        if (adapter == null) {
            adapter = new HistoricalAdapter(act, listBean);
        }
        setRecyclerViewGridType(mB.recyclerView, 2, 10, R.color.white);
        mB.recyclerView.setAdapter(adapter);

        showLoadDataing();
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1);
            }
        });
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {
        List<DataBean> list = (List<DataBean>) data;
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_all:
                if (adapter.isAllSelect()){
                    adapter.setAllSelect(false);
                }else {
                    adapter.setAllSelect(true);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_clear:
                List<DataBean> temp = new ArrayList<>();
                List<String> listStr = new ArrayList<>();
                for (int i = 0; i < listBean.size(); i++) {
                    DataBean bean = listBean.get(i);
                    if (bean.isSelect()){
                        temp.add(bean);
                        listStr.add(bean.getVideo_history_id());
                    }
                }
                listBean.removeAll(temp);
                adapter.notifyDataSetChanged();
                mPresenter.onDel(listStr);
                break;
        }
    }

    @Override
    public void onDelSuccess() {
        if (listBean.size() == 0){
            showLoadEmpty();
        }else {
            hideLoading();
        }
        setTitle(getString(R.string.historical_record), getString(R.string.edit));
        adapter.setAllSelect(false);
        adapter.setEdit(false);
        adapter.notifyDataSetChanged();
    }
}

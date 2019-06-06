package com.yc.jianjiao.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.HistoricalAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.FHistoricalBinding;
import com.yc.jianjiao.presenter.CollectionPresenter;
import com.yc.jianjiao.presenter.DownloadPresenter;
import com.yc.jianjiao.view.impl.CollectionContract;
import com.yc.jianjiao.view.impl.DownloadContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/29.
 *  下载
 */

public class DownloadFrg extends BaseFragment<DownloadPresenter, FHistoricalBinding> implements DownloadContract.View, View.OnClickListener{

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
            setTitle(getString(R.string.download), getString(R.string.cancel));
            mB.lyButton.setVisibility(View.VISIBLE);
            adapter.setEdit(true);
        }else {
            setTitle(getString(R.string.download), getString(R.string.edit));
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
        setTitle(getString(R.string.download), getString(R.string.edit));
        topRight = view.findViewById(R.id.top_right);
        mB.tvAll.setOnClickListener(this);
        mB.tvClear.setOnClickListener(this);
        if (adapter == null) {
            adapter = new HistoricalAdapter(act, listBean, 1);
        }
        setRecyclerViewGridType(mB.recyclerView, 2, 10, R.color.white);
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.setPureScrollModeOn();
        mPresenter.onRequest(pagerNumber = 1);
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
                        listStr.add(bean.getCover());
                    }
                }
                listBean.removeAll(temp);
                adapter.notifyDataSetChanged();

                for (String name:listStr){
                    FileUtils.deleteFile(name);
                }

                if (listBean.size() == 0){
                    showLoadEmpty();
                    setTitle(getString(R.string.download), getString(R.string.edit));
                    adapter.setAllSelect(false);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}

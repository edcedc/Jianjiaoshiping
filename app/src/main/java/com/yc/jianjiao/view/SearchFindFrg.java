package com.yc.jianjiao.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.flyco.roundview.RoundTextView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.FileAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.bean.SaveSearchFindListBean;
import com.yc.jianjiao.databinding.FSearchStarBinding;
import com.yc.jianjiao.presenter.SearchFindPresenter;
import com.yc.jianjiao.view.impl.SearchFindContract;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/3/20.
 *  搜索发现
 */

public class SearchFindFrg extends BaseFragment<SearchFindPresenter, FSearchStarBinding> implements SearchFindContract.View, View.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private FileAdapter adapter;
    private String content;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_search_star;
    }

    @Override
    protected void initView(View view) {
        mB.tvSearch.setOnClickListener(this);
        mB.fyDel.setOnClickListener(this);
        mB.ivDelSearch.setOnClickListener(this);
        mB.fyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.onBackPressed();
            }
        });
        mPresenter.onHotLabel(1);
        setSearchData();
        mB.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    mB.etSearch.setText(mB.etSearch.getText().toString().trim());
                    mB.swipeLoadDataLayout.setVisibility(View.VISIBLE);
                    mB.scrollView.setVisibility(View.GONE);
                    mB.refreshLayout.startRefresh();
                    content = mB.etSearch.getText().toString().trim();
//                    mPresenter.onSearch(mB.etSearch.getText().toString().trim(), pagerNumber = 1);
                    return true;
                }
                return false;
            }
        });
        mB.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (editable.length() == 0){
                            mB.tvSearch.setText("搜索");
                            mB.ivDelSearch.setVisibility(View.GONE);
                        }else {
                            mB.tvSearch.setText("取消");
                            mB.ivDelSearch.setVisibility(View.VISIBLE);
                        }
                    }
                }, 300);
            }
        });

        if (adapter == null) {
            adapter = new FileAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  20));
//        mB.recyclerView.setBackgroundColor(act.getColor(R.color.white));
        mB.recyclerView.setAdapter(adapter);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onSearch(content, pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onSearch(content, pagerNumber += 1);
            }
        });
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public void setSearchData() {
        final List<SaveSearchFindListBean> all = LitePal.findAll(SaveSearchFindListBean.class);
        if (all != null && all.size() != 0){
            mB.rvHistoricalRecords.removeAllViews();
            mB.rvHistoricalRecords.setAdapter(new TagAdapter<SaveSearchFindListBean>(all){
                @Override
                public View getView(FlowLayout parent, int position, SaveSearchFindListBean dataBean) {
                    View view = View.inflate(act, R.layout.i_search_label, null);
                    TextView tvText = view.findViewById(R.id.tv_text);
                    SaveSearchFindListBean bean = all.get(position);
                    tvText.setText(bean.getMessage());
                    return view;
                }
            });

            mB.rvHistoricalRecords.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    SaveSearchFindListBean bean = all.get(position);
                    mB.etSearch.setText(bean.getMessage());
                    mB.swipeLoadDataLayout.setVisibility(View.VISIBLE);
                    mB.scrollView.setVisibility(View.GONE);
                    mB.refreshLayout.startRefresh();
                    content = mB.etSearch.getText().toString().trim();
//                    mPresenter.onSearch(bean.getMessage(), pagerNumber = 1);
                    return false;
                }
            });
        }
    }

    @Override
    public void setData(List<DataBean> list, String text) {
        mB.scrollView.setVisibility(View.GONE);
        mB.swipeLoadDataLayout.setVisibility(View.VISIBLE);
        this.content = text;

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
    public void setHotData(final List<DataBean> listBean) {
        mB.rvHotSearch.setAdapter(new TagAdapter<DataBean>(listBean) {
            @Override
            public View getView(FlowLayout parent, int position, DataBean dataBean) {
                View view = View.inflate(act, R.layout.i_search_label, null);
                RoundTextView tvText = view.findViewById(R.id.tv_text);
                DataBean bean = listBean.get(position);
                tvText.setText(bean.getMessage());
                tvText.setSelected(bean.isSelect());
                return view;
            }
        });

        mB.rvHotSearch.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                DataBean bean = listBean.get(position);
                mB.etSearch.setText(bean.getMessage());
                mB.swipeLoadDataLayout.setVisibility(View.VISIBLE);
                mB.scrollView.setVisibility(View.GONE);
                mB.refreshLayout.startRefresh();
                content = mB.etSearch.getText().toString().trim();
//                mPresenter.onSearch(bean.getMessage(), pagerNumber = 1);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fy_del:
                LitePal.deleteAll(SaveSearchFindListBean.class);
                mB.rvHistoricalRecords.removeAllViews();
                break;
            case R.id.tv_search:
                String text = mB.tvSearch.getText().toString();
                if (text.equals("搜索")){
                    mB.swipeLoadDataLayout.setVisibility(View.VISIBLE);
                    mB.scrollView.setVisibility(View.GONE);
                    mB.refreshLayout.startRefresh();
                    content = mB.etSearch.getText().toString().trim();
//                    mPresenter.onSearch(text, pagerNumber = 1);
                }else {
                    mB.swipeLoadDataLayout.setVisibility(View.GONE);
                    mB.scrollView.setVisibility(View.VISIBLE);
                    mB.etSearch.setText("");
                }
                break;
            case R.id.iv_del_search:
                mB.tvSearch.setText("");
                break;
        }
    }
}

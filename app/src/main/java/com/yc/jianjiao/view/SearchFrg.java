package com.yc.jianjiao.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.MyPagerAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.bean.SaveSearchListBean;
import com.yc.jianjiao.databinding.FSearchBinding;
import com.yc.jianjiao.presenter.SearchPresenter;
import com.yc.jianjiao.view.impl.SearchContract;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by edison on 2019/1/22.
 *  搜索
 */

public class SearchFrg extends BaseFragment<SearchPresenter, FSearchBinding> implements SearchContract.View, View.OnClickListener{

    private int type;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_search;
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
        mPresenter.onHotLabel(type);
        mPresenter.onHomeLabel();
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
                    mB.refreshLayout.setVisibility(View.VISIBLE);
                    mB.scrollView.setVisibility(View.GONE);
                    mPresenter.onSearch(mB.etSearch.getText().toString().trim());
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fy_del:
                LitePal.deleteAll(SaveSearchListBean.class);
                mB.rvHistoricalRecords.removeAllViews();
                break;
            case R.id.tv_search:
                String text = mB.tvSearch.getText().toString();
                if (text.equals("搜索")){
                    mB.refreshLayout.setVisibility(View.VISIBLE);
                    mB.scrollView.setVisibility(View.GONE);
                    mPresenter.onSearch(text);
                }else {
                    mB.refreshLayout.setVisibility(View.GONE);
                    mB.scrollView.setVisibility(View.VISIBLE);
                    mB.etSearch.setText("");
                }
                break;
            case R.id.iv_del_search:
                mB.tvSearch.setText("");
                break;
        }
    }

    @Override
    public void setSearchData() {
        final List<SaveSearchListBean> all = LitePal.findAll(SaveSearchListBean.class);
        if (all != null && all.size() != 0){
            mB.rvHistoricalRecords.removeAllViews();
            mB.rvHistoricalRecords.setAdapter(new TagAdapter<SaveSearchListBean>(all){
                @Override
                public View getView(FlowLayout parent, int position, SaveSearchListBean dataBean) {
                    View view = View.inflate(act, R.layout.i_search_label, null);
                    TextView tvText = view.findViewById(R.id.tv_text);
                    SaveSearchListBean bean = all.get(position);
                    tvText.setText(bean.getMessage());
                    return view;
                }
            });

            mB.rvHistoricalRecords.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    SaveSearchListBean bean = all.get(position);
                    mB.etSearch.setText(bean.getMessage());
                    mB.refreshLayout.setVisibility(View.VISIBLE);
                    mB.scrollView.setVisibility(View.GONE);
                    mPresenter.onSearch(bean.getMessage());
                    return false;
                }
            });
        }
    }

    @Override
    public void setData(final List<DataBean> listBean) {

    }

    @Override
    public void setSearchList(List<DataBean> list) {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        String[] strings = new String[list.size()];
        for (int i = 0;i < list.size();i++){
            DataBean bean = list.get(i);
            strings[i] = bean.getName();
            SearchChildFrg frg = new SearchChildFrg();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            bundle.putString("id", bean.getId());
//            bundle.putString("content", mB.etSearch.getText().toString());
            frg.setArguments(bundle);
            mFragments.add(frg);
        }
        mB.viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragments, strings));
        mB.tbLayout.setViewPager(mB.viewPager);
        mB.viewPager.setOffscreenPageLimit(list.size() - 1);
        mB.tbLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mB.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
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
                mB.refreshLayout.setVisibility(View.VISIBLE);
                mB.scrollView.setVisibility(View.GONE);
                mPresenter.onSearch(bean.getMessage());
                return false;
            }
        });
    }

    private void setHistorySQL(DataBean bean){
        SaveSearchListBean searchListBean = new SaveSearchListBean();
//        searchListBean.setIds(bean.getId());
        searchListBean.setMessage(bean.getMessage());
//        searchListBean.setNumber(bean.getNumber());
//        searchListBean.setType(bean.getType());
        searchListBean.save();
    }

}

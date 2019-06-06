package com.yc.jianjiao.view;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.ScrollView;

import com.blankj.utilcode.util.ToastUtils;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.StarChildAdapter;
import com.yc.jianjiao.adapter.VideoCommentAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.databinding.FDynamicDetailsBinding;
import com.yc.jianjiao.event.ZanSuccessInEvent;
import com.yc.jianjiao.presenter.DynamicDetailsPresenter;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.bottom.CommentBottomFrg;
import com.yc.jianjiao.view.bottom.InputBottomFrg;
import com.yc.jianjiao.view.bottom.ShareBottomFrg;
import com.yc.jianjiao.view.impl.DynamicDetailsContract;
import com.yc.jianjiao.weight.CircleImageView;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;
import com.yc.jianjiao.weight.WithScrollGridView;
import com.yc.jianjiao.weight.X5WebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/29.
 * 动态详情
 */

public class DynamicDetailsFrg extends BaseFragment<DynamicDetailsPresenter, FDynamicDetailsBinding> implements DynamicDetailsContract.View, View.OnClickListener {

    private String id;
    private int position;
    private DataBean bean;

    public static DynamicDetailsFrg newInstance() {
        Bundle args = new Bundle();
        DynamicDetailsFrg fragment = new DynamicDetailsFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listBean = new ArrayList<>();
    private VideoCommentAdapter commentAdapter;

    CircleImageView ivHead;
    AppCompatTextView tvTitle;
    AppCompatTextView tvNick;
    AppCompatTextView tvContent;
    AppCompatTextView tvContent2;
    AppCompatTextView tvComment;
    AppCompatTextView tvZan;
    AppCompatTextView tvShare;
    RoundTextView tvLabel;
    WithScrollGridView gridView;
    X5WebView webView;
    private ShareBottomFrg frg;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        position = bundle.getInt("position");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_dynamic_details;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.dynamic_details));
        ivHead = view.findViewById(R.id.iv_head);
        tvTitle = view.findViewById(R.id.tv_title);
        tvNick = view.findViewById(R.id.tv_nick);
        tvContent = view.findViewById(R.id.tv_content);
        tvContent2 = view.findViewById(R.id.tv_content2);
        gridView = view.findViewById(R.id.gridView);
        tvComment = view.findViewById(R.id.tv_comment);
        tvComment.setOnClickListener(this);
        tvZan = view.findViewById(R.id.tv_zan);
        tvShare = view.findViewById(R.id.tv_share);
        tvLabel = view.findViewById(R.id.tv_label);
        webView = view.findViewById(R.id.webView);
        tvZan.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvComment.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        mB.btShare.setOnClickListener(this);
        EventBus.getDefault().register(this);
        frg = new ShareBottomFrg();
        if (commentAdapter == null) {
            commentAdapter = new VideoCommentAdapter(act, listBean, Constants.CIRCLE_TYPE, false);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  2));
        mB.recyclerView.setAdapter(commentAdapter);
        commentAdapter.setOnClickListener(new VideoCommentAdapter.OnClickListener() {
            @Override
            public void onClick(int position, List<DataBean> list, DataBean bean) {
                CommentBottomFrg frg = new CommentBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putInt("position", position);
                bundle.putString("bean", new Gson().toJson(bean));
                frg.setArguments(bundle);
                frg.setActivity(act);
                frg.show(getChildFragmentManager(), "dialog");

//                UIHelper.startDynamicItemFrg(DynamicDetailsFrg.this);
            }

            @Override
            public void onComment(final int position, final int i, String circle_id, String userId, String byReplyUserId, String parentId, final int commentType, final String id) {
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("id", circle_id);
                bundle.putString("replyUserId", userId);
                bundle.putString("byReplyUserId", byReplyUserId);
                bundle.putString("parentId", parentId);
                inputBottomFrg.setArguments(bundle);
                inputBottomFrg.show(getChildFragmentManager(), "dialog");
                inputBottomFrg.setActivity(act);
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                    }

                    @Override
                    public void onChildClick(String text, String circleId, String replyUserId, String byReplyUserId, String parentId) {
                        mPresenter.onChildComment(position, i, text, circleId, replyUserId, byReplyUserId, parentId, commentType, id);
                    }
                });
            }


            @Override
            public void onZan(int position, String id, int isPraise) {
                mPresenter.onChildPraise(position, id, isPraise);
            }
        });

        showLoadDataing();
        mB.refreshLayout.startRefresh();
        mPresenter.onCircleDetail(id);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, id);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1, id);
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
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
            case R.id.tv_comment:
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putString("id", id);
                inputBottomFrg.setArguments(bundle);
                inputBottomFrg.show(getChildFragmentManager(), "dialog");
                inputBottomFrg.setActivity(act);
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                        mPresenter.onComment(text,  id);
                    }

                    @Override
                    public void onChildClick(String text,String circleId, String replyUserId, String byReplyUserId, String parentId) {

                    }
                });
                break;
            case R.id.bt_share:
                frg.show(getChildFragmentManager(), "dialog");
                break;
            /*case R.id.tv_comment:
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                if (inputBottomFrg != null && !inputBottomFrg.isShowing()){
                    inputBottomFrg.show(getChildFragmentManager(), "dialog");
                    inputBottomFrg.setActivity(act);
                }
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                        LogUtils.e(text);
                    }
                });
                break;*/
            case R.id.tv_zan:
                int circlePraise = bean.getIsCirclePraise();
                if (circlePraise == 0) {
                    mPresenter.onPraise(position, id, 2);
                } else {
                    mPresenter.onPraise(position, id, 0);
                }
                break;
        }
    }

    @Override
    public void onZanSuccess(int type) {
        if (type == 0){
            bean.setPraise(bean.getPraise() + 1);
        }else {
            bean.setPraise(bean.getPraise() - 1);
        }
        bean.setIsCirclePraise(type);
        tvZan.setText(bean.getPraise() + "");
        if (type != 0){
            tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.home_p02),
                    null, null, null);
        }else {
            tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.add_a05),
                    null, null, null);
        }
    }

    @Override
    public void onCommentSuccess(DataBean data) {
        listBean.add(0, data);
        commentAdapter.notifyDataSetChanged();

//        mB.scrollView.post(new Runnable() {
//
//            @Override
//            public void run() {
//                mB.scrollView.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });
    }

    @Override
    public void setCircleDetail(DataBean data) {
        bean = data;
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getHead(), ivHead);
        tvTitle.setText(bean.getNick());
        tvNick.setText(bean.getSubhead());
        tvContent.setText(bean.getTitle());
//        tvContent2.setText(bean.getRemark());
        tvContent2.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.loadDataWithBaseURL(null, bean.getContent(), "text/html", "utf-8", null);
        webView.setInitialScale(100);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                ToastUtils.showShort("网页加载失败");
            }
        });
        tvLabel.setText(bean.getLabel_name());
        tvZan.setText(bean.getPraise() + "");
        tvShare.setText(bean.getShare() + "");
        tvComment.setText(bean.getComment_number() + "");

        int circlePraise = bean.getIsCirclePraise();
        if (circlePraise == 0) {
            tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.add_a05),
                    null, null, null);
        } else {
            tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.home_p02),
                    null, null, null);
        }

        List<DataBean> imageList = bean.getImageList();
        if (imageList != null) {
            gridView.setVisibility(View.VISIBLE);
            StarChildAdapter adapter = new StarChildAdapter(act, imageList);
            gridView.setAdapter(adapter);
        } else {
            gridView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onChildZanSuccess(int position, int isPraise) {
        DataBean bean = listBean.get(position);
        int praise = bean.getPraise();
        if (isPraise == 1){
            isPraise = 0;
            bean.setPraise(praise -= 1);
        }else {
            isPraise = 1;
            bean.setPraise(praise += 1);
        }
        bean.setIsPraise(isPraise);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildCommentSuccess(int position, int i, DataBean data) {
        DataBean bean = listBean.get(position);
        DataBean.CircleCommentBean circleComment = bean.getCircleComment();
        if (circleComment == null){
            DataBean.CircleCommentBean circleCommentBean = new DataBean.CircleCommentBean();
            List<DataBean> list = new ArrayList<>();
            list.add(data);
            circleCommentBean.setList(list);
            bean.setCircleComment(circleCommentBean);
        }else {
            List<DataBean> list = circleComment.getList();
            list.add(data);
        }
        commentAdapter.notifyDataSetChanged();

//        mB.scrollView.post(new Runnable() {
//
//            @Override
//            public void run() {
//                mB.scrollView.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });
    }

    @Subscribe
    public void onMainZanInEvent(ZanSuccessInEvent event){
        onChildZanSuccess(event.position, event.isPraise);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

package com.yc.jianjiao.view.bottom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.view.KeyEvent;
import android.view.View;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.VideoCommentAdapter;
import com.yc.jianjiao.base.BaseBottomSheetFrag;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.databinding.PCommentBinding;
import com.yc.jianjiao.event.ZanSuccessInEvent;
import com.yc.jianjiao.presenter.CommentBottomPresenter;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.view.CommentFrg;
import com.yc.jianjiao.view.impl.CommentBottomContract;
import com.yc.jianjiao.weight.CircleImageView;
import com.yc.jianjiao.weight.LinearDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public class CommentBottomFrg extends BaseBottomSheetFrag<CommentBottomPresenter, PCommentBinding> implements CommentBottomContract.View, View.OnClickListener {

    private List<DataBean> listBean = new ArrayList<>();
    private VideoCommentAdapter adapter;

    CircleImageView ivHead;
    AppCompatTextView tvName;
    AppCompatTextView tvTime;
    AppCompatTextView tvCommentSize;
    AppCompatTextView tvZan;
    AppCompatTextView tvContent;
    AppCompatTextView tvLook;
    private DataBean bean;
    private int type;
    private String id;
    private int position;
    private int isPraise;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
            case R.id.tv_comment_size:
//                String s = mB.etText.getText().toString();
//                if (StringUtils.isEmpty(s)) {
//                    return;
//                }
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("id", id);
                bundle.putString("replyUserId", User.getInstance().getUserId());
                bundle.putString("byReplyUserId", bean.getUser_id());
                bundle.putString("parentId", bean.getParent_id() == null ? bean.getId() : bean.getParent_id());
                inputBottomFrg.setArguments(bundle);
                inputBottomFrg.show(getChildFragmentManager(), "dialog");
                inputBottomFrg.setActivity(act);
                inputBottomFrg.setOnClickListener(new InputBottomFrg.OnClickListener() {
                    @Override
                    public void onClick(final String text) {
                    }

                    @Override
                    public void onChildClick(String text, String circleId, String replyUserId, String byReplyUserId, String parentId) {
                        mPresenter.onComment(0, text, circleId, replyUserId, byReplyUserId, parentId, type, bean.getId());
                    }
                });
                break;
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_zan:
                mPresenter.onPraise(position, bean.getId(), isPraise, type);
                break;
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        id = bundle.getString("id");
        position = bundle.getInt("position");
        bean = new Gson().fromJson(bundle.getString("bean"), DataBean.class);
    }

    @Override
    public int bindLayout() {
        return R.layout.p_comment;
    }

    @Override
    public void initView(View view) {
        ivHead = view.findViewById(R.id.iv_head);
        tvName = view.findViewById(R.id.tv_name);
        tvTime = view.findViewById(R.id.tv_time);
        tvCommentSize = view.findViewById(R.id.tv_comment_size);
        tvZan = view.findViewById(R.id.tv_zan);
        tvContent = view.findViewById(R.id.tv_content);
        tvLook = view.findViewById(R.id.tv_look);
        mB.ivClose.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        tvZan.setOnClickListener(this);
        tvCommentSize.setOnClickListener(this);
        mB.refreshLayout.setEnableOverScroll(false);

        if (bean == null) return;
        isPraise = bean.getIsPraise();
        if (isPraise == 0){
            tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.home_p02),
                    null, null, null);
        }else {
            tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.add_a05),
                    null, null, null);
        }
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getHead(), ivHead);
        tvName.setText(bean.getNick());
        tvTime.setText(bean.getCreate_time());
//        tvCommentSize.setText("123");
        tvZan.setText(bean.getPraise() + "");
        String emoji_content = bean.getEmoji_content();
        if (!StringUtils.isEmpty(emoji_content)) {
            tvContent.setText(new String(EncodeUtils.base64Decode(emoji_content.getBytes())));
        }

        if (adapter == null) {
            adapter = new VideoCommentAdapter(act, listBean, type, false);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2));
        mB.recyclerView.setAdapter(adapter);

        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, type, bean.getId());
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1, type, bean.getId());
            }
        });

        adapter.setOnClickListener(new VideoCommentAdapter.OnClickListener() {
            @Override
            public void onClick(int position, List<DataBean> list, DataBean bean) {

            }

            @Override
            public void onComment(final int position, final int i, String circle_id, String userId, String byReplyUserId, String parentId, final int commentType, final String id) {
                InputBottomFrg inputBottomFrg = new InputBottomFrg();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("id", CommentBottomFrg.this.id);
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
                        mPresenter.onChildComment(position, i, text, circleId, replyUserId, byReplyUserId, parentId, type, id);
                    }
                });

            }

            @Override
            public void onZan(int position, String id, int isPraise) {
                mPresenter.onChildPraise(position, id, isPraise, type);
            }
        });
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildCommentSuccess(int position, int i, DataBean bean) {
//        DataBean bean = listBean.get(position);
//        DataBean.CircleCommentBean circleComment = bean.getCircleComment();
//        if (circleComment == null){
//            DataBean.CircleCommentBean circleCommentBean = new DataBean.CircleCommentBean();
//            List<DataBean> list = new ArrayList<>();
//            list.add(data);
//            circleCommentBean.setList(list);
//            bean.setCircleComment(circleCommentBean);
//        }else {
//            List<DataBean> list = circleComment.getList();
//            list.add(data);
//        }
        if (type == Constants.VIDEO_TYPE){
            DataBean.PageCommentBean pageCommentBean = new DataBean.PageCommentBean();
            List<DataBean> listVideo = new ArrayList<>();
            DataBean beanVideo = new DataBean();
            beanVideo.setNick(bean.getNick());
            beanVideo.setReply_user_id(bean.getReply_user_id());
            beanVideo.setReply_nick(bean.getReply_nick());
            beanVideo.setEmoji_content(bean.getLevel_emoji_content());
            beanVideo.setContent(bean.getLevel_content());
            beanVideo.setUser_id(bean.getUser_id());
            listVideo.add(beanVideo);
            pageCommentBean.setList(listVideo);
            bean.setPageComment(pageCommentBean);
        }else {
            DataBean.CircleCommentBean circleCommentBean = new DataBean.CircleCommentBean();
            List<DataBean> listCircle = new ArrayList<>();
            DataBean dataBean = new DataBean();
            dataBean.setNick(bean.getNick());
            dataBean.setReply_user_id(bean.getReply_user_id());
            dataBean.setReply_nick(bean.getReply_nick());
            dataBean.setEmoji_content(bean.getLevel_emoji_content());
            dataBean.setContent(bean.getLevel_content());
            dataBean.setUser_id(bean.getUser_id());
            listCircle.add(dataBean);
            circleCommentBean.setList(listCircle);
            bean.setCircleComment(circleCommentBean);
        }

        listBean.remove(position);
        listBean.add(position, bean);
//        adapter.notifyItemRemoved(position);
//        adapter.notifyItemInserted(position);
//        adapter.notifyItemChanged(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onZanSuccess(int position, int isPraise) {
        int praise = bean.getPraise();
        if (isPraise == 1){
            praise -= 1;
            this.isPraise = 0;
            tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.home_p02),
                    null, null, null);
        }else {
            praise += 1;
            this.isPraise = 1;
            tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.add_a05),
                    null, null, null);
        }
        bean.setPraise(praise);
        tvZan.setText(bean.getPraise() + "");
        LogUtils.e(this.isPraise);
        EventBus.getDefault().post(new ZanSuccessInEvent(position, isPraise));
    }

    @Override
    public void onCommentSuccess(int i, DataBean data) {
        listBean.add(0, data);
        adapter.notifyDataSetChanged();
    }


    private Activity act;
    public void setActivity(Activity activity) {
        this.act = activity;
    }
}

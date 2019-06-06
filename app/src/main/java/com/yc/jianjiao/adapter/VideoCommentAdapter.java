package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.CircleImageView;
import com.yc.jianjiao.weight.WithScrollListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/26.
 */

public class VideoCommentAdapter extends BaseRecyclerviewAdapter<DataBean> {

    public VideoCommentAdapter(Context act, List<DataBean> listBean, int type, boolean isLock) {
        super(act, listBean);
        this.type = type;
        this.isLock = isLock;
    }
    public VideoCommentAdapter(Context act, List<DataBean> listBean, int type, boolean isLock, boolean allLock) {
        super(act, listBean);
        this.type = type;
        this.isLock = isLock;
        this.allLock = allLock;
    }

    public VideoCommentAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    private boolean isLock = false, allLock;
    private int type;

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);

        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getHead(), viewHolder.ivHead);
        viewHolder.tvName.setText(bean.getNick());
        viewHolder.tvTime.setText(bean.getCreate_time());
//        viewHolder.tvCommentSize.setText("123");
        viewHolder.tvZan.setText(bean.getPraise() + "");
        String emoji_content = bean.getEmoji_content();
        if (!StringUtils.isEmpty(emoji_content)) {
            viewHolder.tvContent.setText(new String(EncodeUtils.base64Decode(emoji_content.getBytes())));
        }


        final int isPraise = bean.getIsPraise();
        if (isPraise == 0) {
            viewHolder.tvZan.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, act.getDrawable(R.mipmap.home_p02), null);
        } else {
            viewHolder.tvZan.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, act.getDrawable(R.mipmap.add_a05), null);
        }
        viewHolder.tvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onZan(position, bean.getId(), isPraise);
            }
        });

        viewHolder.tvCommentSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id;
                if (type == Constants.VIDEO_TYPE) {
                    id = bean.getVideo_id();
                } else {
                    id = bean.getCircle_id();
                }
                if (listener != null)
                    listener.onComment(position, 0, id, User.getInstance().getUserId(), bean.getReply_user_id(), bean.getParent_id() == null ? bean.getId() : bean.getParent_id(), 0, bean.getId());
            }
        });
        if (type == Constants.VIDEO_TYPE) {
            viewHolder.tvCeng.setVisibility(View.GONE);
        } else {
            viewHolder.tvCeng.setVisibility(View.VISIBLE);
            viewHolder.tvCeng.setText((position + 1) + "楼  ");
        }


        final List<DataBean> list = new ArrayList<>();
        final VideoCommentChildAdapter adapter = new VideoCommentChildAdapter(act, list);
        viewHolder.listView.setAdapter(adapter);
        viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataBean childBean = list.get(i);
                String id;
                if (type == Constants.VIDEO_TYPE) {
                    id = bean.getVideo_id();
                } else {
                    id = bean.getCircle_id();
                }
                if (listener != null && isLock)
                    listener.onComment(position, i, id, User.getInstance().getUserId(), childBean.getReply_user_id(), childBean.getParent_id(), 1, bean.getId());
            }
        });
        int totalRow = 0;
        if (type == Constants.VIDEO_TYPE) {
            DataBean.PageCommentBean pageComment = bean.getPageComment();
            if (pageComment != null) {
                totalRow = pageComment.getTotalRow();
                list.addAll(pageComment.getList());
            }
        } else {
            DataBean.CircleCommentBean circleComment = bean.getCircleComment();
            if (circleComment != null) {
                totalRow = circleComment.getTotalRow();
                list.addAll(circleComment.getList());
            }
        }
        adapter.notifyDataSetChanged();
        viewHolder.listView.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        final ViewHolder finalViewHolder = viewHolder;

        if (list.size() > 5 || totalRow > 5) {
            viewHolder.tvLook.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvLook.setVisibility(View.GONE);
        }
        viewHolder.tvLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allLock) {
                    boolean look = adapter.isLook();
                    if (!look) {
                        adapter.setLook(true);
                        finalViewHolder.tvLook.setText("收起");
                    } else {
                        adapter.setLook(false);
                        finalViewHolder.tvLook.setText("查看全部回复>");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    if (listener != null) {
                        bean.setType(type);
                        listener.onClick(position, null, bean);
                    }
                }
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_video_comment, parent, false));
    }

    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClick(int position, List<DataBean> list, DataBean bean);

        void onComment(int position, int i, String video_id, String userId, String byReplyUserId, String parentId, int type, String id);

        void onZan(int position, String id, int isPraise);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivHead;
        AppCompatTextView tvName;
        AppCompatTextView tvTime;
        AppCompatTextView tvCommentSize;
        AppCompatTextView tvZan;
        AppCompatTextView tvContent;
        AppCompatTextView tvLook;
        AppCompatTextView tvCeng;
        WithScrollListView listView;

        public ViewHolder(View itemView) {
            super(itemView);
            ivHead = itemView.findViewById(R.id.iv_head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvCeng = itemView.findViewById(R.id.tv_ceng);
            tvCommentSize = itemView.findViewById(R.id.tv_comment_size);
            tvZan = itemView.findViewById(R.id.tv_zan);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvLook = itemView.findViewById(R.id.tv_look);
            listView = itemView.findViewById(R.id.listView);
        }
    }

}

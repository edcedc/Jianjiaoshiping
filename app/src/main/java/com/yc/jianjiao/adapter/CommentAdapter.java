package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.CircleImageView;

import java.util.List;

/**
 * Created by edison on 2019/1/24.
 */

public class CommentAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public CommentAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);

        GlideLoadingUtils.load(act, "", viewHolder.ivHead);
        GlideLoadingUtils.load(act, "", viewHolder.ivImg);
        viewHolder.tvName.setText("昵称");
        viewHolder.tvTime.setText("2018-12-17 ");
        viewHolder.tvContent.setText("评论评论评论评论评论评论评论评论 评论评评论评 ");
        viewHolder.tvComment.setText("评论评论评论评论评论评论评论评论");

        viewHolder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UIHelper.startDynamicDetailsAct(bean.getId());
            }
        });
    }

    private onClickListener listener;
    public void setOnClickListener(onClickListener listener){
        this.listener = listener;
    }
    public interface onClickListener{
        void onClick(int position);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_comment, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView ivHead;
        AppCompatTextView tvName;
        AppCompatTextView tvTime;
        ImageView ivComment;
        ImageView ivImg;
        AppCompatTextView tvContent;
        AppCompatTextView tvComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ivHead = itemView.findViewById(R.id.iv_head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivComment = itemView.findViewById(R.id.iv_comment);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvComment = itemView.findViewById(R.id.tv_comment);
            ivImg = itemView.findViewById(R.id.iv_img);
        }
    }

}

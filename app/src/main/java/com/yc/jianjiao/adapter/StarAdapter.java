package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.roundview.RoundTextView;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.CircleImageView;
import com.yc.jianjiao.weight.WithScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/28.
 */

public class StarAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public StarAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);

        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getHead(), viewHolder.ivHead);
        viewHolder.tvTitle.setText(bean.getNick());
        viewHolder.tvNick.setText(bean.getRemark());
        viewHolder.tvContent.setText(bean.getTitle());
        viewHolder.tvContent2.setText(bean.getRemark());

        List<DataBean> imageList = bean.getImageList();
        if (imageList != null){
            viewHolder.gridView.setVisibility(View.VISIBLE);
            StarChildAdapter adapter = new StarChildAdapter(act, imageList);
            viewHolder.gridView.setAdapter(adapter);
        }else {
            viewHolder.gridView.setVisibility(View.GONE);
        }

        viewHolder.tvLabel.setText(bean.getLabel_name());
        viewHolder.tvZan.setText(bean.getPraise() + "");
        viewHolder.tvShare.setText(bean.getShare() + "");
        viewHolder.tvComment.setText(bean.getComment_number() + "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startDynamicDetailsAct(position, bean.getId());
            }
        });

        final int circlePraise = bean.getCirclePraise();
        if (circlePraise == 0){
            viewHolder.tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.add_a05),
                    null, null, null);
        }else {
            viewHolder.tvZan.setCompoundDrawablesWithIntrinsicBounds(act.getDrawable(R.mipmap.home_p02),
                    null, null, null);
        }

        viewHolder.tvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                     if (circlePraise == 0){
                        listener.onZan(position, bean.getId(), 2);
                    }else {
                        listener.onZan(position, bean.getId(), 0);
                    }
                }
            }
        });
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
    public interface OnClickListener{
        void onZan(int position, String id, int type);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_star, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

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

        public ViewHolder(View itemView) {
            super(itemView);
            ivHead = itemView.findViewById(R.id.iv_head);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvNick = itemView.findViewById(R.id.tv_nick);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvContent2 = itemView.findViewById(R.id.tv_content2);
            gridView = itemView.findViewById(R.id.gridView);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvZan = itemView.findViewById(R.id.tv_zan);
            tvShare = itemView.findViewById(R.id.tv_share);
            tvLabel = itemView.findViewById(R.id.tv_label);
        }
    }

}

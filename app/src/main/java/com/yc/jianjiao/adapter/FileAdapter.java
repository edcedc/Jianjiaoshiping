package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flyco.roundview.RoundTextView;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.holder.RecyclerItemNormalHolder;
import com.yc.jianjiao.adapter.holder.RecyclerItemViewHolder;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.SampleCoverVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/28.
 */

public class FileAdapter extends RecyclerView.Adapter {

    private Context act;
    private List<DataBean> listBean = new ArrayList<>();

    public FileAdapter(Context act, List<DataBean> listBean) {
        this.act = act;
        this.listBean = listBean;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(act).inflate(R.layout.i_file, parent, false);
        final RecyclerView.ViewHolder holder = new RecyclerItemNormalHolder(act, itemView);

        tvTitle = itemView.findViewById(R.id.tv_title);
        tvContent = itemView.findViewById(R.id.tv_content);
        ivComment = itemView.findViewById(R.id.iv_comment);
        ivZan = itemView.findViewById(R.id.iv_zan);
        ivShare = itemView.findViewById(R.id.iv_share);
        ivImg = itemView.findViewById(R.id.iv_img);
        tvLabel = itemView.findViewById(R.id.tv_label);
        lyItem = itemView.findViewById(R.id.ly_item);
        listItemContainer = itemView.findViewById(R.id.list_item_container);
        listItemBtn = itemView.findViewById(R.id.list_item_btn);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerItemNormalHolder recyclerItemViewHolder = (RecyclerItemNormalHolder) holder;
        recyclerItemViewHolder.setRecyclerBaseAdapter(this);
        recyclerItemViewHolder.onBind(position, listBean.get(position));
        onBindViewHolde(recyclerItemViewHolder, position);
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    protected void onBindViewHolde(RecyclerItemNormalHolder holder, final int position) {
//        final ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);

        ivZan.setBackgroundResource(bean.getIsCollect() == 0 ? R.mipmap.home_a09 : R.mipmap.add_a04);

        tvTitle.setText(bean.getTitle());
        tvContent.setText(bean.getVideo_desc());
//        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getCover_image(), viewHolder.ivImg);
        tvLabel.setText(bean.getLabel_name());
        lyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startVideoDescAct(bean.getId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.item(position);
            }
        });

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.comment(position);
            }
        });
        ivZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.zan(position, bean.getId(), bean.getIsCollect());
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.share(position);
            }
        });
    }

    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void comment(int position);

        void zan(int i, String id, int isCollect);

        void share(int position);

        void item(int position);
    }


    AppCompatTextView tvTitle;
    AppCompatTextView tvContent;
    ImageView ivComment;
    ImageView ivZan;
    ImageView ivShare;
    ImageView ivImg;
    RoundTextView tvLabel;
    View lyItem;
    View listItemContainer;
    View listItemBtn;

    class ViewHolder extends RecyclerItemNormalHolder {

        AppCompatTextView tvTitle;
        AppCompatTextView tvContent;
        ImageView ivComment;
        ImageView ivZan;
        ImageView ivShare;
        ImageView ivImg;
        RoundTextView tvLabel;
        View lyItem;
        View listItemContainer;
        View listItemBtn;

        public ViewHolder(Context context, View itemView) {
            super(context, itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivComment = itemView.findViewById(R.id.iv_comment);
            ivZan = itemView.findViewById(R.id.iv_zan);
            ivShare = itemView.findViewById(R.id.iv_share);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvLabel = itemView.findViewById(R.id.tv_label);
            lyItem = itemView.findViewById(R.id.ly_item);
            listItemContainer = itemView.findViewById(R.id.list_item_container);
            listItemBtn = itemView.findViewById(R.id.list_item_btn);
        }
    }


}

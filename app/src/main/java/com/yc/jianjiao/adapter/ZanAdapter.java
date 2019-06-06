package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.CircleImageView;

import java.util.List;

/**
 * Created by edison on 2019/1/24.
 */

public class ZanAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public ZanAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);


        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getHead(), viewHolder.ivHead);
        String image = bean.getImage();
        if (StringUtils.isEmpty(image)){
            viewHolder.ivImg.setVisibility(View.VISIBLE);
            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + image, viewHolder.ivImg);
        }else {
            viewHolder.ivImg.setVisibility(View.GONE);
        }
        viewHolder.tvName.setText(bean.getNick());
        viewHolder.tvTime.setText(bean.getCreate_time());
        String emoji_content = bean.getEmoji_content();
        if (!StringUtils.isEmpty(emoji_content)){
            viewHolder.tvComment.setText(new String(EncodeUtils.base64Decode(emoji_content.getBytes())));
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_zan, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView ivHead;
        AppCompatTextView tvName;
        AppCompatTextView tvTime;
        ImageView ivImg;
        AppCompatTextView tvComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ivHead = itemView.findViewById(R.id.iv_head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvComment = itemView.findViewById(R.id.tv_comment);
            ivImg = itemView.findViewById(R.id.iv_img);
        }
    }


}

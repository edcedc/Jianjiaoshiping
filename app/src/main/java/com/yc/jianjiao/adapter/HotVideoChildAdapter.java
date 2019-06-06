package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.RoundImageView;

import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public class HotVideoChildAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public HotVideoChildAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);

        viewHolder.tvTitle.setText(bean.getTitle());
        viewHolder.tvFen.setText(bean.getMark() + "");
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getCover_image(), viewHolder.ivImg);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startVideoDescAct(bean.getId());
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_hot_video_child, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RoundImageView ivImg;
        AppCompatTextView tvFen;
        AppCompatTextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvFen = itemView.findViewById(R.id.tv_fen);
            tvTitle = itemView.findViewById(R.id.tv_title);

        }
    }

}

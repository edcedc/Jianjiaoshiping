package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flyco.roundview.RoundTextView;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.GlideLoadingUtils;

import java.util.List;

/**
 * Created by edison on 2019/1/22.
 */

public class WorthSeeingChildAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public WorthSeeingChildAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }
    public WorthSeeingChildAdapter(Context act, BaseFragment root, List<DataBean> listBean) {
        super(act, root, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        viewHolder.tvLabel.setVisibility(View.VISIBLE);
        viewHolder.tvLabel.setText(bean.getLabel_name());
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getCover_image(), viewHolder.ivImg);
        viewHolder.tvName.setText(bean.getTitle());
        viewHolder.tvContent.setText(bean.getBrief());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startVideoDescAct(bean.getId());
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_worth, parent, false));
    }

    class ViewHolder  extends RecyclerView.ViewHolder{

        ImageView ivImg;
        AppCompatTextView tvName;
        AppCompatTextView tvContent;
        RoundTextView tvLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvLabel = itemView.findViewById(R.id.tv_label);
        }
    }
}

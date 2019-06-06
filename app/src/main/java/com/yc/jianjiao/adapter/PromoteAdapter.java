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
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.CircleImageView;

import java.util.List;

/**
 * Created by edison on 2019/1/29.
 */

public class PromoteAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public PromoteAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);

        GlideLoadingUtils.load(act, "", viewHolder.ivHead);
        viewHolder.tvTitle.setText("用户昵称 拷贝");
        viewHolder.tvTime.setText("创建时间：" +
                "2018-12-17");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_promote, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView ivHead;
        AppCompatTextView tvTitle;
        AppCompatTextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ivHead = itemView.findViewById(R.id.iv_head);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}

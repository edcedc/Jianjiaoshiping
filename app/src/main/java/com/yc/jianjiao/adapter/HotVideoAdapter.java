package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.CircleImageView;
import com.yc.jianjiao.weight.GridDividerItemDecoration;

import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public class HotVideoAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public HotVideoAdapter(Context act, BaseFragment  root, List<DataBean> listBean) {
        super(act, root, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);

        if (position == 0){
            viewHolder.tvText.setBackgroundResource(R.mipmap.home_m03);
            viewHolder.tvText.setText("");
        }else if (position == 1){
            viewHolder.tvText.setBackgroundResource(R.mipmap.home_m04);
            viewHolder.tvText.setText("");
        }else if (position == 2){
            viewHolder.tvText.setBackgroundResource(R.mipmap.home_m06);
            viewHolder.tvText.setText("");
        }else {
            viewHolder.tvText.setBackgroundResource(0);
            viewHolder.tvText.setText("" + position);
        }

        viewHolder.tvName.setText(bean.getNick());
        viewHolder.tvTime.setText(bean.getBrief());
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getHead(), viewHolder.ivHead);
        viewHolder.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startStarAlbumFrg(root, bean.getId(), 0);
            }
        });

        List<DataBean> listStarVideo = bean.getListStarVideo();
        if (listStarVideo != null && listStarVideo.size() != 0){
            viewHolder.recyclerView.setVisibility(View.VISIBLE);
            HotVideoChildAdapter adapter = new HotVideoChildAdapter(act, listStarVideo);
            viewHolder.recyclerView.setLayoutManager(new GridLayoutManager(act, 3));
            viewHolder.recyclerView.setHasFixedSize(true);
            viewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            viewHolder.recyclerView.addItemDecoration(new GridDividerItemDecoration(10, ContextCompat.getColor(act,R.color.white)));
            viewHolder.recyclerView.setNestedScrollingEnabled(false);
            viewHolder.recyclerView.setAdapter(adapter);
        }else {
            viewHolder.recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_hot_video, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView tvText;
        AppCompatTextView tvName;
        AppCompatTextView tvTime;
        CircleImageView ivHead;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
            ivHead = itemView.findViewById(R.id.iv_head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

}

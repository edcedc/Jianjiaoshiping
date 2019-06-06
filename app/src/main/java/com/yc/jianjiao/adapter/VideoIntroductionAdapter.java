package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.utils.GlideLoadingUtils;

import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public class VideoIntroductionAdapter extends BaseRecyclerviewAdapter<DataBean> {
    public VideoIntroductionAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);

        GlideLoadingUtils.load(act, "", viewHolder.ivImg);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_introduction, parent, false));
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
        }
    }

}

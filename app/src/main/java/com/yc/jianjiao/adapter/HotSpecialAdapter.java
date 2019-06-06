package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.weight.WithScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/28.
 */

public class HotSpecialAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public HotSpecialAdapter(Context act, BaseFragment root, List<DataBean> listBean) {
        super(act, root, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);

        final List<DataBean> listStar = bean.getListStar();
        if (listStar != null && listStar.size() != 0){
            HotLabelAdapter adapter = new HotLabelAdapter(act, listStar);
            viewHolder.gridView.setAdapter(adapter);
            viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    UIHelper.startStarAlbumFrg(root, listStar.get(i).getId(), 1);
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_hot_special, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        WithScrollGridView gridView;

        public ViewHolder(View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.gridView);
        }
    }

}

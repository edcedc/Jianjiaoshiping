package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/26.
 */

public class AnthologyAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public AnthologyAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    private int mPosition = -1;

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);
        RoundViewDelegate delegate = viewHolder.tvAgreement.getDelegate();
        if (mPosition == position){
            viewHolder.tvAgreement.setTextColor(act.getColor(R.color.orange_FF7D2D));
        }else {
            viewHolder.tvAgreement.setTextColor(act.getColor(R.color.black_333333));
        }
        final int finalPosition = position;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)listener.onClick(finalPosition);
            }
        });
        viewHolder.tvAgreement.setText((position += 1) + "");
    }

    private onClickListener listener;
    public void setClickListener(onClickListener listener){
        this.listener = listener;
    }
    public interface onClickListener{
        void onClick(int position);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_anthology, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RoundTextView tvAgreement;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAgreement = itemView.findViewById(R.id.tv_agreement);
        }
    }

}

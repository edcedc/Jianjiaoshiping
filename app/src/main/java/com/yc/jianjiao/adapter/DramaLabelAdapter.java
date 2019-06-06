package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public class DramaLabelAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public DramaLabelAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    private int mPosition = -1;

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);
        viewHolder.tvText.setText(bean.getName());
        RoundViewDelegate delegate = viewHolder.tvText.getDelegate();
        if (bean.isSelect()){
            viewHolder.tvText.setTextColor(act.getColor(R.color.orange_FF7D2D));
            delegate.setBackgroundColor(act.getColor(R.color.white_f4f4f4));
        }else {
            viewHolder.tvText.setTextColor(act.getColor(R.color.gray_8E8E9C));
            delegate.setBackgroundColor(act.getColor(R.color.white));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)listener.onClick(position);
            }
        });
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
    public interface OnClickListener{
        void onClick(int position);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_drama_label, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RoundTextView tvText;

        public ViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }

}

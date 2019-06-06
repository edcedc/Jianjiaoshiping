package com.yc.jianjiao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flyco.roundview.RoundTextView;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/26.
 */

public class DowCacheAdapter extends BaseListViewAdapter<DataBean>{
    public DowCacheAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }
    private int mPosition = -1;

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }
    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_dow_cache, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);

        viewHolder.ivState.setVisibility(bean.getState() == 0 ? View.GONE : View.VISIBLE);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.tvAgreement.setText((position += 1) + "");
        return convertView;
    }

    private onClickListener listener;
    public void setClickListener(onClickListener listener){
        this.listener = listener;
    }
    public interface onClickListener{
        void onClick(int position);
    }

    class ViewHolder{

        RoundTextView tvAgreement;
        ImageView ivState;

        public ViewHolder(View convertView) {
            tvAgreement = convertView.findViewById(R.id.tv_agreement);
            ivState = convertView.findViewById(R.id.iv_state);
        }
    }

}

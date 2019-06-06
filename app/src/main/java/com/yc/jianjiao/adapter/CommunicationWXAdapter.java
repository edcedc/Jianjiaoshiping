package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.CircleImageView;

import java.util.List;

/**
 * Created by edison on 2019/1/30.
 */

public class CommunicationWXAdapter extends BaseListViewAdapter<DataBean> {
    public CommunicationWXAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_wx, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getImage(), viewHolder.ivImg);
        viewHolder.tvName.setText(bean.getName());
        return convertView;
    }

    class ViewHolder{

        AppCompatImageView ivImg;
        AppCompatTextView tvName;

        public ViewHolder(View convertView) {
            ivImg = convertView.findViewById(R.id.iv_img);
            tvName = convertView.findViewById(R.id.tv_name);
        }
    }

}


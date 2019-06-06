package com.yc.jianjiao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.utils.GlideLoadingUtils;
import com.yc.jianjiao.weight.SizeImageView;

import java.util.List;

/**
 * Created by edison on 2019/1/29.
 */

public class StarChildAdapter extends BaseListViewAdapter<DataBean>{
    public StarChildAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_img, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        viewHolder.ivImg.setWH(1, 1, true);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getImage(), viewHolder.ivImg);
        return convertView;
    }

    class ViewHolder{

        SizeImageView ivImg;

        public ViewHolder(View convertView) {
            ivImg = convertView.findViewById(R.id.iv_img);
        }
    }

}

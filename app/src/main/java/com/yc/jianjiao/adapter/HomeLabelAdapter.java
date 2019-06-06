package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.GlideLoadingUtils;

import java.util.List;

/**
 * Created by edison on 2019/1/20.
 */

public class HomeLabelAdapter extends BaseListViewAdapter<DataBean>{

    public HomeLabelAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_label, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);

        String name = bean.getName();
        if (StringUtils.isEmpty(name)){
            name = bean.getNick();
        }
        viewHolder.tvText.setText(name);

        String image = bean.getImage();
        if (StringUtils.isEmpty(image)){
            image = bean.getHead();
        }
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + image, viewHolder.ivImg);
        return convertView;
    }

    class ViewHolder{

        ImageView ivImg;
        AppCompatTextView tvText;

        public ViewHolder(View convertView) {
            ivImg = convertView.findViewById(R.id.iv_img);
            tvText = convertView.findViewById(R.id.tv_text);
        }
    }

}

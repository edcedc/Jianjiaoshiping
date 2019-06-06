package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flyco.roundview.RoundTextView;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.GlideLoadingUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/20.
 */

public class HomeChildListAdapter extends BaseListViewAdapter<DataBean> {

    public HomeChildListAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }
    public HomeChildListAdapter(Context act, BaseFragment root, List<DataBean> listBean) {
        super(act, root, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getCover_image(), viewHolder.ivImg);
        viewHolder.tvName.setText(bean.getTitle());
        viewHolder.tvContent.setText(bean.getVideo_desc());
        viewHolder.tvLabel.setVisibility(View.VISIBLE);
        viewHolder.tvLabel.setText(bean.getLabel_name());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startVideoDescAct(bean.getId());
            }
        });
        return convertView;
    }

    class ViewHolder  {

        ImageView ivImg;
        AppCompatTextView tvName;
        AppCompatTextView tvContent;
        RoundTextView tvLabel;

        public ViewHolder(View itemView) {
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvLabel = itemView.findViewById(R.id.tv_label);
        }
    }


}

package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/3/5.
 */

public class HomeChildScreenAdapter extends BaseListViewAdapter<DataBean>{
    public HomeChildScreenAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_screen, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        viewHolder.tvText.setText(bean.getName());
        return convertView;
    }

    class ViewHolder{

        AppCompatTextView tvText;

        public ViewHolder(View convertView) {
            tvText = convertView.findViewById(R.id.tv_text);
        }
    }

}

package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/26.
 */

public class VideoCommentChildAdapter extends BaseListViewAdapter<DataBean>{

    public VideoCommentChildAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    private boolean isLook = false;

    public void setLook(boolean look) {
        isLook = look;
    }

    public boolean isLook() {
        return isLook;
    }

    @Override
    public int getCount() {
        if (isLook){
            return listBean.size();
        }else {
            return listBean.size() >= 5 ? 5 : listBean.size();
        }
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_video_comment_child, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        String emoji_content = bean.getEmoji_content();
        if (emoji_content == null){
            showToast("后台emoji为kong ");
        }else{
            if (bean.getUser_id().equals(bean.getReply_user_id())){
                viewHolder.tvContent.setText(Html.fromHtml("<font color='#4162A7'>" + bean.getNick() +
                        "：" + "</font>" +
                        new String(EncodeUtils.base64Decode(emoji_content.getBytes()))));
            }else {
                viewHolder.tvContent.setText(Html.fromHtml("<font color='#4162A7'>" + bean.getNick() + "</font>" + "回复" +
                        "<font color='#4162A7'>" + bean.getReply_nick() + "：" + "</font>" +
                        new String(EncodeUtils.base64Decode(emoji_content.getBytes()))));
            }
        }

        return convertView;
    }
    class ViewHolder{

        AppCompatTextView tvContent;

        public ViewHolder(View convertView) {
            tvContent = convertView.findViewById(R.id.tv_content);
        }
    }

}

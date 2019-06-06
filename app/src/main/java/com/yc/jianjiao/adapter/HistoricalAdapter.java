package com.yc.jianjiao.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.blankj.utilcode.util.LogUtils;
import com.flyco.roundview.RoundTextView;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.utils.GlideLoadingUtils;

import java.io.File;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by edison on 2019/1/29.
 */

public class HistoricalAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public HistoricalAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }
     public HistoricalAdapter(Context act, List<DataBean> listBean, int type) {
        super(act, listBean);
        this.type = type;
    }

    private int type;

    private boolean isEdit = false;
    private boolean isAllSelect = false;

    public void setAllSelect(boolean allSelect) {
        isAllSelect = allSelect;
    }

    public boolean isAllSelect() {
        return isAllSelect;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        viewHolder.tvTitle.setText(bean.getTitle());
        viewHolder.tvContent.setText(bean.getVideo_desc());

        viewHolder.tvLabel.setText(bean.getLabel_name());
        if (type == 1){
            GlideLoadingUtils.load(act, bean.getCover(), viewHolder.ivImg);
            viewHolder.tvLabel.setVisibility(View.GONE);
        }else {
            GlideLoadingUtils.load(act, CloudApi.SERVLET_URL + bean.getCover_image(), viewHolder.ivImg);
            viewHolder.tvLabel.setVisibility(View.VISIBLE);
        }

        viewHolder.cb.setVisibility(isEdit == true ? View.VISIBLE : View.GONE);
        viewHolder.cb.setChecked(bean.isSelect());
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.setSelect(viewHolder.cb.isChecked());
//                if (listener != null)listener.onClick(position, viewHolder.cb.isChecked());
            }
        });

        if (isAllSelect){
            for (int i = 0;i < listBean.size();i++){
                DataBean bean1 = listBean.get(i);
                bean1.setSelect(true);
                viewHolder.cb.setChecked(true);
            }
        }else {
            for (DataBean bean1 : listBean){
                bean1.setSelect(false);
                viewHolder.cb.setChecked(false);
            }
        }



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String path = bean.getCover();//该路径可以自定义
                    LogUtils.e(path);
                    File file = new File(path);
                    Uri uri = Uri.fromFile(file);
                    intent.setDataAndType(uri, "video/*");
                    startActivity(intent);
                }else {
                    UIHelper.startVideoDescAct(bean.getId());
                }
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_historical, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox cb;
        AppCompatTextView tvTitle;
        AppCompatTextView tvContent;
        RoundTextView tvLabel;
        AppCompatImageView ivImg;

        public ViewHolder(View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cb);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvLabel = itemView.findViewById(R.id.tv_label);
        }
    }

}

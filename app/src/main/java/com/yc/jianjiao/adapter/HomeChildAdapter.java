package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.BaseRecyclerviewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.UIHelper;
import com.yc.jianjiao.weight.WithScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/20.
 */

public class HomeChildAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public HomeChildAdapter(Context act, BaseFragment root, List<DataBean> listBean) {
        super(act, root, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);

        switch (bean.getContent()){
            case "正在热播":
                viewHolder.tvTitle.setText("正在热播");
                viewHolder.ivImg.setBackgroundResource(R.mipmap.home_a14);
                viewHolder.tvMore.setVisibility(View.VISIBLE);
                break;
            case "什么值得看":
                viewHolder.tvTitle.setText("什么值得看");
                viewHolder.ivImg.setBackgroundResource(R.mipmap.home_a16);
                viewHolder.tvMore.setVisibility(View.GONE);
                break;
            case "最新看点":
                viewHolder.tvTitle.setText("最新看点");
                viewHolder.ivImg.setBackgroundResource(R.mipmap.home_d04);
                viewHolder.tvMore.setVisibility(View.GONE);
                break;
            case "分类栏":
                viewHolder.tvTitle.setText("分类栏");
                viewHolder.ivImg.setBackgroundResource(R.mipmap.home_d05);
                viewHolder.tvMore.setVisibility(View.GONE);
                break;
            case "大剧约起来":
                viewHolder.tvTitle.setText("大剧约起来");
                viewHolder.ivImg.setBackgroundResource(R.mipmap.home_e02);
                viewHolder.tvMore.setVisibility(View.GONE);
                break;
            default:
                viewHolder.tvTitle.setText("猜你喜欢");
                viewHolder.ivImg.setBackgroundResource(R.mipmap.home_a17);
                viewHolder.tvMore.setVisibility(View.GONE);
                break;
        }

        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        if (list != null && list.size() != 0){
            if (bean.getContent().equals("正在热播") || bean.getContent().equals("最新看点") || bean.getContent().equals("大剧约起来")){
                HomeChildListAdapter adapter = new HomeChildListAdapter(act, list);
                viewHolder.gridView.setAdapter(adapter);
                viewHolder.gridView.setNumColumns(2);
            }else {
                HomeChildList2Adapter adapter = new HomeChildList2Adapter(act, list);
                viewHolder.gridView.setAdapter(adapter);
                viewHolder.gridView.setNumColumns(3);
            }
            viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (bean.getContent()){
                        case "正在热播":
                            UIHelper.startVideoDescAct(bean.getId());
                            break;
                        case "什么值得看":
//                            UIHelper.startWorthSeeingFrg(root, whatListBean.get(i).getId());
                            break;
                        case "最新看点":
                            UIHelper.startVideoDescAct(bean.getId());
                            break;
                        case "分类栏":
                            UIHelper.startVideoDescAct(bean.getId());
                            break;
                        case "大剧约起来":
                            UIHelper.startVideoDescAct(bean.getId());
                            break;
                        default:
                            UIHelper.startVideoDescAct(bean.getId());
                            break;
                    }
                }
            });
        }

    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_home, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImg;
        AppCompatTextView tvTitle;
        AppCompatTextView tvMore;
        WithScrollGridView gridView;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMore = itemView.findViewById(R.id.tv_more);
            gridView = itemView.findViewById(R.id.gridView);
        }
    }


}

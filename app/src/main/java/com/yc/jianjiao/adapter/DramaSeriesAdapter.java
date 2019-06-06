package com.yc.jianjiao.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;
import com.yc.jianjiao.R;
import com.yc.jianjiao.base.BaseListViewAdapter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.view.HomeChildFrg;
import com.yc.jianjiao.weight.OrientationAwareRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/27.
 */

public class DramaSeriesAdapter extends BaseListViewAdapter<DataBean>{

    public DramaSeriesAdapter(Context act, List<DataBean> listBean, String screenId) {
        super(act, listBean);
        this.screenId = screenId;
    }

    private String screenId;

    @Override
    protected View getCreateVieww(final int pos, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_drama, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(pos);
        viewHolder.tvTitle.setText(bean.getName());
       /* RoundViewDelegate delegate = viewHolder.tvTitle.getDelegate();
        if (bean.isSelect()){
            viewHolder.tvTitle.setTextColor(act.getColor(R.color.orange_FF7D2D));
            delegate.setBackgroundColor(act.getColor(R.color.white_f4f4f4));
        }else {
            viewHolder.tvTitle.setTextColor(act.getColor(R.color.black_333333));
            delegate.setBackgroundColor(act.getColor(R.color.white));
        }


        viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtils.isEmpty(screenId)){//取消掉原来的逻辑
                    screenId = null;
                }

            }
        });*/



        final List<DataBean> list = bean.getListSubclassification();
        if (list != null && list.size() != 0){
            final DramaLabelAdapter adapter = new DramaLabelAdapter(act, list);
            viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder.recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
            viewHolder.recyclerView.setHasFixedSize(true);
            viewHolder.recyclerView.setNestedScrollingEnabled(false);
            viewHolder.recyclerView.setAdapter(adapter);

            if (!StringUtils.isEmpty(screenId)){//外面传进来的id
                for (int i = 0;i < list.size();i++){
                    DataBean bean1 = list.get(i);
                    if (bean1.getId().equals(screenId)){
                        bean1.setSelect(true);
                        adapter.notifyDataSetChanged();
                        List<String> listStr = new ArrayList<>();
                        listStr.add(bean1.getId());
                        if (listener != null){
                            listener.onClick(listStr);
                        }
                        break;
                    }
                }
            }
            if (!StringUtils.isEmpty(screenId) && screenId.equals(HomeChildFrg.allId)){//全部
                List<String> listId = new ArrayList<>();
                for (int i = 0;i < listBean.size();i++){
                    DataBean bean2 = listBean.get(i);
                    List<DataBean> listSubclassification1 = bean2.getListSubclassification();
                    for (int j = 0;j < listSubclassification1.size();j++){
                        DataBean bean3 = listSubclassification1.get(j);
                        if (j == 0){
                            bean3.setSelect(true);
                            String id = bean3.getId();
                            if (id.contains(",")){
                                String[] split = id.split(",");
                                for (String id1 : split){
                                    listId.add(id1);
                                }
                            }else {
                                listId.add(bean3.getId());
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                if (listener != null){
                    listener.onClick(listId);
                }
            }

            adapter.setOnClickListener(new DramaLabelAdapter.OnClickListener() {
                @Override
                public void onClick(int position) {
                    screenId = null;
                    for (int i = 0;i < list.size();i++){
                        DataBean bean2 = list.get(i);
                        if (position == i){
                            if (bean2.isSelect()){
                                bean2.setSelect(false);
                            }else {
                                bean2.setSelect(true);
                            }
                        }else {
                            bean2.setSelect(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    List<String> listId = new ArrayList<>();
                    for (int i = 0;i < listBean.size();i++){
                        for (DataBean bean3 : listBean.get(i).getListSubclassification()){
                            if (bean3.isSelect()){
                                String id = bean3.getId();
                                if (id.contains(",")){
                                    String[] split = id.split(",");
                                    for (String id1 : split){
                                        listId.add(id1);
                                    }
                                }else {
                                    listId.add(bean3.getId());
                                }

                            }
                        }
                    }
                    if (listener != null){
                        listener.onClick(listId);
                    }
                }
            });
        }
        return convertView;
    }

    private OnClickListener listener;
    public interface OnClickListener{
        void onClick(List<String> position);
    }
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    class ViewHolder{

        OrientationAwareRecyclerView recyclerView;
        RoundTextView tvTitle;

        public ViewHolder(View convertView) {
            recyclerView = convertView.findViewById(R.id.recyclerView);
            tvTitle = convertView.findViewById(R.id.tv_title);
        }
    }

}

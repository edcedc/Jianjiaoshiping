package com.yc.jianjiao.view.bottom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.DowCacheAdapter;
import com.yc.jianjiao.base.BaseBottomSheetFrag;
import com.yc.jianjiao.base.BasePresenter;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.databinding.PDowBinding;
import com.yc.jianjiao.weight.WithScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/26.
 */

@SuppressLint("ValidFragment")
public class DownloadBottomFrg extends BaseBottomSheetFrag<BasePresenter, PDowBinding> implements View.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private DowCacheAdapter adapter;
    private boolean isAllState = false;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        listBean = new Gson().fromJson(bundle.getString("list"), new TypeToken<ArrayList<DataBean>>() {}.getType());
    }

    @Override
    public int bindLayout() {
        return R.layout.p_dow;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.tv_all).setOnClickListener(this);
        view.findViewById(R.id.tv_look).setOnClickListener(this);
        WithScrollGridView gridView = view.findViewById(R.id.gridView);
        if (adapter == null){
            adapter = new DowCacheAdapter(act, listBean);
        }
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_all:
                for (int i = 0;i < listBean.size();i++){
                    DataBean bean = listBean.get(i);
                    bean.setState(isAllState == true ? 0 : 1);
                }
                adapter.notifyDataSetChanged();
                isAllState = !isAllState;
                break;
            case R.id.tv_look:
                if (listener != null){
                    listener.onLook();
                    dismiss();
                }
                break;
        }
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
    public interface OnClickListener{
        void onLook();
    }

}

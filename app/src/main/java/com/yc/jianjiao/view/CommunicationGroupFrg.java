package com.yc.jianjiao.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.luck.picture.lib.entity.LocalMedia;
import com.yc.jianjiao.R;
import com.yc.jianjiao.adapter.CommunicationQQAdapter;
import com.yc.jianjiao.adapter.CommunicationWXAdapter;
import com.yc.jianjiao.base.BaseFragment;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.controller.CloudApi;
import com.yc.jianjiao.databinding.FGroupBinding;
import com.yc.jianjiao.presenter.CommunicationPresenter;
import com.yc.jianjiao.view.impl.CommentContract;
import com.yc.jianjiao.view.impl.CommunicationContract;
import com.yc.jianjiao.weight.PictureSelectorTool;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/30.
 *  火爆交流群
 */

public class CommunicationGroupFrg extends BaseFragment<CommunicationPresenter, FGroupBinding> implements CommunicationContract.View{

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    private List<DataBean> listQQ = new ArrayList<>();
    private CommunicationQQAdapter qqAdapter;

    private List<DataBean> listWX = new ArrayList<>();
    private CommunicationWXAdapter wxAdapter;

    List<LocalMedia> localMediaQQList = new ArrayList<>();
    List<LocalMedia> localMediaWXList = new ArrayList<>();


    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_group;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.communication));
        if (qqAdapter == null){
            qqAdapter = new CommunicationQQAdapter(act, listQQ);
        }
        mB.gvQq.setAdapter(qqAdapter);
        mB.gvQq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PictureSelectorTool.PictureMediaType(act, localMediaQQList, i);
            }
        });

        if (wxAdapter == null){
            wxAdapter = new CommunicationWXAdapter(act, listWX);
        }
        mB.gvWx.setAdapter(wxAdapter);
        mB.gvWx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PictureSelectorTool.PictureMediaType(act, localMediaWXList, i);
            }
        });

        mB.refreshLayout.setPureScrollModeOn();
        showLoadDataing();
        mPresenter.onRequestQQ();
        mPresenter.onRequestWX();
        JSONObject basicGet = User.getInstance().getBasicGet();

        mB.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinQQGroup("");
            }
        });
    }

    @Override
    public void setWXData(List<DataBean> listBean) {
        listWX.addAll(listBean);
        wxAdapter.notifyDataSetChanged();
        for (DataBean bean : listWX){
            LocalMedia media = new LocalMedia();
            String url = CloudApi.SERVLET_URL + bean.getImage();
            String ss = "api/";
            String s = (String) url;
            if (s.contains(ss)){
                s = s.replace(ss, "");
            }
            media.setPath(s);
            media.setPictureType("image/jpeg");
            localMediaWXList.add(media);
        }
    }

    @Override
    public void setQQData(List<DataBean> listBean) {
        listQQ.addAll(listBean);
        qqAdapter.notifyDataSetChanged();
        for (DataBean bean : listQQ){
            LocalMedia media = new LocalMedia();
            String url = CloudApi.SERVLET_URL + bean.getImage();
            String ss = "api/";
            String s = (String) url;
            if (s.contains(ss)){
                s = s.replace(ss, "");
            }
            media.setPath(s);
            media.setPictureType("image/jpeg");
            localMediaQQList.add(media);
        }
    }

    /****************
     *
     * 发起添加群流程。群号：逗比(624288227) 的 key 为： LedXfelpP86qXFDmekbqe8XxVxfl6Vzj
     * 调用 joinQQGroup(LedXfelpP86qXFDmekbqe8XxVxfl6Vzj) 即可发起手Q客户端申请加群 逗比(624288227)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            showToast("未安装手Q或安装的版本不支持");
            return false;
        }
    }

}
